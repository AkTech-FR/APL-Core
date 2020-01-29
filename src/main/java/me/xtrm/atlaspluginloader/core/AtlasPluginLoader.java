package me.xtrm.atlaspluginloader.core;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import me.xtrm.atlaspluginloader.core.load.LoadController;

public class AtlasPluginLoader {
	
	private static LoadController loadController;
	private static Logger logger;
	private static File aplDataDir;
	
	private static boolean initialized;
	
	public static void initialize(File dataDir) {
		if(initialized) return;
		
		aplDataDir = dataDir;
		
		logger = LogManager.getLogger(Consts.NAME);
		logger.info("Initializing " + Consts.NAME + " v" + Consts.VER);
		
		loadController = new LoadController();
		loadController.initialize();
	}
	
	public static void finishInitialization() {
		if(initialized) return;
		initialized = true;
	}
	
	public static File getDataDir() {
		return aplDataDir;
	}
	
	public static LoadController getLoadController() {
		return loadController;
	}
	
	public static Logger getLogger() {
		return logger;
	}

}
