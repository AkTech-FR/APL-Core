package me.xtrm.atlaspluginloader.core.load;

import me.ihaq.eventmanager.EventManager;
import me.xtrm.atlaspluginloader.api.load.ILoadController;
import me.xtrm.atlaspluginloader.api.load.plugin.IPluginManager;
import me.xtrm.atlaspluginloader.core.AtlasPluginLoader;
import me.xtrm.atlaspluginloader.core.load.plugin.PluginManager;
import me.xtrm.atlaspluginloader.core.load.plugin.exception.PluginLoadingException;
import me.xtrm.atlaspluginloader.core.load.plugin.exception.PluginManagerException;

public class LoadController implements ILoadController {
	
	private EventManager eventManager;
	private IPluginManager pluginManager;
	
	public LoadController() {
		pluginManager = new PluginManager();
		eventManager = new EventManager();
	}
	
	public void initialize() {
		try {
			pluginManager.loadPlugins();
		} catch(PluginLoadingException | PluginManagerException e) {
			AtlasPluginLoader.getLogger().fatal("Exception occured while loading plugins, exitting...");
			e.printStackTrace();
			System.exit(-1);
		}	
	}

	@Override
	public EventManager getEventManager() {
		return eventManager;
	}
	
	@Override
	public IPluginManager getPluginManager() {
		return pluginManager;
	}

}
