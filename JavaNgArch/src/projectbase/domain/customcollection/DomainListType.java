package projectbase.domain.customcollection;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.collection.spi.PersistentCollection;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.persister.collection.CollectionPersister;
import org.hibernate.usertype.UserCollectionType;
   /**
    * run as a proxy to provide and manipulate the real persistent list 
    * @author tudoubaby
    *
    * @param <T>
    */
    public class DomainListType<T>  implements  UserCollectionType
    {
        public PersistentCollection instantiate(SessionImplementor session, CollectionPersister persister)
        {
            return new PersistentDomainList<T>(session);
        }

        public PersistentCollection wrap(SessionImplementor session, Object collection)
        {
            return new PersistentDomainList<T>(session, (List<T>)collection);
        }

        public Iterator<?> getElementsIterator(Object collection)
        {
            return (Iterator<?>)((List<T>)collection).listIterator();
        }

        public boolean contains(Object collection, Object entity)
        {
            return ((List<T>)collection).contains((T)entity);
        }

        @SuppressWarnings("unchecked")
		public Object indexOf(Object collection, Object entity)
        {
            return ((List<T>)collection).indexOf((T)entity);
        }

        public Object replaceElements(Object original, Object target, CollectionPersister persister, Object owner,
                                      Map copyCache, SessionImplementor session)
        {
        	List<T> result = (List<T>)target;
            result.clear();
            for(T o : ((Iterable<T>)original))
            {
                result.add(o);
            }
            return result;
        }

        public Object instantiate(int anticipateCapacity)
        {
            return new ArrayList<T>();
        }
    }


