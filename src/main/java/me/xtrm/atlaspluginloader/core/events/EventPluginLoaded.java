package me.xtrm.atlaspluginloader.core.events;

import me.ihaq.eventmanager.Event;
import me.xtrm.atlaspluginloader.api.types.PluginInfo;

public class EventPluginLoaded extends Event {

	private PluginInfo pi;
	
	public EventPluginLoaded(PluginInfo pi) {
		this.pi = pi;
	}
	
	public PluginInfo getPluginInfo() {
		return pi;
	}

}
