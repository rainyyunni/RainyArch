package projectbase.domain;

import java.util.List;

import projectbase.sharparch.hibernate.contracts.repositories.IHibernateRepositoryWithTypedId;
import projectbase.utils.HqlCriterion;
import projectbase.utils.Pager;

public interface IGenericDaoWithTypedId<T extends BaseDomainObjectWithTypedId<Integer>, TId>
		extends IHibernateRepositoryWithTypedId<T, TId> {
	Class<T> getEntityClass();
	
	int GetCountByQuery(String where);

	List<T> GetByQuery(String where);

	List<T> GetByQuery(String where, String sort);

	List<T> GetByQuery(Pager pager);

	List<T> GetByQuery(Pager pager, String where);

	List<T> GetByQuery(Pager pager, String where, String sort);

	T GetOneByQuery(String where);

	T GetOneByQuery(String where, boolean unique);

	T GetOneByQuery(String where, String sort);

	T GetOneByQuery(String where, String sort, boolean unique);
    DORef<T> GetRef(int id);
    List<DORef<T>> GetRefList();
    List<DORef<T>> GetRefList(String where, String sort);
	void Refresh(T entity);

	<TResult> List<TResult> GetProjectionByQuery(Pager pager,
			Class<TResult> resultClass, String selector, String where,
			String sort);

	<TResult> List<TResult> GetProjectionByQuery(Class<TResult> resultClass,
			String selector, String where, String sort);
	
	
	
	//-------------------
	int GetCountByQuery(HqlCriterion where);

	List<T> GetByQuery(HqlCriterion where);

	List<T> GetByQuery(HqlCriterion where, String sort);

	List<T> GetByQuery(Pager pager, HqlCriterion where);

	List<T> GetByQuery(Pager pager, HqlCriterion where, String sort);

	T GetOneByQuery(HqlCriterion where);

	T GetOneByQuery(HqlCriterion where, boolean unique);

	T GetOneByQuery(HqlCriterion where, String sort);

	T GetOneByQuery(HqlCriterion where, String sort, boolean unique);
	List<DORef<T>> GetRefList(HqlCriterion where, String sort);
	<TResult> List<TResult> GetProjectionByQuery(Pager pager,
			Class<TResult> resultClass, String selector, HqlCriterion where,
			String sort);

	<TResult> List<TResult> GetProjectionByQuery(Class<TResult> resultClass,
			String selector, HqlCriterion where, String sort);
	<TResult> List<TResult> GetProjectionByQuery(Pager pager,Class<TResult> resultClass,String selector,HqlCriterion where, String sort,String countSelector);
}
