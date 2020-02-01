package me.xtrm.atlaspluginloader.core.exception.plugin;

@SuppressWarnings("serial")
public class PluginLoadingException extends RuntimeException {
	
	public PluginLoadingException() {
		super();
	}
	
	public PluginLoadingException(String str) {
		super(str);
	}
	
}
