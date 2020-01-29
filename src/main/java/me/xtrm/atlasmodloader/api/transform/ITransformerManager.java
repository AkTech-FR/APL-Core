package me.xtrm.atlasmodloader.api.transform;

public interface ITransformerManager {
	
	void addTransformers();
	byte[] transform(String className, byte[] classData);

}
