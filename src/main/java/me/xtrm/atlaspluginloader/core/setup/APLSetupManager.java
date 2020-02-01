package me.xtrm.atlaspluginloader.core.setup;

import me.xtrm.atlaspluginloader.api.load.ILoadController;
import me.xtrm.atlaspluginloader.api.setup.ISetupManager;
import me.xtrm.atlaspluginloader.core.load.LoadController;

public abstract class APLSetupManager implements ISetupManager {

	@Override
	public ILoadController getLoadController() {
		return new LoadController();
	}

}
