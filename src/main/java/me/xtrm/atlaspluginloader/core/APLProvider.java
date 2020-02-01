package me.xtrm.atlaspluginloader.core;

public class APLProvider {
	
	private static AtlasPluginLoader primaryInstance;
	
	public static AtlasPluginLoader getPrimaryAPL() {
		if(primaryInstance == null) primaryInstance = new AtlasPluginLoader();
		return primaryInstance;
	}

}
