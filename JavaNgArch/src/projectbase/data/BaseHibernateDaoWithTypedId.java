package projectbase.data;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import org.hibernate.Query;
import org.hibernate.annotations.common.util.StringHelper;
import org.hibernate.transform.Transformers;

import projectbase.domain.BaseDomainObject;
import projectbase.domain.BaseDomainObjectWithTypedId;
import projectbase.domain.DORef;
import projectbase.domain.DORefValue;
import projectbase.domain.IGenericDao;
import projectbase.domain.IGenericDaoWithTypedId;
import projectbase.sharparch.domain.Enums;
import projectbase.sharparch.domain.designbycontract.Check;
import projectbase.sharparch.hibernate.HibernateRepositoryWithTypedId;
import projectbase.utils.HqlCriterion;
import projectbase.utils.Pager;

    public class BaseHibernateDaoWithTypedId<T extends  BaseDomainObjectWithTypedId<Integer> , TId>  extends  HibernateRepositoryWithTypedId<T, TId> implements IGenericDaoWithTypedId<T, TId>
    {
    	protected String refTextFormula=BaseDomainObject.GetRefTextFormula();
    	
        public BaseHibernateDaoWithTypedId(Class<T> entityClassName){
        	entityClass=entityClassName;
        }
        public BaseHibernateDaoWithTypedId(Class<T> entityClassName,String refTextFormula){
        	entityClass=entityClassName;
        	this.refTextFormula=refTextFormula;
        }
        public void Refresh(T entity)
        {
           getSession().refresh(entity, LockOptionsFrom(Enums.LockMode.READ));
        }
        /// <summary>
        /// Loads an instance of type T from the DB based on its ID.
        /// </summary>
        public T GetById(TId id, boolean shouldLock) {
            T entity;

            if (shouldLock) {
                entity = (T)getSession().load(entityClass, (Serializable) id, LockOptionsFrom(Enums.LockMode.UPGRADE));
            }
            else {
                entity = (T)getSession().load(entityClass, (Serializable)id);
            }
            return entity;
        }

        /// <summary>
        /// Get item count by filter
        /// </summary>
        public int GetCountByQuery(String where)
        {
            String hql = " from " + GetEntityName(entityClass) + " as  this ";
            if (where != null && !where.isEmpty())
                hql += " where " + where;
            
            Long c = (Long)getSession().createQuery("select count(*) " + hql).uniqueResult();
            return c.intValue();
        }
        public int GetCountByQuery(HqlCriterion where)
        {
            String hql = " from " + GetEntityName(entityClass) + " as  this ";
            if (where != null && !where.IsEmpty())
                hql += " where " + where.ToHqlString();

            Long c = (Long)getSession().createQuery("select count(*) " + hql).setProperties(where.getParameters()).uniqueResult();
            return c.intValue();
        }

        /// <summary>
        /// Loads every instance of the requested type with no filtering,no paging
        /// </summary>
        public List<T> GetByQuery()
        {
            return GetByQuery("");
        }



        /// <summary>
        /// Loads every instance of the requested type with  filtering ,no paging
        /// </summary>
        public List<T> GetByQuery(String where)
        {
            return GetByQuery(where, null);
        }
        /// <summary>
        /// Loads every instance of the requested type with  filtering ,no paging
        /// </summary>
        public List<T> GetByQuery(HqlCriterion where)
        {
            return GetByQuery(where, null);
        }
        /// <summary>
        /// Loads every instance of the requested type with  filtering,no paging
        /// </summary>
        public List<T> GetByQuery(String where, String sort)
        {
            return (List<T>)FilterSortQuery(where, sort,null,null).list();
        }
        /// <summary>
        /// Loads every instance of the requested type with  filtering,no paging
        /// </summary>
        public List<T> GetByQuery(HqlCriterion where, String sort)
        {
            return (List<T>)FilterSortQuery(where, sort,null,null).list();
        }

        /// <summary>
        /// Loads every instance of the requested type with no filtering,but paging
        /// </summary>
        public List<T> GetByQuery(Pager pager)
        {
            return GetByQuery(pager, "");
        }

        /// <summary>
        /// Loads every instance of the requested type with no filtering,but paging
        /// </summary>
        public List<T> GetByQuery(Pager pager, String where)
        {
            return GetByQuery(pager, where, null);
        }
        /// <summary>
        /// Loads every instance of the requested type with no filtering,but paging
        /// </summary>
        public List<T> GetByQuery(Pager pager, HqlCriterion where)
        {
            return GetByQuery(pager, where, null);
        }
        public List<T> GetByQuery(Pager pager, String where, String sort)
        {
            return (List<T>)PagerFilterSortQuery(pager,where, sort).list();
        }
        public List<T> GetByQuery(Pager pager, HqlCriterion where, String sort)
        {
            return (List<T>)PagerFilterSortQuery(pager,where, sort).list();
        }
        /// <summary>
        /// Looks for a single instance import the query condition and check the total count.
        /// when being asked for an unique Object but found more,throw an exception
        /// </summary>
        /// <param name="unique">if the result should be unique,default to true</param>
        /// <returns>the unique or the first Object or defalt Object</returns>
        /// <exception cref="NonUniqueResultException"></exception>
        public T GetOneByQuery(String where, String sort, boolean unique)
        {
           
            String hql = " from " + GetEntityName(entityClass) + " as  this ";
            if (where != null && !where.isEmpty())
                hql += " where " + where;
            if (unique) {
            	return (T)getSession().createQuery(hql).uniqueResult();
            }
            
            String hqlcount = hql;
            if (sort == null) sort = GetDefaultSort();
            if (sort != null && !sort.isEmpty())
                hql += " Order By " + sort;
            Query q = getSession().createQuery(hql);
            q.setFirstResult(0);
            q.setMaxResults(1);

            List<T> foundList = (List<T>)q.list();

            if (foundList.size() > 0)
                return foundList.get(0);
            else
                return null;
        }
        public T GetOneByQuery(HqlCriterion where, String sort, boolean unique)
        {
           
            String hql = " from " + GetEntityName(entityClass) + " as  this ";
            if (where != null && !where.IsEmpty())
                hql += " where " + where.ToHqlString();
            if (unique) {
            	return (T)getSession().createQuery(hql).setProperties(where.getParameters()).uniqueResult();
            }
            
            String hqlcount = hql;
            if (sort == null) sort = GetDefaultSort();
            if (sort != null && !sort.isEmpty())
                hql += " Order By " + sort;
            Query q = getSession().createQuery(hql).setProperties(where.getParameters());
            q.setFirstResult(0);
            q.setMaxResults(1);

            List<T> foundList = (List<T>)q.list();

            if (foundList.size() > 0)
                return foundList.get(0);
            else
                return null;
        }

        /// <summary>
        /// Looks for a single and unique instance import the query condition.
        /// </summary>
        /// <returns></returns>
        public T GetOneByQuery(String where)
        {
            return GetOneByQuery(where, null);
        }
        public T GetOneByQuery(HqlCriterion where)
        {
            return GetOneByQuery(where, null);
        }
        /// <summary>
        /// Looks for the first instance import the query condition without checking on uniqueness
        /// </summary>
        /// <returns></returns>
        public T GetOneByQuery(String where, String sort)
        {
            return GetOneByQuery(where, sort,true);
        }
        public T GetOneByQuery(HqlCriterion where, String sort)
        {
            return GetOneByQuery(where, sort,true);
        }
        /// <summary>
        /// Looks for a single instance import the query condition and check the total count.
        /// </summary>
        /// <param name="unique">if the count must not be more than 1</param>
        /// <returns></returns>
        public T GetOneByQuery(String where, boolean unique)
        {
            return GetOneByQuery(where, null, unique);
        }
        public T GetOneByQuery(HqlCriterion where, boolean unique)
        {
            return GetOneByQuery(where, null, unique);
        }
        /// <summary>
        /// 
        /// </summary>
        @SuppressWarnings("unchecked")
		public <TResult> List<TResult> GetProjectionByQuery(Class<TResult> resultClass,String selector,String where, String sort)
        {
            Check.Require(selector != null);
            return (List<TResult>)FilterSortQuery(where,sort,selector,null).setResultTransformer(Transformers.aliasToBean(resultClass)).list();
        }
		public <TResult> List<TResult> GetProjectionByQuery(Class<TResult> resultClass,String selector,HqlCriterion where, String sort)
        {
            Check.Require(selector != null);
	        if (resultClass.isPrimitive() || resultClass==String.class)
	        {
	            return FilterSortQuery(where,sort,selector,null).list();
	        }
            return (List<TResult>)FilterSortQuery(where,sort,selector,null).setResultTransformer(Transformers.aliasToBean(resultClass)).list();
        }
        public <TResult> List<TResult> GetProjectionByQuery(Pager pager,Class<TResult> resultClass,String selector,String where, String sort)
        {
            Check.Require(selector != null);
            Check.Require(pager != null, "pager may not be null!");
            StringBuffer hqlcount=new StringBuffer();
            Query q = FilterSortQuery(where, sort,selector,hqlcount);
            Long c = (Long)getSession().createQuery("select count(*) " + hqlcount).uniqueResult();
            pager.setItemCount(c.intValue());
            if(pager.getPageSize()>0)
            	q.setFirstResult(pager.getFromRowIndex()).setMaxResults(pager.getPageSize());
            return (List<TResult>)q.setResultTransformer(Transformers.aliasToBean(resultClass)).list();

        }
        public <TResult> List<TResult> GetProjectionByQuery(Pager pager,Class<TResult> resultClass,String selector,HqlCriterion where, String sort)
        {
            Check.Require(selector != null);
            Check.Require(pager != null, "pager may not be null!");
            StringBuffer hqlcount=new StringBuffer();
            Query q = FilterSortQuery(where, sort,selector,hqlcount);
            Long c = (Long)getSession().createQuery("select count(*) " + hqlcount).setProperties(where.getParameters()).uniqueResult();
            pager.setItemCount(c.intValue());
            if(pager.getPageSize()>0)
            	q.setFirstResult(pager.getFromRowIndex()).setMaxResults(pager.getPageSize());
            return (List<TResult>)q.setResultTransformer(Transformers.aliasToBean(resultClass)).list();

        }
        public <TResult> List<TResult> GetProjectionByQuery(Pager pager,Class<TResult> resultClass,String selector,HqlCriterion where, String sort,String countSelector)
        {
            Check.Require(selector != null);
            Check.Require(pager != null, "pager may not be null!");
            StringBuffer hqlcount=new StringBuffer();
            Query q = FilterSortQuery(where, sort,selector,hqlcount);
            Long c = (Long)getSession().createQuery("select " +countSelector+ hqlcount).setProperties(where.getParameters()).uniqueResult();
            pager.setItemCount(c.intValue());
            if(pager.getPageSize()>0)
            	q.setFirstResult(pager.getFromRowIndex()).setMaxResults(pager.getPageSize());
            return (List<TResult>)q.setResultTransformer(Transformers.aliasToBean(resultClass)).list();

        }
        public DORef<T> GetRef(int id)
        { 
        	List<DORefValue> list=GetProjectionByQuery(DORefValue.class, "this.id as id,"+refTextFormula,"id="+id,null);
            return new DORef<T>(list.get(0));
        }
        public List<DORef<T>> GetRefList()
        {
            return GetProjectionByQuery(DORefValue.class, "this.id as id,"+refTextFormula,(String)null,null).parallelStream().map(o->new DORef<T>(o)).collect(Collectors.toList());
        }
        public List<DORef<T>> GetRefList(String where, String sort)
        {
            return GetProjectionByQuery(DORefValue.class, "this.id as id,"+refTextFormula,where, sort).parallelStream().map(o->new DORef<T>(o)).collect(Collectors.toList());
        }
        public List<DORef<T>> GetRefList(HqlCriterion where, String sort)
        {
            return GetProjectionByQuery(DORefValue.class, "this.id as id,"+refTextFormula,where, sort).parallelStream().map(o->new DORef<T>(o)).collect(Collectors.toList());
        }
        private Query FilterSortQuery(String where, String sort, String selector,StringBuffer hqlcount)
        {
        	StringBuffer hql=new StringBuffer();
        	if(selector!=null) hql.append("select "+selector);
        	hql.append(" from " + GetEntityName(entityClass) + " as  this ");
            if (where != null && !where.isEmpty())
            	hql.append(" where " + where);
            if(hqlcount!=null) hqlcount.append(hql.substring(hql.indexOf(" from ")));
            if (sort == null) sort = GetDefaultSort();
            if (sort != null && !sort.isEmpty())
            	hql.append(" Order By " + sort);
            Query q = getSession().createQuery(hql.toString());
            return q;
        }
        private Query FilterSortQuery(HqlCriterion where, String sort, String selector,StringBuffer hqlcount)
        {
        	StringBuffer hql=new StringBuffer();
        	if(selector!=null) hql.append("select "+selector);
        	hql.append(" from " + GetEntityName(entityClass) + " as  this ");
            if (where != null && !where.IsEmpty())
            	hql.append(" where " + where.ToHqlString());
            if(hqlcount!=null) hqlcount.append(hql.substring(hql.indexOf(" from ")));
            if (sort == null) sort = GetDefaultSort();
            if (sort != null && !sort.isEmpty())
            	hql.append(" Order By "+sort);
             Query q = getSession().createQuery(hql.toString()).setProperties(where.getParameters());
            return q;
        }
        private Query PagerFilterSortQuery(Pager pager,String where, String sort )
        {
            Check.Require(pager != null, "pager may not be null!");
            StringBuffer hqlcount=new StringBuffer();
            Query q=FilterSortQuery(where, sort,null,hqlcount);
            Long c = (Long)getSession().createQuery("select count(*) " + hqlcount).uniqueResult();
            pager.setItemCount(c.intValue());
            return pager.getPageSize()==0?q:q.setFirstResult(pager.getFromRowIndex()).setMaxResults(pager.getPageSize());
        }
        private Query PagerFilterSortQuery(Pager pager,HqlCriterion where, String sort )
        {
            Check.Require(pager != null, "pager may not be null!");
            StringBuffer hqlcount=new StringBuffer();
            Query q=FilterSortQuery(where, sort,null,hqlcount);

            Long c =(Long) getSession().createQuery("select count(*) " + hqlcount).setProperties(where.getParameters()).uniqueResult();
            
            pager.setItemCount(c.intValue());

            return pager.getPageSize()==0?q:q.setFirstResult(pager.getFromRowIndex()).setMaxResults(pager.getPageSize());
        }
        private String GetDefaultSort()
        {
            try
            {
                return (String)entityClass.getField("DefaultSort").get(null);
            }
            catch(Exception e)
            {
                return null;
            }
        }
        
        private String GetEntityName(String entityClass){
        	return StringHelper.unqualify(entityClass);
        }
        private String GetEntityName(Class<T> entityClass){
        	return StringHelper.unqualify(entityClass.getSimpleName());
        }

    }

