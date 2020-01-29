package me.xtrm.atlaspluginloader.api.transform;

public interface ITransformerManager {
	
	void addTransformers();
	byte[] transform(String className, byte[] classData);

}
