

package projectbase.data;

import projectbase.domain.BaseDomainObjectWithTypedId;
import projectbase.domain.IGenericDao;

    public class BaseHibernateDao<T extends  BaseDomainObjectWithTypedId<Integer> >  extends  BaseHibernateDaoWithTypedId<T, Integer> implements IGenericDao<T>
    {
        public BaseHibernateDao(Class<T> entityClassName){
        	super(entityClassName);
        }
        public BaseHibernateDao(Class<T> entityClassName,String refTextFormula){
        	super(entityClassName,refTextFormula);
        }
    }

   

