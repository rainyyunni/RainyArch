package projectbase.mvc;

import javax.servlet.ServletContext;

import projectbase.bd.IApplicationStorage;

public class WebApplicationStorage implements IApplicationStorage{
	
    private ServletContext storage = null;

    public WebApplicationStorage(ServletContext storage)
    {
        this.storage = storage;
    }
    public Object Get(String key)
    {
        return storage.getAttribute(key);
    }


    public void Set(String key, Object obj)
    {
        storage.setAttribute(key, obj);
    }
    public String GetInitParameter(String key){
    	return storage.getInitParameter(key);
    }
    public String GetRealPath(String relativePath){
    	return storage.getRealPath(relativePath);
    }
}
