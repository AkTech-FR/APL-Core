package me.xtrm.atlaspluginloader.core.load;

import java.util.ArrayList;
import java.util.List;

import me.ihaq.eventmanager.EventManager;
import me.xtrm.atlaspluginloader.api.load.ILoadController;
import me.xtrm.atlaspluginloader.api.load.plugin.IPluginManager;
import me.xtrm.atlaspluginloader.api.load.transform.Transformer;
import me.xtrm.atlaspluginloader.api.types.IPlugin;
import me.xtrm.atlaspluginloader.core.AtlasPluginLoader;
import me.xtrm.atlaspluginloader.core.exception.plugin.PluginLoadingException;
import me.xtrm.atlaspluginloader.core.exception.plugin.PluginManagerException;
import me.xtrm.atlaspluginloader.core.load.plugin.PluginManager;

public class LoadController implements ILoadController {
	
	private EventManager eventManager;
	private IPluginManager pluginManager;
	
	private List<Transformer> internalTransformers;
	
	public LoadController() {
		this.pluginManager = new PluginManager();
		this.eventManager = new EventManager();
		
		this.internalTransformers = new ArrayList<>();
	}
	
	@Override
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
	public void registerInternalPlugin(IPlugin internalPlugin) {
		pluginManager.getLoadedPlugins().add(internalPlugin);
	}
	
	@Override
	public void registerInternalTransformer(Transformer internalTransformer) {
		internalTransformers.add(internalTransformer);
	}

	@Override
	public List<Transformer> getInternalTransformers() {
		return internalTransformers;
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
