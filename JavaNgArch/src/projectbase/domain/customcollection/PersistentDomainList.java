package projectbase.domain.customcollection;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.collection.internal.PersistentBag;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.internal.SessionImpl;
import org.hibernate.transform.Transformers;

import projectbase.domain.BaseDomainObject;
import projectbase.sharparch.domain.designbycontract.Check;
import projectbase.sharparch.hibernate.HibernateSession;
import projectbase.sharparch.hibernate.SessionFactoryKeyHelper;
import projectbase.utils.HqlCriterion;
import projectbase.utils.Pager;
import projectbase.utils.QuerySelectorBuilder;

/// <summary>
/// this class is for hibernate to perform persistency on an collection of type IDomainList[[T]]
/// I try to make the collection load as few data as possible.view and dbempty and dbcount won't trigger fully loading,but others do.
/// </summary>
/// <typeparam name="T"></typeparam>
public class PersistentDomainList<T> extends PersistentBag implements
		IDomainList<T> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static Logger log = LogManager.getLogger("DomainList");

	public PersistentDomainList(SessionImplementor session, List<T> coll) {
		super(session, coll);
	}

	public PersistentDomainList(SessionImplementor session) {
		super(session);
	}

	public List<T> View(String filter) {
		Check.Require(filter != null && !filter.isEmpty(),
				"filter should not be null or empty String");

		return View(filter, "");
	}

	public List<T> View(String filter, String sort) {
		if (filter != null && !filter.isEmpty())
			filter = "where " + filter;

		if (sort != null && !sort.isEmpty())
			sort = " order by " + sort;

		Query q = ((SessionImpl) GetSession())
				.createFilter(this, filter + sort);
		List<T> viewlist = (List<T>)q.list();
		return viewlist;
	}
	public <TDto> List<TDto> View(Class<T> entityClass, Class<TDto> dtoClass) {
		return View(entityClass,dtoClass,null,null);
	}

	public <TDto> List<TDto> View(Class<T> entityClass, Class<TDto> dtoClass, String filter) {
		return View(entityClass,dtoClass,filter,null);
	}
	public <TDto>List<TDto> View(Class<T> entityClass,Class<TDto> dtoClass,String filter, String sort) {
		if (filter != null && !filter.isEmpty())
			filter = "where " + filter;
		else
			filter="";
		if (sort != null && !sort.isEmpty())
			sort = " order by " + sort;
		else
			sort="";
		String selector="select "+QuerySelectorBuilder.BuildSelector(entityClass,dtoClass,null);
		
		Query q = ((SessionImpl) GetSession())
				.createFilter(this, selector+" "+filter + sort)
				.setResultTransformer(Transformers.aliasToBean(dtoClass));
		List<TDto> viewlist = (List<TDto>)q.list();
		return viewlist;
	}

	public List<T> View(Pager pager) {
		return View(pager, (String)null, null);
	}

	public List<T> View(Pager pager, String filter) {
		return View(pager, filter, null);
	}

	public List<T> View(Pager pager, String filter, String sort) {
		Check.Require(pager != null, "pager may not be null!");

		if (filter != null && !filter.isEmpty())
			filter = "where " + filter;
		
		if (sort != null && !sort.isEmpty())
			sort = " order by " + sort;
		
		long c = (long) ((SessionImpl) GetSession()).createFilter(this,
				"select count(*) " + filter).uniqueResult();

		pager.setItemCount((int) c);

		Query q = ((SessionImpl) GetSession())
				.createFilter(this, filter + sort);
		q.setFirstResult(pager.getFromRowIndex());
		q.setMaxResults(pager.getPageSize());
		List<T> viewlist = (List<T>)q.list();

		return viewlist;
	}
	public <TDto> List<TDto> View(Pager pager, Class<T> entityClass, Class<TDto> dtoClass) {
		return View(pager,entityClass,dtoClass,null,null);
	}

	public <TDto> List<TDto> View(Pager pager, Class<T> entityClass, Class<TDto> dtoClass, String filter) {
		return View(pager,entityClass,dtoClass,filter,null);
	}
	public <TDto>List<TDto> View(Pager pager, Class<T> entityClass,Class<TDto> dtoClass,String filter, String sort) {
		Check.Require(pager != null, "pager may not be null!");

		if (filter != null && !filter.isEmpty())
			filter = "where " + filter;
		else
			filter="";
		if (sort != null && !sort.isEmpty())
			sort = " order by " + sort;
		else
			sort="";

		long c = (long) ((SessionImpl) GetSession()).createFilter(this,
				"select count(*) " + filter).uniqueResult();

		pager.setItemCount((int) c);
		String selector="select "+QuerySelectorBuilder.BuildSelector(entityClass,dtoClass,null);
		
		Query q = ((SessionImpl) GetSession())
				.createFilter(this, selector+" "+filter + sort)
				.setResultTransformer(Transformers.aliasToBean(dtoClass));
		q.setFirstResult(pager.getFromRowIndex());
		q.setMaxResults(pager.getPageSize());
		List<TDto> viewlist = (List<TDto>)q.list();

		return viewlist;
	}
    private SessionImplementor GetSession() {//为本子类中的集合驱动数据库操作打开session（如view方法，但this.Count等需父对象关联session）

            if(super.getSession()!=null&&!super.getSession().isClosed())
            {
                return super.getSession();
            }
            String factoryKey = SessionFactoryKeyHelper.GetKeyFrom(this);
            SessionImplementor session=(SessionImplementor)HibernateSession.CurrentFor(factoryKey);
            setCurrentSession(session);
            return session;
    }
	@SuppressWarnings("unchecked")
	public List<T> Page(Pager pager) {
		Check.Require(pager != null, "pager may not be null!");

		pager.setItemCount(this.size());

		ArrayList<T> pageList = new ArrayList<T>();
		int i = 0;
		Iterator<?> etor = this.iterator();
		while (etor.hasNext() && i <= pager.getToRowIndex()) {
			Object item=etor.next();
			if (i >= pager.getFromRowIndex()) {
				pageList.add((T) item);
			}
			i++;
		}
		return pageList;
	}

	@SuppressWarnings("unchecked")
	public T GetElementById(int id) {
		T found = null;
		for (Object element : this) {
			if ((int) GetIdentifier(element) != id)
				continue;
			found = (T) element;
			break;
		}
		return found;
	}

	protected Object GetIdentifier(Object element) {
		Check.Require(element instanceof BaseDomainObject,
				"element must be of type BaseDomainObject to call this method!");
		return ((BaseDomainObject) element).getId();
	}

	public void Remove(int id) {
		T item = GetElementById(id);
		remove(item);
	}

	public boolean DbEmpty() {

		long c = (long) ((SessionImpl) GetSession()).createFilter(this,
				"select count(*) ").uniqueResult();
		return c == 0;

	}

	public int DbCount() {

		long c = (long) ((SessionImpl) GetSession()).createFilter(this,
				"select count(*) ").uniqueResult();
		return (int) c;

	}

	public boolean Add(T item) {
		if (item != null && !contains(item)) {
			return super.add(item);
		}
		return false;
	}

	public boolean Remove(T item) {
		if (contains(item)) {
			return super.remove(item);
		}
		return false;
	}

}
