package projectbase.sharparch.hibernate;

import projectbase.sharparch.domain.SafeServiceLocator;
public final class SessionFactoryKeyHelper {
	public static String GetKey() 
    {
		ISessionFactoryKeyProvider provider = SafeServiceLocator.GetService(ISessionFactoryKeyProvider.class);
		return provider.GetKey();
    }

	public static String GetKeyFrom(Object anObject) 
    {
    	ISessionFactoryKeyProvider provider = SafeServiceLocator.GetService(ISessionFactoryKeyProvider.class);
    	if(provider==null)	
    		provider=new DefaultSessionFactoryKeyProvider();
    	return provider.GetKeyFrom(anObject);
    }
}
