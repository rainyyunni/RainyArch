package projectbase.desktop;

import java.util.Hashtable;
import java.util.Map;

import projectbase.bd.IApplicationStorage;
import projectbase.utils.NotImplementedException;

public class SimpleApplicationStorage implements IApplicationStorage{
    private final Map<String, Object> storage = new Hashtable<String, Object>();


    public Object Get(String key)
    {
    	return storage.get(key);
    }


    public void Set(String key, Object obj)
    {
        storage.put(key, obj);
    }
    public String GetInitParameter(String key){
    	throw new NotImplementedException();
    }
    public String GetRealPath(String relativePath){
    	throw new NotImplementedException();
    }
}
