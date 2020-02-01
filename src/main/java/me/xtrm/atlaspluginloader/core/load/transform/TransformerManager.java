package me.xtrm.atlaspluginloader.core.load.transform;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import me.xtrm.atlaspluginloader.api.TransformerAPI;
import me.xtrm.atlaspluginloader.api.load.transform.ITransformerManager;
import me.xtrm.atlaspluginloader.api.load.transform.Transformer;
import me.xtrm.atlaspluginloader.core.APLProvider;
import me.xtrm.atlaspluginloader.core.exception.APLException;

public class TransformerManager implements ITransformerManager {

	private static TransformerManager instance;
	
	private final List<Transformer> loadedTransformers;
	private final List<String> restrictedDomains;
	private final Logger logger;
	
	public TransformerManager() {		
		instance = this;
		
		this.loadedTransformers = new ArrayList<>();
		this.restrictedDomains = Arrays.asList("me.xtrm.atlaspluginloader.core.load.", "me.xtrm.atlaspluginloader.api.load");
		
		this.logger = LogManager.getLogger("TransformerManager");
	}
	
	@Override
	public void addTransformers() {
		// Load Internal Transformers
		for(Transformer t : APLProvider.getPrimaryAPL().getLoadController().getInternalTransformers()) {
			this.loadedTransformers.add(t);
			logger.info("Loaded Internal Transformer: " + t.getName());
		}		
		
		// Load Plugin Transformers
		for(String transformer : TransformerAPI.getTransformers()) {
			Class<?> clazz = null;
			try {
				clazz = APLProvider.getPrimaryAPL().getLoadController().getPluginManager().getPluginClassLoader().loadClass(transformer);
			} catch(ReflectiveOperationException e) {
				throw new APLException("Couldn't load Transformer class " + transformer + ": " + e.getMessage());
			}
			Transformer t = null;
			try {
				t = (Transformer)clazz.newInstance();
			} catch(ReflectiveOperationException e) {
				throw new APLException("Couldn't load Transformer class " + transformer + ": Class does't extend " + Transformer.class.getSimpleName());
			}
			this.loadedTransformers.add(t);
			logger.info("Loaded Transformer: " + t.getName());
		}
		
		logger.info("Loaded " + this.loadedTransformers.size() + " transformers.");
	}
	
	@Override
	public byte[] transform(String className, byte[] classData) {
		for(String restricted : restrictedDomains) {
			if(className.startsWith(className)) {
				return classData;
			}
		}
		for(Transformer t : loadedTransformers) {
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

}
