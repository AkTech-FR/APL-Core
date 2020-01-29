package me.xtrm.atlasmodloader.core.load;

import java.io.IOException;

import me.xtrm.atlasmodloader.core.AtlasPluginLoader;
import me.xtrm.atlasmodloader.core.load.plugin.PluginManager;
import me.xtrm.atlasmodloader.core.load.plugin.exception.PluginLoadingException;

public class LoadController {
	
	private PluginManager pluginManager;
	
	public LoadController() {
		pluginManager = new PluginManager();
	}
	
	public void initialize() {
		try {
			pluginManager.loadPlugins();
		} catch(PluginLoadingException | IOException e) {
			AtlasPluginLoader.getLogger().fatal("Exception occured while loading plugins, exitting...");
			e.printStackTrace();
			System.exit(-1);
		}
		
		
	}

}
