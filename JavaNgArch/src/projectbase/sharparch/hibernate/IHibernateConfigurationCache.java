package projectbase.sharparch.hibernate;

import java.io.IOException;

import org.hibernate.cfg.Configuration;
/// <summary>
///     Interface for providing caching capability for an <see cref = "Configuration" /> Object.
/// </summary>

public interface IHibernateConfigurationCache {
	// / <summary>
	// / Load the <see cref = "Configuration" /> Object from a cache.
	// / </summary>
	// / <param name = "configKey">Key value to provide a unique name to the
	// cached <see cref = "Configuration" />.</param>
	// / <param name = "configPath">NHibernate configuration xml file. This is
	// used to determine if the
	// / cached <see cref = "Configuration" /> is out of date or not.</param>
	// / <param name="mappingAssemblies">List of assemblies containing HBM
	// files.</param>
	// / <returns>If an up to date cached Object is available, a <see cref =
	// "Configuration" /> Object, otherwise null.</returns>
	Configuration LoadConfiguration(String configKey, String configPath,
			String[] mappingAssemblies) throws IOException;

	// / <summary>
	// / Save the <see cref = "Configuration" /> Object to a cache.
	// / </summary>
	// / <param name = "configKey">Key value to provide a unique name to the
	// cached <see cref = "Configuration" />.</param>
	// / <param name = "config">Configuration Object to save.</param>
	void SaveConfiguration(String configKey, Configuration config) throws IOException;
}
