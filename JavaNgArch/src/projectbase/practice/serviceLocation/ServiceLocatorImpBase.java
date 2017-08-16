package projectbase.practice.serviceLocation;

/// <summary>
/// This class is a helper that provides a default implementation
/// for most of the methods of <see cref="IServiceLocator"/>.
/// </summary>
public abstract class ServiceLocatorImpBase implements IServiceLocator
{
    /// <summary>
    /// Implementation of <see cref="IServiceProvider.GetService"/>.
    /// </summary>
    /// <param name="serviceType">The requested service.</param>
    /// <exception cref="ActivationException">if there is an error in resolving the service instance.</exception>
    /// <returns>The requested object.</returns>
    public Object GetService(Class<?> serviceType) 
    {
        return GetInstance(serviceType, null);
    }

    /// <summary>
    /// Get an instance of the given <paramref name="serviceType"/>.
    /// </summary>
    /// <param name="serviceType">Type of object requested.</param>
    /// <exception cref="ActivationException">if there is an error resolving
    /// the service instance.</exception>
    /// <returns>The requested service instance.</returns>
    public Object GetInstance(Class<?> serviceType)
    {
        return GetInstance(serviceType, null);
    }

    /// <summary>
    /// Get an instance of the given named <paramref name="serviceType"/>.
    /// </summary>
    /// <param name="serviceType">Type of object requested.</param>
    /// <param name="key">Name the object was registered with.</param>
    /// <exception cref="ActivationException">if there is an error resolving
    /// the service instance.</exception>
    /// <returns>The requested service instance.</returns>
    public Object GetInstance(Class<?> serviceType, String key) 
    {
        try
        {
            return DoGetInstance(serviceType, key);
        }
        catch (Exception ex)
        {
            throw new ActivationException(
                FormatActivationExceptionMessage(ex, serviceType, key),
                ex);
        }
    }

    /// <summary>
    /// Get all instances of the given <paramref name="serviceType"/> currently
    /// registered in the container.
    /// </summary>
    /// <param name="serviceType">Type of object requested.</param>
    /// <exception cref="ActivationException">if there is are errors resolving
    /// the service instance.</exception>
    /// <returns>A sequence of instances of the requested <paramref name="serviceType"/>.</returns>
    public Iterable<Object> GetAllInstances(Class<?> serviceType) 
    {
        try
        {
            return DoGetAllInstances(serviceType);
        }
        catch (Exception ex)
        {
            throw new ActivationException(
                FormatActivateAllExceptionMessage(ex, serviceType),
                ex);
        }
    }

    /// <summary>
    /// Get an instance of the given <typeparamref name="TService"/>.
    /// </summary>
    /// <typeparam name="TService">Type of object requested.</typeparam>
    /// <exception cref="ActivationException">if there is are errors resolving
    /// the service instance.</exception>
    /// <returns>The requested service instance.</returns>
//    public <TService> TService GetInstance() 
//    {
//        return (TService)GetInstance(classOfT.getClass(), null);
//    }

    /// <summary>
    /// Get an instance of the given named <typeparamref name="TService"/>.
    /// </summary>
    /// <typeparam name="TService">Type of object requested.</typeparam>
    /// <param name="key">Name the object was registered with.</param>
    /// <exception cref="ActivationException">if there is are errors resolving
    /// the service instance.</exception>
    /// <returns>The requested service instance.</returns>
//    public <TService> TService GetInstance(String key)
//    {
//    	Class<TService> classOfT=null;
//        return (TService)GetInstance(classOfT.getClass(), key);
//    }

    /// <summary>
    /// Get all instances of the given <typeparamref name="TService"/> currently
    /// registered in the container.
    /// </summary>
    /// <typeparam name="TService">Type of object requested.</typeparam>
    /// <exception cref="ActivationException">if there is are errors resolving
    /// the service instance.</exception>
    /// <returns>A sequence of instances of the requested <typeparamref name="TService"/>.</returns>
//    public <TService> Iterable<TService> GetAllInstances() 
//    {
//    	Class<TService> classOfT=null;
//        return (Iterable<TService>)GetAllInstances(classOfT.getClass());
//    }

    /// <summary>
    /// When implemented by inheriting classes, this method will do the actual work of resolving
    /// the requested service instance.
    /// </summary>
    /// <param name="serviceType">Type of instance requested.</param>
    /// <param name="key">Name of registered service you want. May be null.</param>
    /// <returns>The requested service instance.</returns>
    protected abstract Object DoGetInstance(Class<?> serviceType, String key);

    /// <summary>
    /// When implemented by inheriting classes, this method will do the actual work of
    /// resolving all the requested service instances.
    /// </summary>
    /// <param name="serviceType">Type of service requested.</param>
    /// <returns>Sequence of service instance objects.</returns>
    protected abstract Iterable<Object> DoGetAllInstances(Class<?> serviceType);

    /// <summary>
    /// Format the exception message for use in an <see cref="ActivationException"/>
    /// that occurs while resolving a single service.
    /// </summary>
    /// <param name="actualException">The actual exception thrown by the implementation.</param>
    /// <param name="serviceType">Type of service requested.</param>
    /// <param name="key">Name requested.</param>
    /// <returns>The formatted exception message string.</returns>
    protected String FormatActivationExceptionMessage(Exception actualException, Class<?> serviceType, String key)
    {
        return "need localization:Resources.ActivationExceptionMessage";//String.Format(CultureInfo.CurrentUICulture, Resources.ActivationExceptionMessage, serviceType.Name, key);
    }

    /// <summary>
    /// Format the exception message for use in an <see cref="ActivationException"/>
    /// that occurs while resolving multiple service instances.
    /// </summary>
    /// <param name="actualException">The actual exception thrown by the implementation.</param>
    /// <param name="serviceType">Type of service requested.</param>
    /// <returns>The formatted exception message string.</returns>
    protected String FormatActivateAllExceptionMessage(Exception actualException, Class<?> serviceType)
    {
        return "need localization:Resources.ActivationExceptionMessage";//string.Format(CultureInfo.CurrentUICulture, Resources.ActivateAllExceptionMessage, serviceType.Name);
    }
}
