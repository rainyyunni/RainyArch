package projectbase.sharparch.hibernate.contracts.repositories;

import java.util.Map;
import java.util.List;

import projectbase.sharparch.domain.Enums;
import projectbase.sharparch.domain.persistencesupport.IDbContext;
import projectbase.sharparch.domain.persistencesupport.IRepositoryWithTypedId;

public interface IHibernateRepositoryWithTypedId<T, TId> extends
		IRepositoryWithTypedId<T, TId> {

	// / <summary>
	// / Provides a handle to application wide DB activities such as committing
	// any pending changes,
	// / beginning a transaction, rolling back a transaction, etc.
	// / </summary>
	IDbContext getDbContext() ;

	// / <summary>
	// / Dissasociates the entity with the ORM so that changes made to it are
	// not automatically
	// / saved to the database. More precisely, this removes the entity from
	// <see cref="ISession" />'s cache.
	// / More details may be found at
	// http://www.hibernate.org/hib_docs/nhibernate/html_single/#performance-sessioncache.
	// / </summary>
	void Evict(T entity) ;

	// / <summary>
	// / Looks for zero or more instances import the <see
	// cref="IDictionary{String, Object}"/> provided.
	// / The key of the collection should be the property name and the value
	// should be
	// / the value of the property to filter by.
	// / </summary>
	List<T> FindAll(Map<String, Object> propertyValuePairs);

	// / <summary>
	// / Looks for zero or more instances import the example provided.
	// / </summary>
	List<T> FindAll(T exampleInstance, String[] propertiesToExclude) ;

	// / <summary>
	// / Looks for a single instance import the property/values provided.
	// / </summary>
	// / <exception cref="NonUniqueResultException" />
	T FindOne(Map<String, Object> propertyValuePairs);

	// / <summary>
	// / Looks for a single instance import the example provided.
	// / </summary>
	// / <exception cref="NonUniqueResultException" />
	T FindOne(T exampleInstance, String[] propertiesToExclude);

	// / <summary>
	// / Returns null if a row is not found matching the provided Id.
	// / </summary>
	T Get(TId id, Enums.LockMode lockMode);

	// / <summary>
	// / Throws an exception if a row is not found matching the provided Id.
	// / </summary>
	T Load(TId id);

	// / <summary>
	// / Throws an exception if a row is not found matching the provided Id.
	// / </summary>
	T Load(TId id, Enums.LockMode lockMode);

	// / <summary>
	// / For entities that have assigned Id's, you must explicitly call Save to
	// add a new one.
	// / See
	// http://www.hibernate.org/hib_docs/nhibernate/html_single/#mapping-declaration-id-assigned.
	// / </summary>
	T Save(T entity) ;

	// / <summary>
	// / For entities that have assigned Id's, you should explicitly call Update
	// to update an existing one.
	// / Updating also allows you to commit changes to a detached Object. More
	// info may be found at:
	// /
	// http://www.hibernate.org/hib_docs/nhibernate/html_single/#manipulatingdata-updating-detached
	// / </summary>
	T Update(T entity);

}
