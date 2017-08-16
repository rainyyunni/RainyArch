package projectbase.sharparch.domain;

import projectbase.practice.serviceLocation.ActivationException;
import projectbase.practice.serviceLocation.ServiceLocator;

    /// <summary>
    ///     This is a helper for accessing dependencies via the Common Service Locator (CSL).  But while
    ///     the CSL will throw Object reference errors if used before initialization, this will inform
    ///     you of what the problem is.  Perhaps it would be more aptly named "InformativeServiceLocator."
    /// </summary>
    public final class SafeServiceLocator
    {
        @SuppressWarnings("unchecked")
		public static <TDependency> TDependency GetService(Class<TDependency> classOfT)
        {
        	TDependency service;
            try
            {
            	service = (TDependency)ServiceLocator.getCurrent().GetService(classOfT);
            }
            catch (NullPointerException ex)
            {
                throw new NullPointerException(
                    "ServiceLocator has not been initialized; " + "I was trying to retrieve " + classOfT.getName());
            }
            catch (ActivationException ex)
            {
                throw new ActivationException(
                    "The needed dependency of type " + classOfT.getName() +
                    " could not be located with the ServiceLocator. You'll need to register it with " +
                    "the Common Service Locator (CSL) via your IoC's CSL adapter. " + ex.getMessage(), ex);
            }

            return service;
        }
    }
