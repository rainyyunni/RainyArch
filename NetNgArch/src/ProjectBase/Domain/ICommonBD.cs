using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using ProjectBase.Utils;
using System.Linq.Expressions;
using ProjectBase.Web.Mvc;

namespace ProjectBase.Domain
{
    public interface ICommonBD<T> : IBusinessDelegate where T : BaseDomainObject
    {
        IList<T> GetAll();
        T Get(int? id);
        void Save(T domainObject);
        void Delete(T domainObject);
        void Delete(int id);

        int GetCountByQuery(Expression<Func<T, bool>> where);
        IList<T> GetByQuery(Expression<Func<T, bool>> where, string sort=null);
        IList<T> GetByQuery(Pager pager, Expression<Func<T, bool>> where=null, string sort=null);
        T GetOneByQuery(Expression<Func<T, bool>> where, bool unique);
        T GetOneByQuery(Expression<Func<T, bool>> where, string sort=null, bool unique=true);
        DORef<T> GetRef(int id);
        IList<DORef<T>> GetRefList(Expression<Func<T, bool>> where=null, string sort=null);
        IList<TDto> GetDtoList<TDto>(Pager pager, Expression<Func<T, bool>> where = null, string sort = null,
                                     IDictionary<string, string> propNameMap=null);
        IList<TDto> GetDtoList<TDto>(Pager pager, Expression<Func<T, bool>> where , string sort,
                                     Expression<Func<T, TDto>> selector);
        IList<TDto> GetDtoList<TDto>(Expression<Func<T, bool>> where = null, string sort = null,
                             IDictionary<string, string> propNameMap = null);
        IList<TDto> GetDtoList<TDto>(Expression<Func<T, bool>> where, string sort,
                                     Expression<Func<T, TDto>> selector);
        IQueryable<T> Query();
    }
}
