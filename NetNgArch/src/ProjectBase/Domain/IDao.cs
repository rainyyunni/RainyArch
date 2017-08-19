using System;
using System.Collections.Generic;
using System.Linq;
using ProjectBase.Utils;
using SharpArch.Domain.Specifications;
using SharpArch.NHibernate.Contracts.Repositories;
using System.Linq.Expressions;

namespace ProjectBase.Domain
{
    public interface IGenericDaoWithTypedId<T, TId> : INHibernateRepositoryWithTypedId<T, TId> where T: BaseDomainObject
    {
        int GetCountByQuery(Expression<Func<T, bool>> where);
        IList<T> GetByQuery(Expression<Func<T, bool>> where, SortStruc<T>[] sort=null);
        IList<T> GetByQuery(Pager pager, Expression<Func<T, bool>> where=null, SortStruc<T>[] sort=null);
        T GetOneByQuery(Expression<Func<T, bool>> where, bool unique);
        T GetOneByQuery(Expression<Func<T, bool>> where, SortStruc<T>[] sort=null, bool unique=true);
        void Refresh(T entity);
        IQueryable<T> Query();
        IList<TResult> GetProjectionByQuery<TResult>(Pager pager, Expression<Func<T, bool>> where, SortStruc<T>[] sort,
                                   Expression<Func<T, TResult>> selector);
        IList<TResult> GetProjectionByQuery<TResult>(Expression<Func<T, bool>> where, SortStruc<T>[] sort,
                                           Expression<Func<T, TResult>> selector);

        /// <summary>
        /// Finds an item by id.
        /// </summary>
        /// <typeparam name="T">Type of entity to find</typeparam>
        /// <param name="id">The id of the entity</param>
        /// <returns>The matching item</returns>
        T FindOne(TId id);

        /// <summary>
        /// Finds an item by a specification
        /// </summary>
        /// <param name="specification">The specification.</param>
        /// <typeparam name="T">Type of entity to find</typeparam>
        /// <returns>The the matching item</returns>
        T FindOne(ILinqSpecification<T> specification);

        /// <summary>
        /// Finds all items within the repository.
        /// </summary>
        /// <typeparam name="T">Type of entity to find</typeparam>
        /// <returns>All items in the repository</returns>
        IQueryable<T> FindAll();

        /// <summary>
        /// Finds all items by a specification.
        /// </summary>
        /// <param name="specification">The specification.</param>
        /// <typeparam name="T">Type of entity to find</typeparam>
        /// <returns>All matching items</returns>
        IQueryable<T> FindAll(ILinqSpecification<T> specification);
    }

    public interface IGenericDao<T> : IGenericDaoWithTypedId<T, int> where T : BaseDomainObject 
    {
    }

}
