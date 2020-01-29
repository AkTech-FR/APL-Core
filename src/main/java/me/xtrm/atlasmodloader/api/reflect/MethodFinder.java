package me.xtrm.atlasmodloader.api.reflect;

import java.lang.reflect.Method;

public class MethodFinder {
	
	public static String getMethod(Class<?> cla$$, String defaultName, Class<?> returnType, Class... args) {
		for(Method met : cla$$.getDeclaredMethods()) {
			if(met.getReturnType() == returnType) {
				if(met.getParameterTypes() == args) {
					return met.getName();
				}
			}
		}
		return defaultName;
	}

}
