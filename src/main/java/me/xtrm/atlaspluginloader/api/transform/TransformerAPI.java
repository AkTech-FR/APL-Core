package me.xtrm.atlaspluginloader.api.transform;

import java.util.ArrayList;
import java.util.List;

public class TransformerAPI {
	
	private static List<String> classTransformers = new ArrayList<>();
	
	public static void registerTransformer(String className) {
		classTransformers.add(className);
	}
	
	public static List<String> getTransformers() {
		return classTransformers;
	}

}
