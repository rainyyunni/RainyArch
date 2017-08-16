package projectbase.sharparch.hibernate;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.LockMode;
import org.hibernate.LockOptions;
import org.hibernate.NonUniqueResultException;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Restrictions;

import projectbase.sharparch.domain.domainmodel.IHasAssignedId;
import projectbase.sharparch.hibernate.contracts.repositories.IHibernateRepositoryWithTypedId;
import projectbase.sharparch.domain.Enums;
import projectbase.sharparch.domain.designbycontract.Check;
import projectbase.sharparch.domain.persistencesupport.IDbContext;
/// <summary>
///     Provides a fully loaded DAO which may be created in a few ways including:
///     * Direct instantiation; e.g., new GenericDao<Customer, String>
///     * Spring configuration; e.g., <Object id = "CustomerDao" type = "SharpArch.Data.NHibernateSupport.GenericDao&lt;CustomerAlias, String>, SharpArch.Data" autowire = "byName" />
/// </summary>

public class HibernateRepositoryWithTypedId<T, TId>
		implements IHibernateRepositoryWithTypedId<T, TId> {

	protected Class<T> entityClass=null;
	private IDbContext _dbContext;

	public IDbContext getDbContext() {

		if (_dbContext == null) {
			String factoryKey = SessionFactoryKeyHelper.GetKeyFrom(this);
			_dbContext = new DbContext(factoryKey);
		}

		return _dbContext;
	}
	public Class<T> getEntityClass() {
		return entityClass;
	}
	protected Session getSession() {

		String factoryKey = SessionFactoryKeyHelper.GetKeyFrom(this);
		return HibernateSession.CurrentFor(factoryKey);

	}

	public void Evict(T entity) {
		getSession().evict(entity);
	}

	@SuppressWarnings("unchecked")
	public List<T> FindAll(T exampleInstance, String[] propertiesToExclude) {
		Criteria criteria = getSession().createCriteria(entityClass);
		Example example = Example.create(exampleInstance);

		for (String propertyToExclude : propertiesToExclude) {
			example.excludeProperty(propertyToExclude);
		}

		criteria.add(example);

		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	public List<T> FindAll(Map<String, Object> propertyValuePairs) {
		Check.Require(
				propertyValuePairs != null && propertyValuePairs.size() > 0,
				"propertyValuePairs was null or empty; "
						+ "it has to have at least one property/value pair in it");

		Criteria criteria = getSession().createCriteria(entityClass);

		for (String key : propertyValuePairs.keySet()) {
			criteria.add(propertyValuePairs.get(key) != null ? Restrictions.eq(
					key, propertyValuePairs.get(key)) : Restrictions
					.isNull(key));
		}

		return criteria.list();
	}

	public T FindOne(T exampleInstance, String[] propertiesToExclude) {
		List<T> foundList = this.FindAll(exampleInstance, propertiesToExclude);

		if (foundList.size() > 1) {
			throw new NonUniqueResultException(foundList.size());
		}

		if (foundList.size() == 1) {
			return foundList.get(0);
		}

		return null;
	}

	public T FindOne(Map<String, Object> propertyValuePairs) {
		List<T> foundList = this.FindAll(propertyValuePairs);

		if (foundList.size() > 1) {
			throw new NonUniqueResultException(foundList.size());
		}

		if (foundList.size() == 1) {
			return foundList.get(0);
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	public T Get(TId id, Enums.LockMode lockMode) {
		return (T) getSession().get(entityClass, (Serializable) id,
				LockOptionsFrom(lockMode));
	}

	@SuppressWarnings("unchecked")
	public T Load(TId id) {
		return (T) getSession().load(entityClass, (Serializable) id);
	}

	@SuppressWarnings("unchecked")
	public T Load(TId id, Enums.LockMode lockMode) {
		return (T) getSession()
				.load(entityClass, LockOptionsFrom(lockMode));
	}

	public T Save(T entity) {
		getSession().save(entity);
		return entity;
	}

	public T Update(T entity) {
		getSession().update(entity);
		return entity;
	}

	public void Delete(T entity) {
		getSession().delete(entity);
	}

	@SuppressWarnings("unchecked")
	public T Get(TId id) {
		return (T) getSession().get(entityClass, (Serializable) id);
	}

	public List<T> GetAll() {
		Criteria criteria = getSession().createCriteria(entityClass);
		return (List<T>)criteria.list();
	}

	// / <summary>
	// / Although SaveOrUpdate _can_ be invoked to update an Object with an
	// assigned Id, you are
	// / hereby forced instead to use Save/Update for better clarity.
	// / </summary>
	public T SaveOrUpdate(T entity) {
		Check.Require(
				!(entity instanceof IHasAssignedId),
				"For better clarity and reliability, Entities with an assigned Id must call Save or Update");

		getSession().saveOrUpdate(entity);
		return entity;
	}

	// / <summary>
	// / Translates a domain layer lock mode into an NHibernate lock mode via
	// reflection. This is
	// / provided to facilitate developing the domain layer without a direct
	// dependency on the
	// / NHibernate assembly.
	// / </summary>
	protected static LockMode ConvertFrom(Enums.LockMode lockMode) {
		LockMode translatedLockMode = LockMode.valueOf(lockMode.toString());

		Check.Ensure(
				translatedLockMode != null,
				"The provided lock mode , '"
						+ lockMode
						+ ",' "
						+ "could not be translated into an NHibernate.LockMode. This is probably because "
						+ "NHibernate was updated and now has different lock modes which are out of synch "
						+ "with the lock modes maintained in the domain layer.");

		return translatedLockMode;
	}

	protected static LockOptions LockOptionsFrom(Enums.LockMode lockMode) {
		LockOptions translatedLockoptions = new LockOptions(
				ConvertFrom(lockMode));

		Check.Ensure(
				translatedLockoptions != null,
				"The provided lock mode , '"
						+ lockMode
						+ ",' "
						+ "could not be translated into an NHibernate.LockMode. This is probably because "
						+ "NHibernate was updated and now has different lock modes which are out of synch "
						+ "with the lock modes maintained in the domain layer.");

		return translatedLockoptions;
	}
}
