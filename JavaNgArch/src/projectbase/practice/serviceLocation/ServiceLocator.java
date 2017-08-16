package projectbase.practice.serviceLocation;

import projectbase.utils.InvalidOperationException;


/// <summary>
/// This class provides the ambient container for this application. If your
/// framework defines such an ambient container, use ServiceLocator.Current
/// to get it.
/// </summary>
public final class ServiceLocator
{
    private static ServiceLocatorProvider currentProvider;

    /// <summary>
    /// The current ambient container.
    /// </summary>
    public static IServiceLocator getCurrent() 
    {

            if (!IsLocationProviderSet()) throw new InvalidOperationException("Resources.ServiceLocationProviderNotSetMessage");

            return currentProvider.getServiceLocatorProvider(); 

    }

    /// <summary>
    /// Set the delegate that is used to retrieve the current container.
    /// </summary>
    /// <param name="newProvider">Delegate that, when called, will return
    /// the current ambient container.</param>
    public static void SetLocatorProvider(ServiceLocatorProvider newProvider)
    {
        currentProvider = newProvider;
    }

    public static boolean IsLocationProviderSet()
    {
            return currentProvider != null;
    }
}
