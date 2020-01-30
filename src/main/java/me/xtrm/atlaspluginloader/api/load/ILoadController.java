package me.xtrm.atlaspluginloader.api.load;

import me.ihaq.eventmanager.EventManager;
import me.xtrm.atlaspluginloader.api.load.plugin.IPluginManager;

public interface ILoadController {
	
	void initialize();
	
	EventManager getEventManager();
	IPluginManager getPluginManager();

}
