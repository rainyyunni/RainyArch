using System;
using System.Collections.Generic;
using System.Linq;
using System.Reflection;
using NHibernate;
using ProjectBase.Domain;
using ProjectBase.Utils;
using System.Linq.Expressions;
using ProjectBase.Web.Mvc;
using System.Text;

namespace ProjectBase.BusinessDelegate
{
    public class CommonBD<T> : BaseBusinessDelegate, ICommonBD<T> where T : BaseDomainObject
    {
        private static char[] _caption=new char[]{
                                                           'A','B','C','D','E','F','G','H','I','J','K','L',
                                                           'M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'
        };

        protected IGenericDao<T> _dao;

        public CommonBD(IGenericDao<T> dao)
        {
            _dao = dao;
        }

        public virtual IList<T> GetAll()
        {
            return _dao.GetAll();
        }

        public virtual T Get(int? id)
        {
            if ((id??0) == 0) return null;
            return _dao.Load(id.Value);
        }

        public virtual void Save(T domainObject)
        {
            _dao.SaveOrUpdate(domainObject);
        }

        public virtual void Delete(T domainObject)
        {
            _dao.Delete(domainObject);
        }
        public virtual void Delete(int id)
        {
            try
            {
                var domainObject=_dao.Load(id);
                _dao.Delete(domainObject);
            }
            catch(ObjectNotFoundException)
            {
                
            }
        }
        public virtual int GetCountByQuery(Expression<Func<T, bool>> where)
        {
            return _dao.GetCountByQuery(where);
        }
        public virtual IList<T> GetByQuery(Expression<Func<T, bool>> where, string sort=null)
        {
            return _dao.GetByQuery(where, SortStruc<T>.CreateFrom(sort));
        }
        public virtual IList<T> GetByQuery(Pager pager, Expression<Func<T, bool>> where=null, string sort=null)
        {
            return _dao.GetByQuery(pager, where, SortStruc<T>.CreateFrom(sort));
        }
        public virtual T GetOneByQuery(Expression<Func<T, bool>> where=null, bool unique=true)
        {
            return _dao.GetOneByQuery(where, unique);
        }
        public virtual T GetOneByQuery(Expression<Func<T, bool>> where, string sort=null, bool unique=true)
        {
            return _dao.GetOneByQuery(where, SortStruc<T>.CreateFrom(sort), unique);
        }
        public virtual DORef<T> GetRef(int id)
        {
            return _dao.GetProjectionByQuery(o=>o.Id==id, null, o => new DORef<T> { Id = o.Id, RefText = o.RefText }).FirstOrDefault();
        }
        public virtual IList<DORef<T>> GetRefList(Expression<Func<T, bool>> where=null, string sort=null)
        {
            return _dao.GetProjectionByQuery(where, SortStruc<T>.CreateFrom(sort), o => new DORef<T> { Id = o.Id, RefText = o.RefText });
        }
        public virtual IList<TDto> GetDtoList<TDto>(Expression<Func<T, bool>> where = null, string sort = null, Expression<Func<T, TDto>> selector = null)
        {
            return _dao.GetProjectionByQuery(where, SortStruc<T>.CreateFrom(sort), selector);
        }
        public virtual IList<TDto> GetDtoList<TDto>(Pager pager, Expression<Func<T, bool>> where=null, string sort=null, Expression<Func<T, TDto>> selector=null)
        {
            return _dao.GetProjectionByQuery(pager,where, SortStruc<T>.CreateFrom(sort), selector);
        }
        public IQueryable<T> Query()
        {
            return _dao.Query();
        }

        /// <summary>
        /// automatically transform T to TDto,according to property-flatten pattern like automapper.(Use Caption to identiyfy words)
        /// </summary>
        /// <typeparam name="TDto"></typeparam>
        /// <param name="pager"></param>
        /// <param name="where"></param>
        /// <param name="sort"></param>
        /// <param name="propNameMap">use this to manually set mapping propname of DTO to prop of DO.words are delimetered by '|'
        /// </param>
        /// <returns></returns>
        public virtual IList<TDto> GetDtoList<TDto>(Pager pager, Expression<Func<T, bool>> where=null, string sort=null,IDictionary<string,string> propNameMap=null)
        {
            //following to make lamda like o=>new DTO{Id=o.Id,Other=o.Other}}
            var props = typeof(TDto).GetProperties().Where(o=>o.CanWrite).ToArray();
            MemberBinding[] propExprs = null;
            var parameterExpr = Expression.Parameter(typeof (T), "o");
            var newDtoExpr = Expression.New(typeof(TDto));//new TDto;
            
            Array.Resize(ref propExprs, props.Count());
            for (var i = 0; i < props.Count(); i++)//Id=o.Id;ReferenceId=o.Reference??null:o.Reference.Id
            {
                var entityPropExpr = (propNameMap != null && propNameMap.ContainsKey(props[i].Name))
                    ? ChainedPropExprByMap(parameterExpr, propNameMap[props[i].Name], typeof(T),props[i].PropertyType)
                    : ChainedPropExpr(parameterExpr, props[i].Name, typeof(T),props[i].PropertyType);
                if (entityPropExpr == null) throw new Exception("can't figure out " + props[i].Name);
                MemberAssignment assign; 
                try
                {
                    assign =Expression.Bind(props[i],entityPropExpr); 
                 }catch(ArgumentException e)
                 {
                     throw new Exception("assignment error：" +props[i].Name+" "+ e.Message);
                 }
                propExprs[i] = assign;

            }
            var initExpr = Expression.MemberInit(newDtoExpr, propExprs);
            var selector = Expression.Lambda<Func<T, TDto>>(initExpr, new ParameterExpression[]{parameterExpr});
            return pager==null?_dao.GetProjectionByQuery(where, SortStruc<T>.CreateFrom(sort), selector):_dao.GetProjectionByQuery(pager, where, SortStruc<T>.CreateFrom(sort), selector);

        }
        public virtual IList<TDto> GetDtoList<TDto>(Expression<Func<T, bool>> where = null, string sort = null, IDictionary<string, string> propNameMap = null)
        {
            return GetDtoList<TDto>(null, where, sort, propNameMap);
        }

        private Expression ChainedPropExpr(Expression expr,string propName,Type containerType,Type dtoPropType)
        {
            if (propName=="") return null;
            var entityProp = containerType.GetProperty(propName);
            if (entityProp!=null) return Expression.Property(expr, propName);
            var firstWordEnd = propName.IndexOfAny(_caption, 1);
            if(firstWordEnd<=0) return null;
            var firstWord = propName.Substring(0, firstWordEnd);
            entityProp = containerType.GetProperty(firstWord);
            if (entityProp==null) return null;
            var firstPart = Expression.Property(expr, firstWord);
            var sencondPart = propName.Substring(firstWordEnd);
            if (dtoPropType.IsValueType && !Util.IsNullableType(dtoPropType))
                return ChainedPropExpr(firstPart, sencondPart, entityProp.PropertyType, dtoPropType);

           return Expression.Condition(Expression.Equal(firstPart, Expression.Constant(null)),
                Expression.Constant(null, dtoPropType), Expression.Convert(ChainedPropExpr(firstPart, sencondPart, entityProp.PropertyType, dtoPropType), dtoPropType));
        }
        private Expression ChainedPropExprByMap(Expression expr, string propMappedName, Type containerType, Type dtoPropType)
        {
            if (propMappedName == "") return null;
            var entityProp = containerType.GetProperty(propMappedName);
            if (entityProp != null) return Expression.Property(expr, propMappedName);
            var firstWordEnd = propMappedName.IndexOf("|");
            if (firstWordEnd <= 0) return null;
            var firstWord = propMappedName.Substring(0, firstWordEnd);
            entityProp = containerType.GetProperty(firstWord);
            if (entityProp == null) return null;
            var firstPart = Expression.Property(expr, firstWord);
            var sencondPart = propMappedName.Substring(firstWordEnd+1);
            if (dtoPropType.IsValueType && !Util.IsNullableType(dtoPropType))
                return ChainedPropExprByMap(firstPart, sencondPart, entityProp.PropertyType, dtoPropType);

            return Expression.Condition(Expression.Equal(firstPart, Expression.Constant(null)),
                Expression.Constant(null, dtoPropType), Expression.Convert(ChainedPropExprByMap(firstPart, sencondPart, entityProp.PropertyType, dtoPropType),dtoPropType));
        }
    }
}
