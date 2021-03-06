package me.xtrm.atlaspluginloader.core.utils.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;

/**
 * Handy class for Reflection Operations
 * 
 * @author Fox2Code
 */
public class ProxiedClass {
	
    private final Object object;

    public ProxiedClass(Object object) {
        this.object = object;
    }

    public Object getObject() {
        return this.isClass() ? null : this.object;
    }

    public Object getHandle() {
        return this.object;
    }

    public ProxiedClass get(String name) throws ReflectiveOperationException {
        return new ProxiedClass(get0(name));
    }

    public Object get0(String name) throws ReflectiveOperationException {
        return findField(name).get(this.getObject());
    }

    public void set(String name, Object value) throws ReflectiveOperationException {
        set0(name, value instanceof ProxiedClass ? ((ProxiedClass)value).getHandle() : value);
    }

    public void set0(String name, Object value) throws ReflectiveOperationException {
        findField(name).set(this.getObject(), value);
    }

    public ProxiedClass run(String name, Object... args) throws ReflectiveOperationException {
    	return new ProxiedClass(run0(name, args));
    }
    
    public Object run0(String name, Object... args) throws ReflectiveOperationException {
        return runAccessible(findMethod(name, args), args);
    }

    @Override
    public String toString() {
        return "ProxiedClass{"+ this.object + "}";
    }

    public Class getObjClass() {
        return this.object == null ? null : this.object instanceof Class ? (Class)this.object : this.object.getClass();
    }

    public String getClassName() {
        return this.object == null ? "null" : this.getObjClass().getName();
    }

    public ProxiedClass newInstance(Object... args) throws ReflectiveOperationException {
        for (int i = 0;i < args.length;i++) if (args[i] instanceof ProxiedClass) args[i] = ((ProxiedClass) args[i]).getObject();
        return new ProxiedClass(newInstance0(args));
    }

    public Object newInstance0(Object... args) throws ReflectiveOperationException {
        if (this.object instanceof Class<?>) {
            if (args.length == 0) {
                return runAccessible(((Class<?>) this.object).getDeclaredConstructor());
            } else {
                return runAccessible(((Class<?>) this.object).getDeclaredConstructors());
            }
        } else throw new IllegalAccessException("Unable to create an instance on a non static context!");
    }

    private Object runAccessible(Executable[] executables,Object... args) throws ReflectiveOperationException {
        global:
        for (Executable executable : executables) if (executable.getParameterCount() == args.length) {
            Parameter[] parameters = executable.getParameters();
            for (int i = 0;i < args.length;i++) if (args[i] != null) {
                if (!generify(parameters[i].getType()).isAssignableFrom(args[i].getClass())) continue global;
            }
            return runAccessible(executable, args);
        }
        throw new NoSuchMethodException("No compatible method found!");
    }

    private Field findField(String name) throws ReflectiveOperationException {
        Class<?> cl = this.getObjClass();
        while (cl != Object.class && cl != null) {
            for (Field field:cl.getDeclaredFields()) if (field.getName().equals(name) && Modifier.isStatic(field.getModifiers()) == this.isClass()) {
                if (!field.isAccessible()) field.setAccessible(true);
                return field;
            }
            cl = cl.getSuperclass();
        }
        throw new NoSuchMethodException("No field found!");
    }

    private Method findMethod(String name,Object... args) throws ReflectiveOperationException {
        Class<?> cl = this.getObjClass();
        while (cl != Object.class && cl != null) {
            global:
            for (Method method : cl.getDeclaredMethods()) if (method.getParameterCount() == args.length && method.getName().equals(name)) {
                Parameter[] parameters = method.getParameters();
                for (int i = 0; i < args.length; i++)
                    if (args[i] != null) {
                        if (!generify(parameters[i].getType()).isAssignableFrom(args[i].getClass()))
                            continue global;
                    }
                return method;
            }
            cl = cl.getSuperclass();
        }
        throw new NoSuchMethodException("No compatible method found!");
    }

    public Object runAccessible(Executable executable,Object... args) throws ReflectiveOperationException {
        if (!executable.isAccessible()) executable.setAccessible(true);
        if (executable instanceof Constructor) {
            return ((Constructor) executable).newInstance(args);
        }
        boolean isStatic;
        if ((isStatic = Modifier.isStatic(executable.getModifiers())) != this.isClass()) {
            if (isStatic) {
                throw new IllegalAccessException("Tried to run a static method on a non static context!");
            } else {
                throw new IllegalAccessException("Tried to run a non static method on a static context!");
            }
        }
        if (executable instanceof Method) {
            return ((Method) executable).invoke(this.getObject(),args);
        }
        throw new IllegalArgumentException("Invalid executable type! (Found: "+executable.getClass().getName()+")");
    }

    public String asString() {
        if (this.object instanceof String)
            return (String) this.object;
        throw new ClassCastException("Unable to cast " + this.getClassName() + " to String");
    }

    public int asInteger() {
        if (this.object instanceof Integer)
            return (Integer) this.object;
        throw new ClassCastException("Unable to cast " + this.getClassName() + " to Integer");
    }

    public boolean asBoolean() {
        if (this.object instanceof Boolean)
            return (Boolean) this.object;
        throw new ClassCastException("Unable to cast " + this.getClassName() + " to Boolean");
    }

    public boolean isClass() {
        return this.object instanceof Class;
    }

    private static Class<?> generify(Class cl) {
        if (!cl.isPrimitive()) return cl;
        if (cl == byte.class) return Byte.class;
        if (cl == short.class) return Short.class;
        if (cl == int.class) return Integer.class;
        if (cl == long.class) return Long.class;
        if (cl == float.class) return Float.class;
        if (cl == double.class) return Double.class;
        if (cl == boolean.class) return Boolean.class;
        if (cl == char.class) return Character.class;
        if (cl == void.class) return Void.class;
        throw new IllegalArgumentException("Invalid Primitive type!");
    }

    public static ProxiedClass forName(String name) throws ClassNotFoundException {
        return new ProxiedClass(Class.forName(name));
    }
}
