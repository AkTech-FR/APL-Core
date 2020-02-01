package me.xtrm.atlaspluginloader.core.load.plugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.JarInputStream;
import java.util.jar.Manifest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import me.xtrm.atlaspluginloader.api.load.plugin.IPluginManager;
import me.xtrm.atlaspluginloader.api.types.IPlugin;
import me.xtrm.atlaspluginloader.api.types.PluginInfo;
import me.xtrm.atlaspluginloader.core.APLProvider;
import me.xtrm.atlaspluginloader.core.events.EventPluginLoaded;
import me.xtrm.atlaspluginloader.core.exception.plugin.PluginLoadingException;
import me.xtrm.atlaspluginloader.core.exception.plugin.PluginManagerException;

public class PluginManager implements IPluginManager {

	private final Logger logger;
	private final List<IPlugin> loadedPlugins;
	
	private Class<? extends PluginClassLoader> customClassLoader;
	private PluginClassLoader classLoader;
	
	public PluginManager() {
		this.logger = LogManager.getLogger("PluginManager");
		this.loadedPlugins = new ArrayList<>();
	}
	
	@Override
	public void loadPlugins() throws PluginManagerException, PluginLoadingException {		
		List<File> plugins = new ArrayList<>();
		
		// Find plugins jars
		try {
			plugins = findPlugins();
		} catch(IOException e) {
			throw new PluginManagerException("Error while searching for plugins: " + e.getMessage());
		}
		
		if(plugins.isEmpty()) {
			logger.info("No plugins found, stopping...");
			return;
		}
		logger.info("Found " + plugins.size() + " potential plugins...");
		
		// Add all main classes
		Map<File, String> pluginMapClass = new HashMap<>();
		List<File> toRemove = new ArrayList<>();
		for(File f : plugins) {
			String mainClass = getPluginClass(f);
			if(mainClass != null) {
				if(mainClass.contains("$")) {
					throw new PluginLoadingException("Error while loading " + f.getName() + ": APL-Plugin Class cannot be a subclass");
				}
				pluginMapClass.put(f, mainClass.replace('/', '.'));
			}else {
				toRemove.add(f);
			}
		}
		toRemove.forEach(plugins::remove);
		toRemove.clear();
		
		logger.info("Identified " + plugins.size() + " plugins.");
		
		// CLI/Dev Plugin
		if(System.getProperty("apl.plugin.load") != null) {
			String clazzName = System.getProperty("apl.plugin.load");
			Class<?> cliPluginClass = null;
			try {
				cliPluginClass = Class.forName(clazzName);
			} catch(ClassNotFoundException e) {
				throw new PluginLoadingException("Error while loading CLI-Plugin " + clazzName + ": Main Class not found!");
			}
			IPlugin idp = null;
			try {
				idp = (IPlugin)cliPluginClass.getConstructor().newInstance();
			} catch (ReflectiveOperationException e) {
				throw new PluginLoadingException("Error while loading CLI-Plugin " + clazzName + ": Main Class doesn't implement " + IPlugin.class.getSimpleName() + "!");
			}
		}
		
		// Setup ClassLoader
		URL[] arrAYY = new URL[plugins.size()];
		try {
			for(int i = 0; i < plugins.size(); i++) {
				arrAYY[i] = plugins.get(i).toURI().toURL();
			}
		} catch(IOException e) {
			throw new PluginManagerException("Error while initializing PluginManager: " + e.getMessage());
		}		
		if(customClassLoader != null) {
			classLoader = new PluginClassLoader(arrAYY, PluginManager.class.getClassLoader());
		}else {
			try {
				classLoader = customClassLoader.getDeclaredConstructor(URL[].class).newInstance(arrAYY);
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
				throw new PluginManagerException("Error while loading PluginClassLoader: " + e.getMessage());
			}
		}
		
		// Plugin Loading Stuff
		List<PluginInfo> pluginInfos = new ArrayList<PluginInfo>();
		for(File f : pluginMapClass.keySet()) {
			
			// Check main class
			String mainClazz = pluginMapClass.get(f);
			Class<?> cla$$ = null;
			try {
				cla$$ = classLoader.loadClass(mainClazz);
			} catch(ClassNotFoundException e) {
				throw new PluginLoadingException("Error while loading " + f.getName() + ": Main Class " + mainClazz + " not found on " + classLoader.getClass().getSimpleName() + "!");
			}
			
			PluginInfo info = null;
			if(cla$$.isAnnotationPresent(PluginInfo.class)) {
				info = cla$$.getDeclaredAnnotation(PluginInfo.class);
				for(PluginInfo pi : pluginInfos) {
					if(pi.name().equalsIgnoreCase(info.name())) {
						throw new PluginLoadingException("Error while loading " + f.getName() + ": Loaded Plugin " + pi.name() + " already has that name!");
					}
				}
				pluginInfos.add(info);
			}else {
				throw new PluginLoadingException("Error while loading " + f.getName() + ": Main Class doesn't have " + PluginInfo.class.getSimpleName() + " annotation");
			}
			
			if(!Arrays.asList(cla$$.getInterfaces()).contains(IPlugin.class)) {
				throw new PluginLoadingException("Error while loading " + f.getName() + ": Main Class doesn't implement " + IPlugin.class.getSimpleName() + "!");
			}
			
			IPlugin idp = null;
			try {
				idp = (IPlugin)cla$$.getConstructor().newInstance();
			} catch (ReflectiveOperationException e) {
				throw new PluginLoadingException("Error while loading " + f.getName() + ": Main Class doesn't implement " + IPlugin.class.getSimpleName() + "!");
			}
			
			// Add to loaded plugins
			if(idp != null) {
				loadedPlugins.add(idp);
				logger.info("Loaded plugin " + info.name() + " version " + info.version() + " by " + info.author());
				
				EventPluginLoaded e = new EventPluginLoaded(info);
				APLProvider.getPrimaryAPL().getLoadController().getEventManager().callEvent(e);
			}else {
				throw new PluginLoadingException("Critical Error, IDP is null for " + cla$$.getName() + " (has? " + Arrays.asList(cla$$.getInterfaces()).contains(IPlugin.class) + ") (This shouldn't be happening...? Report me to the devs quicc)");
			}
		}
	}
	
	private List<File> findPlugins() throws IOException {
		logger.info("Searching for plugins...");
		
		List<File> addons = new ArrayList<>();
		
		File fe = new File(APLProvider.getPrimaryAPL().getDataDir(), "plugins");
		if(!fe.exists()) fe.mkdirs();
		
		List<File> files = Arrays.asList(fe.listFiles());
		if(files.isEmpty()) return addons;
		
		files.stream().filter(f -> f.getName().endsWith(".jar")).filter(this::isPlugin).forEach(addons::add);
		files.clear();
		
		return addons;
	}

	@Override
	public boolean isPlugin(File f) {
		return getPluginClass(f) != null;
	}

	@Override
	public String getPluginClass(File f) {
		String cla$$ = null;
		try {
			JarInputStream jarStream = new JarInputStream(new FileInputStream(f));
			Manifest mf = jarStream.getManifest();
			Attributes mainAttribs = mf.getMainAttributes();
			if(mainAttribs.getValue("APL-Plugin") != null) {
				cla$$ = mainAttribs.getValue("APL-Plugin");
			}
			jarStream.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
		return cla$$;
	}

	@Override
	public PluginClassLoader getPluginClassLoader() {
		return classLoader;
	}
	
	@Override
	public void setPluginClassLoaderType(Class<? extends PluginClassLoader> cla$$) {
		this.customClassLoader = cla$$;
	}
	
	@Override
	public List<IPlugin> getLoadedPlugins() {
		return loadedPlugins;
	}
	
}
