package projectbase.sharparch.hibernate;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.cfg.Configuration;

import projectbase.sharparch.domain.FileCache;
import projectbase.utils.ArgumentNullException;


    /// <summary>
    /// File cache implementation of INHibernateConfigurationCache.  Saves and loads a
    /// seralized version of <see cref="Configuration"/> to a temporary file location.
    /// </summary>
    /// <remarks>Seralizing a <see cref="Configuration"/> Object requires that all components
    /// that make up the Configuration Object be Serializable.  This includes any custom NHibernate 
    /// user types implementing <see cref="NHibernate.UserTypes.IUserType"/>.</remarks>
    public class HibernateConfigurationFileCache implements IHibernateConfigurationCache
    {
        /// <summary>
        /// List of files that the cached configuration is dependent on.  If any of these
        /// files are newer than the cache file then the cache file could be out of date.
        /// </summary>
        protected List<String> dependentFilePaths = new ArrayList<String>();


        /// <summary>
        /// Initializes new instance of the NHibernateConfigurationFileCache
        /// </summary>
        public HibernateConfigurationFileCache() { }

        /// <summary>
        /// Initializes new instance of the NHibernateConfigurationFileCache import the 
        /// given dependentFilePaths parameter.
        /// </summary>
        /// <param name="dependentFilePaths">LIst of files that the cached configuration
        /// is dependent upon.</param>
        public HibernateConfigurationFileCache(String[] dependentFilePaths) throws FileNotFoundException
        {
            AppendToDependentFilePaths(dependentFilePaths);
        }


        /// <summary>
        /// Load the <see cref="Configuration"/> Object from a cache.
        /// </summary>
        /// <param name="configKey">Key value to provide a unique name to the cached <see cref="Configuration"/>.</param>
        /// <param name="configPath">NHibernate configuration xml file.  This is used to determine if the 
        /// cached <see cref="Configuration"/> is out of date or not.</param>
        /// <returns>If an up to date cached Object is available, a <see cref="Configuration"/> 
        /// Object, otherwise null.</returns>
        public Configuration LoadConfiguration(String configKey, String configPath, String[] mappingAssemblies) throws IOException
        {
            String cachePath = CachedConfigPath(configKey);
            AppendToDependentFilePaths(mappingAssemblies);

            if (configPath != null)
            {
                AppendToDependentFilePaths(configPath);
            }

            if (IsCachedConfigCurrent(cachePath))
            {
                return FileCache.RetrieveFromCache(cachePath);
            }

            return null;
        }

        /// <summary>
        /// Save the <see cref="Configuration"/> Object to cache to a temporary file.
        /// </summary>
        /// <param name="configKey">Key value to provide a unique name to the cached <see cref="Configuration"/>.</param>
        /// <param name="config">Configuration Object to save.</param>
        public void SaveConfiguration(String configKey, Configuration config) throws IOException
        {
            String cachePath = CachedConfigPath(configKey);
            FileCache.StoreInCache(config, cachePath);
            Path p=Paths.get(cachePath);
            //Files.setLastModifiedTime(p, FileTime.from(GetMaxDependencyTime().toInstant()));
            Files.setLastModifiedTime(p, GetMaxDependencyTime());
        }



        /// <summary>
        /// Tests if an existing cached configuration file is out of date or not.
        /// </summary>
        /// <param name="cachePath">Location of the cached</param>
        /// <returns>False if the cached config file is out of date, otherwise true.</returns>
        /// <exception cref="ArgumentNullException">Thrown if the cachePath or configPath 
        /// parameters are null.</exception>
        protected boolean IsCachedConfigCurrent(String cachePath) throws IOException
        {
            if (cachePath==null || cachePath.isEmpty())
                throw new ArgumentNullException("cachePath");
            Path p=Paths.get(cachePath);
            //FileTime maxdependencytime=FileTime.from(GetMaxDependencyTime().toInstant())
            
            return (Files.exists(p) && (Files.getLastModifiedTime(p).compareTo(GetMaxDependencyTime())>=0));
        }

        /// <summary>
        /// Returns the latest file write time from the list of dependent file paths.
        /// </summary>
        /// <returns>Latest file write time, or '1/1/1980' if list is empty.</returns>
        protected FileTime GetMaxDependencyTime() throws IOException
        {
        	FileTime max=FileTime.fromMillis(0);
            if ((this.dependentFilePaths == null) || (this.dependentFilePaths.size() == 0))
            {
                return max;//DateFormat.getDateInstance().parse("1/1/1980");
            }

            for(String p : dependentFilePaths){
            	Path path=Paths.get(p);
            	FileTime ft1=Files.getLastModifiedTime(path);
            	if(ft1.compareTo(max)>0) max=ft1;
            }
 
            return max;
        }
  
        /// <summary>
        /// Provide a unique temporary file path/name for the cache file.
        /// </summary>
        /// <param name="configKey"></param>
        /// <returns>Full file path.</returns>
        /// <remarks>The hash value is intended to avoid the file from conflicting
        /// with other applications also import this cache feature.</remarks>
        protected String CachedConfigPath(String configKey)
        {
        	String fileName = String.format("{0}-{1}.bin", configKey, this.getClass().getPackage().hashCode());

            return Paths.get(System.getProperty("java.io.tmpdir"), fileName).toString();
        }



        /// <summary>
        /// Append the given file path to the dependentFilePaths list.
        /// </summary>
        /// <param name="paths">File path.</param>
        private void AppendToDependentFilePaths(String path) throws FileNotFoundException
        {
            this.dependentFilePaths.add(FindFile(path));
        }

        /// <summary>
        /// Append the given list of file paths to the dependentFilePaths list.
        /// </summary>
        /// <param name="paths"><see cref="IEnumerable{String}"/> list of file paths.</param>
        private void AppendToDependentFilePaths(Iterable<String> paths) throws FileNotFoundException
        {
            for (String path : paths)
            {
                this.dependentFilePaths.add(FindFile(path));
            }
        }
        /// <summary>
        /// Append the given list of file paths to the dependentFilePaths list.
        /// </summary>
        /// <param name="paths"><see cref="IEnumerable{String}"/> list of file paths.</param>
        private void AppendToDependentFilePaths(String[] paths) throws FileNotFoundException
        {
            for (String path : paths)
            {
                this.dependentFilePaths.add(FindFile(path));
            }
        }
        /// <summary>
        /// Tests if the file or assembly name exists either in the application's bin folder
        /// or elsewhere.
        /// </summary>
        /// <param name="path">Path or file name to test for existance.</param>
        /// <returns>Full path of the file.</returns>
        /// <remarks>If the path parameter does not end with ".dll" it is appended and 
        /// tested if the dll file exists.</remarks>
        /// <exception cref="FileNotFoundException">Thrown if the file is not found.</exception>
        private String FindFile(String path) throws FileNotFoundException
        {
            if (Files.exists(Paths.get(path),LinkOption.NOFOLLOW_LINKS))
            {
                return path;
            }

            String codeLocation=System.getProperty("java.class.path");
            Path codePath = Paths.get(codeLocation, path);
            if (Files.exists(codePath,LinkOption.NOFOLLOW_LINKS))
            {
                return codePath.toString();
            }

            String dllPath = (path.indexOf(".dll") == -1) ? path.trim() + ".dll"  :  path.trim();
            if (Files.exists(Paths.get(dllPath),LinkOption.NOFOLLOW_LINKS))
            {
                return dllPath;
            }

            Path codeDllPath = Paths.get(codeLocation, dllPath);
            if (Files.exists(codeDllPath,LinkOption.NOFOLLOW_LINKS))
            {
                return codeDllPath.toString();
            }

            throw new FileNotFoundException(path);
        }


    }
