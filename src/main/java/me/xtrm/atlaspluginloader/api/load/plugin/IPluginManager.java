package me.xtrm.atlaspluginloader.api.load.plugin;

import java.io.File;
import java.util.List;

import me.xtrm.atlaspluginloader.api.types.IPlugin;
import me.xtrm.atlaspluginloader.core.load.plugin.PluginClassLoader;
import me.xtrm.atlaspluginloader.core.load.plugin.exception.PluginLoadingException;
import me.xtrm.atlaspluginloader.core.load.plugin.exception.PluginManagerException;

public interface IPluginManager {
	
	void loadPlugins() throws PluginLoadingException, PluginManagerException;
	
	boolean isPlugin(File f);
	String getPluginClass(File f);
	
	void setPluginClassLoader(Class<? extends PluginClassLoader> cla$$);
	
	List<IPlugin> getLoadedPlugins();

}
