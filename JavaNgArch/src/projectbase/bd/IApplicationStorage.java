package projectbase.bd;

public interface IApplicationStorage {

    Object Get(String key);
    void Set(String key, Object obj);
    String GetInitParameter(String key);
    String GetRealPath(String relativePath);

}
