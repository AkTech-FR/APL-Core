package me.xtrm.atlasmodloader.core.load.plugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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

import me.xtrm.atlasmodloader.api.types.IPlugin;
import me.xtrm.atlasmodloader.api.types.PluginInfo;
import me.xtrm.atlasmodloader.core.AtlasPluginLoader;
import me.xtrm.atlasmodloader.core.load.plugin.exception.PluginLoadingException;

public class PluginManager {

	private final Logger logger;
	private final List<IPlugin> loadedPlugins;
	private PluginClassLoader classLoader;
	
	public PluginManager() {
		logger = LogManager.getLogger("PluginManager");
		loadedPlugins = new ArrayList<>();
	}
	
	public void loadPlugins() throws IOException, PluginLoadingException {
		Runtime.getRuntime().addShutdownHook(new Thread(this::onShutdown));
		
		List<File> plugins = findPlugins();
		if(plugins.isEmpty()) {
			logger.info("No plugins found, stopping DeltaAPI...");
			return;
		}
		logger.info("Found " + plugins.size() + " potential plugins...");
		
		Map<File, String> pluginMapClass = new HashMap<>();
		List<File> toRemove = new ArrayList<>();
		for(File f : plugins) {
			String mainClass = findPluginClass(f);
			if(mainClass != null) {
				if(mainClass.contains("$")) {
					throw new PluginLoadingException("Error while loading " + f.getName() + ": XDelta-Plugin Class cannot be a subclass");
				}
				pluginMapClass.put(f, mainClass.replace('/', '.'));
			}else {
				toRemove.add(f);
			}
		}
		toRemove.forEach(plugins::remove);
		toRemove.clear();
		
		logger.info("Identified " + plugins.size() + " plugins.");
		
		URL[] arrAYY = new URL[plugins.size()];
		for(int i = 0; i < plugins.size(); i++) {
			arrAYY[i] = plugins.get(i).toURI().toURL();
		}
		
		List<PluginInfo> pluginInfos = new ArrayList<PluginInfo>();
		
		if(System.getProperty("xdelta.plugin.load") != null) {
			String clazzName = System.getProperty("xdelta.plugin.load");
			Class<?> cliPluginClass = null;
			try {
				cliPluginClass = Class.forName(clazzName);
			} catch(ClassNotFoundException e) {
				throw new PluginLoadingException("Error while loading CLI-Plugin " + clazzName + ": Main Class not found!");
			}
			IPlugin idp = null;
		}
		
		classLoader = new PluginClassLoader(arrAYY, PluginManager.class.getClassLoader());
		for(File f : pluginMapClass.keySet()) {
			String mainClazz = pluginMapClass.get(f);
			Class<?> cla$$ = null;
			try {
				cla$$ = classLoader.loadClass(mainClazz);
			} catch(ClassNotFoundException e) {
				throw new PluginLoadingException("Error while loading " + f.getName() + ": Main Class " + mainClazz + " not found!");
			}
			IPlugin idp = null;
			try {
				idp = (IPlugin)cla$$.getConstructor().newInstance();
			} catch (ReflectiveOperationException e) {
				throw new PluginLoadingException("Error while loading " + f.getName() + ": Main Class doesn't implement IDeltaPlugin!");
			}
			if(idp != null) {
				if(cla$$.isAnnotationPresent(PluginInfo.class)) {
					PluginInfo info = cla$$.getDeclaredAnnotation(PluginInfo.class);
					for(PluginInfo pi : pluginInfos) {
						if(pi.name().equalsIgnoreCase(info.name())) {
							throw new PluginLoadingException("Error while loading " + f.getName() + ": Plugin " + pi.name() + " already has that name!");
						}
					}
					logger.info("Loaded plugin " + info.name() + " version " + info.version() + " by " + info.author());
					pluginInfos.add(info);
					loadedPlugins.add(idp);
				}else {
					throw new PluginLoadingException("Error while loading " + f.getName() + ": Main Class doesn't have PluginInfo annotation");
				}
			}else {
				throw new Error("This shouldn't be happening...? fuck me");
			}
		}
	}
	
	private void onShutdown() {
		loadedPlugins.forEach(IPlugin::onShutdown);
	}
	
	private List<File> findPlugins() throws IOException {
		logger.info("Searching for plugins...");
		
		List<File> addons = new ArrayList<>();
		
		File fe = new File(AtlasPluginLoader.getDataDir(), "plugins");
		if(!fe.exists()) fe.mkdirs();
		
		List<File> files = Arrays.asList(fe.listFiles());
		if(files.isEmpty()) return addons;
		
		files.stream().filter(f -> f.getName().endsWith(".jar")).filter(this::checkPlugin).forEach(addons::add);
		files.clear();
		
		return addons;
	}
	
	private boolean checkPlugin(File f) {
		return findPluginClass(f) != null;
	}
	
	private String findPluginClass(File f) {
		String cla$$ = null;
		try {
			JarInputStream jarStream = new JarInputStream(new FileInputStream(f));
			Manifest mf = jarStream.getManifest();
			Attributes mainAttribs = mf.getMainAttributes();
			if(mainAttribs.getValue("XDelta-Plugin") != null) {
				cla$$ = mainAttribs.getValue("XDelta-Plugin");
			}
			jarStream.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
		return cla$$;
	}
	
}
