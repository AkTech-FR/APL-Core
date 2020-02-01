package me.xtrm.atlaspluginloader.api.load;

import java.util.List;

import me.ihaq.eventmanager.EventManager;
import me.xtrm.atlaspluginloader.api.load.plugin.IPluginManager;
import me.xtrm.atlaspluginloader.api.load.transform.Transformer;
import me.xtrm.atlaspluginloader.api.types.IPlugin;

public interface ILoadController {
	
	void initialize();
	
	void registerInternalPlugin(IPlugin internalPlugin);
	void registerInternalTransformer(Transformer internalTransformer);
	
	List<Transformer> getInternalTransformers();
	
	EventManager getEventManager();
	IPluginManager getPluginManager();

}
