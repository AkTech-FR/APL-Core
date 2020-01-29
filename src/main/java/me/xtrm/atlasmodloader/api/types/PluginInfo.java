package me.xtrm.atlasmodloader.api.types;

public @interface PluginInfo {
	
	/**
	 * The plugin's name
	 */
	String name();
	
	/**
	 * The plugin's version
	 */
	String version();
	
	/**
	 * The plugin's author
	 */
	String author();
	
}