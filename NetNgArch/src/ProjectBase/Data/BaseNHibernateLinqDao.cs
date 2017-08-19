using System;
using System.Collections;
using System.Collections.Generic;
using System.Linq;
using NHibernate;
using NHibernate.Linq;
using ProjectBase.Utils;
using ProjectBase.Domain;
using SharpArch.Domain;
using SharpArch.NHibernate;
using System.Linq.Expressions;

namespace ProjectBase.Data
{
    public class BaseNHibernateLinqDao<T> : BaseNHibernateLinqDaoWithTypedId<T, int>, IGenericDao<T> where T : BaseDomainObject 
    {
    }

    public class BaseNHibernateLinqDaoWithTypedId<T, TId> : LinqRepositoryWithTypedId<T, TId>, IGenericDaoWithTypedId<T, TId> where T : BaseDomainObject 
    {

        private Type persitentType = typeof(T);

        public IQueryable<T> Query()
        {
            return Session.Query<T>();
        }
        
        public void Refresh(T entity)
        {
            Session.Refresh(entity, LockMode.Read);
        }
        /// <summary>
        /// Loads an instance of type T from the DB based on its ID.
        /// </summary>
        public T GetById(TId id, bool shouldLock) {
            T entity;

            if (shouldLock) {
                entity = (T)this.Session.Load(persitentType, id, LockMode.Upgrade);
            }
            else {
                entity = (T)this.Session.Load(persitentType, id);
            }
            return entity;
        }

        /// <summary>
        /// Get item count by filter
        /// </summary>
        public int GetCountByQuery(Expression<Func<T, bool>> where)
        {
            if (where == null) return Session.Query<T>().Count();

            var c = Session.Query<T>().Count(where);
            return c;
        }

        /// <summary>
        /// Loads every instance of the requested type with no filtering,no paging
        /// </summary>
        public IList<T> GetByQuery(Expression<Func<T, bool>> where, SortStruc<T>[] sort)
        {
            return FilterSortQuery(where, sort).ToList();
        }

        public IList<T> GetByQuery(Pager pager, Expression<Func<T, bool>> where, SortStruc<T>[] sort)
        {
            return PagerFilterSortQuery(pager,where, sort).ToList();
        }
        /// <summary>
        /// Looks for a single instance using the query condition and check the total count.
        /// when being asked for an unique object but found more,throw an exception
        /// </summary>
        /// <param name="unique">if the result should be unique,default to true</param>
        /// <returns>the unique or the first object or defalt object</returns>
        /// <exception cref="NonUniqueResultException"></exception>
        public T GetOneByQuery(Expression<Func<T, bool>> where, SortStruc<T>[] sort, bool unique)
        {
            var q = Session.Query<T>();

            if (where != null) q = q.Where(where);
            if (unique) return q.SingleOrDefault();
            if (sort == null) sort = GetDefaultSort();
            if (sort != null)
            {
                foreach (var s in sort)
                {
                    q = s.OrderByDirection == OrderByDirectionEnum.Asc
                            ? q.OrderBy(s.OrderByExpression)
                            : q.OrderByDescending(s.OrderByExpression);
                }
            }
            return q.FirstOrDefault();
        }

        /// <summary>
        /// Looks for a single instance using the query condition and check the total count.
        /// </summary>
        /// <param name="unique">if the count must not be more than 1</param>
        /// <returns></returns>
        public T GetOneByQuery(Expression<Func<T, bool>> where, bool unique)
        {
            return GetOneByQuery(where, null, unique);
        }

        /// <summary>
        /// 
        /// </summary>
        public IList<TResult> GetProjectionByQuery<TResult>(Expression<Func<T, bool>> where, SortStruc<T>[] sort, Expression<Func<T, TResult>> selector)
        {
            Check.Require(selector != null);

            return FilterSortQuery(where,sort).Select(selector).ToList();
        }
        public IList<TResult> GetProjectionByQuery<TResult>(Pager pager,Expression<Func<T, bool>> where, SortStruc<T>[] sort, Expression<Func<T, TResult>> selector)
        {
            Check.Require(selector != null);
            Check.Require(pager != null, "pager may not be null!");

            pager.ItemCount = where == null ? Session.Query<T>().Count() : Session.Query<T>().Count(where);
            var q = FilterSortQuery(where, sort).Select(selector);
            if(pager.PageSize>0)
                q = q.Skip(pager.FromRowIndex).Take(pager.PageSize);
            return q.ToList();

        }

        private IQueryable<T> FilterSortQuery(Expression<Func<T, bool>> where, SortStruc<T>[] sort)
        {
            var q = Session.Query<T>();

            if (where != null) q = q.Where(where);
            if (sort == null) sort = GetDefaultSort();
            if (sort != null)
            {
                foreach (var s in sort)
                {
                    q = s.OrderByDirection == OrderByDirectionEnum.Asc
                            ? q.OrderBy(s.OrderByExpression)
                            : q.OrderByDescending(s.OrderByExpression);
                }
            }
            return q;
        }
        private IQueryable<T> PagerFilterSortQuery(Pager pager,Expression<Func<T, bool>> where, SortStruc<T>[] sort )
        {
            Check.Require(pager != null, "pager may not be null!");

            pager.ItemCount = where==null?Session.Query<T>().Count():Session.Query<T>().Count(where);
            return pager.PageSize==0?FilterSortQuery(where, sort):FilterSortQuery(where, sort).Skip(pager.FromRowIndex).Take(pager.PageSize);
        }

        private SortStruc<T>[] GetDefaultSort()
        {
            try
            {
                return typeof (T).GetField("DefaultSort").GetValue(null) as SortStruc<T>[];
            }
            catch(Exception e)
            {
                return null;
            }
        }

    }
}
