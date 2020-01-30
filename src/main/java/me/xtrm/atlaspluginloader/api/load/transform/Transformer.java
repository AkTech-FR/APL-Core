package me.xtrm.atlaspluginloader.api.load.transform;

public abstract class Transformer {

	public abstract String getTransformerCodename();
	public abstract byte[] transform(String className, byte[] classData, boolean isSubclass);
	public abstract String[] getAffectedDomains();
	public abstract boolean isEnabled();

}
