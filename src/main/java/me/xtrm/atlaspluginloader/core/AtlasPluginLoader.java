package me.xtrm.atlaspluginloader.core;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import me.xtrm.atlaspluginloader.api.load.ILoadController;
import me.xtrm.atlaspluginloader.core.load.LoadController;

public class AtlasPluginLoader {
	
	private static ILoadController loadController;
	private static Logger logger;
	private static File aplDataDir;
	
	private static boolean initialized;
	
	public static void setup(File dataDir) {
		if(initialized) return;
		
		aplDataDir = dataDir;
		
		logger = LogManager.getLogger(Consts.NAME);
		logger.info("Setting up " + Consts.NAME + " v" + Consts.VER);
		
		loadController = new LoadController();
	}
	
	public static void initialize() {
		if(initialized) return;
		
		loadController.initialize();
	}
	
	public static void finishInitialization() {
		if(initialized) return;
		
		initialized = true;
	}
	
	public static File getDataDir() {
		return aplDataDir;
	}
	
	public static ILoadController getLoadController() {
		return loadController;
	}
	
	public static Logger getLogger() {
		return logger;
	}

}
