package projectbase.practice.serviceLocation;

import org.springframework.context.ApplicationContext;

import projectbase.utils.NotImplementedException;


public class SpringServiceLocator extends ServiceLocatorImpBase{
	public static ApplicationContext _container;
	public SpringServiceLocator(ApplicationContext container){
		_container=container;
	}
	  /// <summary>
    /// When implemented by inheriting classes, this method will do the actual work of resolving
    /// the requested service instance.
    /// </summary>
    /// <param name="serviceType">Type of instance requested.</param>
    /// <param name="key">Name of registered service you want. May be null.</param>
    /// <returns>The requested service instance.</returns>
    protected Object DoGetInstance(Class<?> serviceType, String key) {

    	return key == null?_container.getBean(serviceType):_container.getBean(key,serviceType);
    	
    }

    /// <summary>
    /// When implemented by inheriting classes, this method will do the actual work of
    /// resolving all the requested service instances.
    /// </summary>
    /// <param name="serviceType">Type of service requested.</param>
    /// <returns>Sequence of service instance objects.</returns>
    protected Iterable<Object> DoGetAllInstances(Class<?> serviceType){
    	throw new  NotImplementedException();
    }
}
