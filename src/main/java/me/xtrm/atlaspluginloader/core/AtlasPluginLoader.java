package me.xtrm.atlaspluginloader.core;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import me.xtrm.atlaspluginloader.api.load.ILoadController;
import me.xtrm.atlaspluginloader.api.setup.ISetupManager;
import me.xtrm.atlaspluginloader.core.utils.Consts;

public class AtlasPluginLoader {
	
	private static Logger logger;
	
	private ILoadController loadController;
	private File aplDataDir;
	
	private boolean initialized;
	
	public void setup(ISetupManager iSetupManager) {
		if(initialized) return;
		
		aplDataDir = iSetupManager.getDataFile();
		
		logger = LogManager.getLogger(Consts.NAME);
		logger.info("Setting up " + Consts.NAME + " v" + Consts.VER);
		
		loadController = iSetupManager.getLoadController();
	}
	
	public void initialize() {
		if(initialized) return;
		
		loadController.initialize();
		
		initialized = true;
	}
	
	public File getDataDir() {
		return aplDataDir;
	}
	
	public ILoadController getLoadController() {
		return loadController;
	}
	
	public static Logger getLogger() {
		return logger;
	}

}
