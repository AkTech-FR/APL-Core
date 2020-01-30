package me.xtrm.atlaspluginloader.core.load.plugin;

import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandlerFactory;

public class PluginClassLoader extends URLClassLoader {
	
	/** Cant call that from API */
	PluginClassLoader(URL[] urls, ClassLoader parent) {
		super(urls, parent);
	}
	
	/** Cant call that either */
	private PluginClassLoader(URL[] var1, ClassLoader var2, URLStreamHandlerFactory var3) { super(var1, var2, var3); }
	
	/** Call this instead, much better */
	public PluginClassLoader(URL[] urls) {
		this(urls, PluginClassLoader.class.getClassLoader());
	}
}
