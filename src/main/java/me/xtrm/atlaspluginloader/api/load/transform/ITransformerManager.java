package me.xtrm.atlaspluginloader.api.load.transform;

public interface ITransformerManager {
	
	void addTransformers();
	byte[] transform(String className, byte[] classData);

}
