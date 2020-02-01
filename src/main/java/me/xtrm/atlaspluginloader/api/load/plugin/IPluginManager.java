package me.xtrm.atlaspluginloader.api.load.plugin;

import java.io.File;
import java.util.List;

import me.xtrm.atlaspluginloader.api.types.IPlugin;
import me.xtrm.atlaspluginloader.core.exception.plugin.PluginLoadingException;
import me.xtrm.atlaspluginloader.core.exception.plugin.PluginManagerException;
import me.xtrm.atlaspluginloader.core.load.plugin.PluginClassLoader;

public interface IPluginManager {
	
	void loadPlugins() throws PluginLoadingException, PluginManagerException;
	
	boolean isPlugin(File f);
	String getPluginClass(File f);
	
	PluginClassLoader getPluginClassLoader();
	void setPluginClassLoaderType(Class<? extends PluginClassLoader> cla$$);
	
	List<IPlugin> getLoadedPlugins();

}
