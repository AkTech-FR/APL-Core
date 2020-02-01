package me.xtrm.atlaspluginloader.api.setup;

import java.io.File;

import me.xtrm.atlaspluginloader.api.load.ILoadController;

public interface ISetupManager {
	
	ILoadController getLoadController();
	File getDataFile();

}
