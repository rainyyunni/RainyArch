package projectbase.bd;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.ObjectNotFoundException;

import projectbase.domain.BaseDomainObjectWithTypedId;
import projectbase.domain.DORef;
import projectbase.domain.ICommonBD;
import projectbase.domain.IGenericDao;
import projectbase.utils.HqlCriterion;
import projectbase.utils.InvalidOperationException;
import projectbase.utils.Pager;
import projectbase.utils.QuerySelectorBuilder;
    public class CommonBD<T extends  BaseDomainObjectWithTypedId<Integer> >  extends  BaseBusinessDelegate implements ICommonBD<T>
    {
 
        protected IGenericDao<T> _dao;
 
        public IGenericDao<T> getDao() {
			return _dao;
		}


		public CommonBD()
        {
        }
        
        public CommonBD(  IGenericDao<T> dao)
        {
            _dao = dao;
        }
        public void setDao(IGenericDao<T> value){
        	_dao=value;
        }
        public List<T> GetAll()
        {
            return _dao.GetAll();
        }

        public T Get(Integer id)
        {
            if (id==null||id==0) return null;
            return _dao.Load(id);
        }

        public void Save(T domainObject)
        {
            _dao.SaveOrUpdate(domainObject);
        }

        public void Delete(T domainObject)
        {
            _dao.Delete(domainObject);
        }
        public void Delete(int id)
        {
            try
            {
                T domainObject=_dao.Load(id);
                _dao.Delete(domainObject);
            }
            catch(ObjectNotFoundException e)
            {
                
            }
        }
        public int GetCountByQuery(String where)
        {
            return _dao.GetCountByQuery(where);
        }
        public int GetCountByQuery(HqlCriterion where)
        {
            return _dao.GetCountByQuery(where);
        }
        public List<T> GetByQuery(String where)
        {
            return _dao.GetByQuery(where);
        }
        public List<T> GetByQuery(HqlCriterion where)
        {
            return _dao.GetByQuery(where);
        }
        public List<T> GetByQuery(String where, String sort)
        {
            return _dao.GetByQuery(where, sort);
        }
        public List<T> GetByQuery(HqlCriterion where, String sort)
        {
            return _dao.GetByQuery(where, sort);
        }
        public List<T> GetByQuery(Pager pager)
        {
            return _dao.GetByQuery(pager);
        }
        public List<T> GetByQuery(Pager pager, String where)
        {
            return _dao.GetByQuery(pager,where);
        }
        public List<T> GetByQuery(Pager pager, HqlCriterion where)
        {
            return _dao.GetByQuery(pager,where);
        }
        public List<T> GetByQuery(Pager pager, String where, String sort)
        {
            return _dao.GetByQuery(pager, where, sort);
        }
        public List<T> GetByQuery(Pager pager, HqlCriterion where, String sort)
        {
            return _dao.GetByQuery(pager, where, sort);
        }
        public T GetOneByQuery(String where)
        {
            return _dao.GetOneByQuery(where);
        }
        public T GetOneByQuery(HqlCriterion where)
        {
            return _dao.GetOneByQuery(where);
        }
        public T GetOneByQuery(String where, boolean unique)
        {
            return _dao.GetOneByQuery(where, unique);
        }
        public T GetOneByQuery(HqlCriterion where, boolean unique)
        {
            return _dao.GetOneByQuery(where, unique);
        }
        public T GetOneByQuery(String where, String sort)
        {
            return _dao.GetOneByQuery(where, sort);
        }
        public T GetOneByQuery(HqlCriterion where, String sort)
        {
            return _dao.GetOneByQuery(where, sort);
        }
        public T GetOneByQuery(String where, String sort, boolean unique)
        {
            return _dao.GetOneByQuery(where, sort, unique);
        }
        public T GetOneByQuery(HqlCriterion where, String sort, boolean unique)
        {
            return _dao.GetOneByQuery(where, sort, unique);
        }
        public DORef<T> GetRef(int id)
        { 
        	return _dao.GetRef(id);
        }
        public List<DORef<T>> GetRefList()
        {
        	return _dao.GetRefList();        
        }
        public List<DORef<T>> GetRefList(String where, String sort)
        {
        	return _dao.GetRefList(where,sort);     
        }
        public List<DORef<T>> GetRefList(HqlCriterion where)
        {
        	return _dao.GetRefList(where,null); 
        }
        public List<DORef<T>> GetRefList(HqlCriterion where, String sort)
        {
        	return _dao.GetRefList(where,sort); 
        }
        public <TDto>List<TDto> GetDtoList(Pager pager, Class<TDto> dtoClass,String selector, String where, String sort)
        {
            return _dao.<TDto>GetProjectionByQuery(pager,dtoClass, selector,where, sort);
        }
        public <TDto>List<TDto> GetDtoList(Pager pager, Class<TDto> dtoClass,String selector, HqlCriterion where, String sort)
        {
            return _dao.<TDto>GetProjectionByQuery(pager,dtoClass, selector,where, sort);
        }
        public <TDto> List<TDto> GetDtoList(Pager pager, Class<TDto> dtoClass, String where)
        {
            return GetDtoList(pager, dtoClass,where, null, (Map<String, String>)null);
        }
                /// <summary>
        /// automatically transform T to TDto,according to property-flatten pattern like automapper.(Use Caption to identify words)
        /// </summary>
        /// <typeparam name="TDto"></typeparam>
        /// <param name="pager"></param>
        /// <param name="where"></param>
        /// <param name="sort"></param>
        /// <returns></returns>
        public <TDto> List<TDto> GetDtoList(Pager pager, Class<TDto> dtoClass, String where, String sort)
        {
            return GetDtoList(pager, dtoClass,where, sort, (Map<String, String>)null);
        }
        public <TDto> List<TDto> GetDtoList(Pager pager, Class<TDto> dtoClass, HqlCriterion where)
        {
            return GetDtoList(pager, dtoClass,where, null, (Map<String, String>)null);
        }
        public <TDto> List<TDto> GetDtoList(Pager pager, Class<TDto> dtoClass, HqlCriterion where, String sort)
        {
            return GetDtoList(pager, dtoClass,where, sort, (Map<String, String>)null);
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
        public <TDto> List<TDto> GetDtoList(Pager pager, Class<TDto> dtoClass,String where, String sort,Map<String,String> propNameMap)
        {
        	String selector=QuerySelectorBuilder.BuildSelector(_dao.getEntityClass(),dtoClass,propNameMap);
            return _dao.<TDto>GetProjectionByQuery(pager,dtoClass, selector,where, sort);

        }

        public <TDto> List<TDto> GetDtoList(Pager pager, Class<TDto> dtoClass,HqlCriterion where, String sort,Map<String,String> propNameMap)
        {
        	String selector=QuerySelectorBuilder.BuildSelector(_dao.getEntityClass(),dtoClass,propNameMap);
            return _dao.<TDto>GetProjectionByQuery(pager,dtoClass, selector,where, sort);

        }
         public <TDto>List<TDto> GetDtoList(Class<TDto> dtoClass,String selector, String where, String sort)
        {
            return _dao.<TDto>GetProjectionByQuery(dtoClass, selector,where, sort);
        }
        public <TDto>List<TDto> GetDtoList(Class<TDto> dtoClass,String selector, HqlCriterion where, String sort)
        {
            return _dao.<TDto>GetProjectionByQuery(dtoClass, selector,where, sort);
        }
                /// <summary>
        /// automatically transform T to TDto,according to property-flatten pattern like automapper.(Use Caption to identify words)
        /// </summary>
        /// <typeparam name="TDto"></typeparam>
        /// <param name="pager"></param>
        /// <param name="where"></param>
        /// <param name="sort"></param>
        /// <returns></returns>
        public <TDto> List<TDto> GetDtoList(Class<TDto> dtoClass, String where, String sort)
        {
            return GetDtoList(dtoClass,where, sort, (Map<String, String>)null);
        }
        public <TDto> List<TDto> GetDtoList(Class<TDto> dtoClass, HqlCriterion where, String sort)
        {
            return GetDtoList(dtoClass,where, sort, (Map<String, String>)null);
        }

        public <TDto> List<TDto> GetDtoList(Class<TDto> dtoClass,String where, String sort,Map<String,String> propNameMap)
        {
			String selector=QuerySelectorBuilder.BuildSelector(_dao.getEntityClass(),dtoClass,propNameMap);
            return _dao.<TDto>GetProjectionByQuery(dtoClass, selector,where, sort);

        }

        public <TDto> List<TDto> GetDtoList(Class<TDto> dtoClass,HqlCriterion where, String sort,Map<String,String> propNameMap)
        {
			String selector=QuerySelectorBuilder.BuildSelector(_dao.getEntityClass(),dtoClass,propNameMap);
            return _dao.<TDto>GetProjectionByQuery(dtoClass, selector,where, sort);

        }
  
    }

