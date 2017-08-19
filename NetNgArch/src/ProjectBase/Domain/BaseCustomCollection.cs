using System;
using System.Collections;
using System.Collections.Generic;
using System.Linq.Expressions;
using NHibernate.Collection;
using NHibernate.Collection.Generic;
using NHibernate.Engine;
using NHibernate.Persister.Collection;
using NHibernate.Transform;
using NHibernate.UserTypes;
using SharpArch.Domain;
using SharpArch.NHibernate;
using log4net;
using ProjectBase.Utils;
using NHibernate.Impl;
namespace ProjectBase.Domain
{
    public interface IDomainCollection<T>
    {
        //void Owner();
        //void Sort();
        IList<T> View(string filter);
        IList<T> View(string filter, string sort);
        IList<TDto> View<TDto>(string filter="", string sort="");
        IList<T> View(Pager pager);
        IList<T> View(Pager pager, string filter);
        IList<T> View(Pager pager, string filter,string sort);
        IList<TDto> View<TDto>(Pager pager, string filter="", string sort="");
        //全部加载后分页
        IList<T> Page(Pager pager);
        //void Add();
        //void Remove();
        T GetElementById(int id);
        void Remove(int id);
        bool Empty{get;}
        bool DbEmpty { get; }
        int DbCount { get; }
    }
    /// <summary>
    /// domainlist interface used by hibernate clients
    /// </summary>
    public interface IDomainList<T> : IList<T>, IDomainCollection<T>
    {
    }
    /// <summary>
    /// this class is for initializing a transient collection by user code,
    /// and after hibernate takes over it,the PersistenDomainList will be swapped in.
    /// so calling it's methods related to persistency will fail because only the PersistenDomainList provides  persistency.
    /// </summary>
    /// <typeparam name="T"></typeparam>
    public class DomainList<T> : List<T>, IDomainList<T>
    {
        #region IDomainCollection<T> Members

        public IDomainList<T> SetFilter(string filter)
        {
            throw new TransientUserCollectionException();
        }

        public IList<T> View(string filter)
        {
            throw new TransientUserCollectionException();
        }

        public IList<T> View(string filter, string sort)
        {
            throw new TransientUserCollectionException();
        }
        public IList<TDto> View<TDto>(string filter, string sort)
        {
            throw new TransientUserCollectionException();
        }
        public IList<T> View(Pager pager)
        {
            throw new TransientUserCollectionException();
        }
        public IList<T> View(Pager pager, string filter)
        {
            throw new TransientUserCollectionException();
        }

        public IList<T> View(Pager pager, string filter,string sort)
        {
            throw new TransientUserCollectionException();
        }
        public IList<TDto> View<TDto>(Pager pager, string filter, string sort)
        {
            throw new TransientUserCollectionException();
        }
        public IList<T> Page(Pager pager)
        {
            throw new TransientUserCollectionException();
        }
        public T GetElementById(int id)
        {
            throw new TransientUserCollectionException();
        }

        public void Remove(int id)
        {
            throw new TransientUserCollectionException();
        }
        public bool Empty
        {
            get
            {
                throw new TransientUserCollectionException();
            }
        }
        public bool DbEmpty
        {
            get
            {
                throw new TransientUserCollectionException();
            }
        }
        public int DbCount
        {
            get
            {
                throw new TransientUserCollectionException();
            }
        }
        #endregion
    }

    /// <summary>
    /// this class is for hibernate to perform persistency on an collection of type IDomainList[[T]]
    /// I try to make the collection load as few data as possible.view and dbempty and dbcount won't trigger fully loading,but others do.
    /// </summary>
    /// <typeparam name="T"></typeparam>
    public class PersistenDomainList<T> : PersistentGenericBag<T>, IDomainList<T>
    {
        public static ILog log = LogManager.GetLogger(typeof(DomainList<T>));

        public PersistenDomainList(ISessionImplementor session, IList<T> coll)
            : base(session, coll) { }
        public PersistenDomainList(ISessionImplementor session)
            : base(session) { }

        protected override ISessionImplementor Session
        {//为本子类中的集合驱动数据库操作打开session（如view方法，但this.Count等需父对象关联session）
            get
            {
                if(base.Session!=null)
                {
                    return base.Session;
                }
                string factoryKey = SessionFactoryKeyHelper.GetKeyFrom(this);
                var session=(ISessionImplementor)NHibernateSession.CurrentFor(factoryKey);
                SetCurrentSession(session);
                return session;
            }
        }
        #region IDomainCollection<T> Members

        public IList<T> View(string filter)
        {
            Check.Require(!String.IsNullOrEmpty(filter),"filter should not be null or empty string");

            return View(filter,"");
        }
        public IList<T> View(string filter,string sort)
        {
            if (!string.IsNullOrEmpty(filter))
                filter = "where " + filter;

            if (!string.IsNullOrEmpty(sort))
                sort = " order by " + sort;

            var q = ((SessionImpl)(Session)).CreateFilter(this, filter+sort);
            var viewlist = q.List<T>();
            return viewlist;
        }
        public IList<TDto> View<TDto>(string filter, string sort)
        {
            if (!string.IsNullOrEmpty(filter))
                filter = "where " + filter;

            if (!string.IsNullOrEmpty(sort))
                sort = " order by " + sort;
            String selector = "select " + QuerySelectorBuilder.BuildSelector<T, TDto>();
            var q = ((SessionImpl)(Session)).CreateFilter(this, selector+" "+filter + sort);
            var viewlist = q.SetResultTransformer(Transformers.AliasToBean<TDto>()).List<TDto>();
            return viewlist;
        }
        public IList<T> View(Pager pager)
        {
            return View(pager, "", "");
        }
        public IList<T> View(Pager pager, string filter)
        {
            return View(pager, filter, "");
        }
        public IList<T> View(Pager pager, string filter, string sort)
        {
            Check.Require(pager != null, "pager may not be null!");

            if (!string.IsNullOrEmpty(filter))
                filter = "where " + filter;

            if (!string.IsNullOrEmpty(sort))
                sort = " order by " + sort;

            long c=(long)((SessionImpl)Session).CreateFilter(this, "select count(*) " + filter).UniqueResult();

            pager.ItemCount = (int)c;

            var q = ((SessionImpl)(Session)).CreateFilter(this, filter + sort);
            q.SetFirstResult(pager.FromRowIndex);
            q.SetMaxResults(pager.PageSize);
            var viewlist = q.List<T>();
     
            return viewlist;
        }
        public IList<TDto> View<TDto>(Pager pager, string filter, string sort)
        {
            Check.Require(pager != null, "pager may not be null!");

            if (!string.IsNullOrEmpty(filter))
                filter = "where " + filter;

            if (!string.IsNullOrEmpty(sort))
                sort = " order by " + sort;

            long c = (long)((SessionImpl)Session).CreateFilter(this, "select count(*) " + filter).UniqueResult();

            pager.ItemCount = (int)c;
            String selector = "select " + QuerySelectorBuilder.BuildSelector<T,TDto>();
            var q = ((SessionImpl)(Session)).CreateFilter(this, selector+" "+filter + sort);
            q.SetFirstResult(pager.FromRowIndex);
            q.SetMaxResults(pager.PageSize);
            var viewlist = q.SetResultTransformer(Transformers.AliasToBean<TDto>()).List<TDto>();

            return viewlist;
        }
        public IList<T> Page(Pager pager)
        {
            Check.Require(pager != null, "pager may not be null!");

            pager.ItemCount = this.Count;

            var pageList = new List<T>();
            int i = 0;
            var etor = this.GetEnumerator();
            while (etor.MoveNext()&&i<= pager.ToRowIndex)
            {
                if(i >= pager.FromRowIndex)
                {
                    pageList.Add((T)etor.Current);
                }
                i++;
            }
            return pageList;
        }
        public T GetElementById(int id)
        {
            var found = default(T);
            foreach (T element in this)
            {
                if ((int)GetIdentifier(element) != id) continue;
                found = element;
                break;
            }
            return found;
        }

        protected object GetIdentifier(object element)
        {
            Check.Require(element is BaseDomainObject, "element must be of type BaseDomainObject to call this method!");
            return ((BaseDomainObject)element).Id;
        }

        public void Remove(int id)
        {
            var item = GetElementById(id);
            Remove(item);
        }
        public bool DbEmpty
        {
            get
            {
                var c = (long)((SessionImpl)(Session)).CreateFilter(this, "select count(*) ").UniqueResult();
                return c == 0;
            }
        }
        public int DbCount 
        {
            get
            {
                var c = (long)((SessionImpl)(Session)).CreateFilter(this, "select count(*) ").UniqueResult();
                return (int)c;
            }
        }
        #endregion

        #region IList Members

        public void Add(T item)
        {
            if (item != null && !Contains(item))
            {
                base.Add(item);
            }
        }
        public void Remove(T item)
        {
            if (Contains(item))
            {
                base.Remove(item);
            }
        }
        #endregion
    }
    /// <summary>
    /// run as a proxy to provide and manipulate the real persistent list 
    /// </summary>
    /// <typeparam name="T"></typeparam>
    public class DomainListType<T> : IUserCollectionType
    {
        public IPersistentCollection Instantiate(ISessionImplementor session, ICollectionPersister persister)
        {
            return new PersistenDomainList<T>(session);
        }

        public IPersistentCollection Wrap(ISessionImplementor session, object collection)
        {
            return new PersistenDomainList<T>(session, (IList<T>)collection);
        }

        public IEnumerable GetElements(object collection)
        {
            return (IEnumerable)collection;
        }

        public bool Contains(object collection, object entity)
        {
            return ((IList<T>)collection).Contains((T)entity);
        }

        public object IndexOf(object collection, object entity)
        {
            return ((IList<T>)collection).IndexOf((T)entity);
        }

        public object ReplaceElements(object original, object target, ICollectionPersister persister, object owner,
                                      IDictionary copyCache, ISessionImplementor session)
        {
            var result = (IList<T>)target;
            result.Clear();
            foreach (T o in ((IEnumerable<T>)original))
            {
                result.Add(o);
            }
            return result;
        }

        public object Instantiate(int anticipateCapacity)
        {
            return new List<T>();
        }
    }

    class TransientUserCollectionException : Exception
    {
        public TransientUserCollectionException() : base("A Transient User Collection shouldn't call this method!") { }
        public TransientUserCollectionException(string msg): base(msg) {}
    }
}

