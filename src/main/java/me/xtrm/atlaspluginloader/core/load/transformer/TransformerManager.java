package me.xtrm.atlaspluginloader.core.load.transformer;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import me.xtrm.atlaspluginloader.api.transform.ITransformerManager;
import me.xtrm.atlaspluginloader.api.transform.Transformer;
import me.xtrm.atlaspluginloader.api.transform.TransformerAPI;
import me.xtrm.atlaspluginloader.core.AtlasPluginLoader;
import me.xtrm.atlaspluginloader.core.load.plugin.PluginClassLoader;

public class TransformerManager implements ITransformerManager {

	private final List<Transformer> transformers;
	private final List<String> getRestrictedDomains;
	private final Logger logger;
	
	public TransformerManager() {		
		this.transformers = new ArrayList<>();
		this.getRestrictedDomains = new ArrayList<>();
		this.logger = LogManager.getLogger("TransformerManager");
	}
	
	@Override
	public byte[] transform(String className, byte[] classData) {		
		for(Transformer t : transformers) {
			if(t.isEnabled()) {
				for(String domain : t.getAffectedDomains()) {
					if(className.startsWith(domain)) {
						classData = t.transform(className, classData, className.contains("$"));
					}
				}
			}
		}
		
		return classData;
	}

	@Override
	public void addTransformers() {
		// Load Internal Transformers
		
		
		
		// Load Plugin Transformers
		
		
		logger.info("Loaded " + this.transformers.size() + " transformers.");
	}

}
