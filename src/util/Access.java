package util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.HashMap;

public class Access {

	private static final HashMap<String, Object> map;
	static {
		map = new HashMap<>();
	}
	
	
	
	@GenericMethod
	public static <TYPE> void drop(TYPE value, String name) {
		map.put(name, value);
	}
	
	@GenericMethod
	public static <TYPE> void drop(TYPE value, Class<?> name) {
		drop(value, name.toString());
	}
	
	@SuppressWarnings("unchecked")
	@GenericMethod
	public static <TYPE> TYPE pull(String name) {
		if (!map.containsKey(name))
			throw new ClassCastException("There is no such key as: " + name);
		else {
			try {
				return (TYPE) map.get(name);
			} catch (ClassCastException cce) {
				System.err.println("Invalid cast!,");
				cce.printStackTrace();
			}
		}
		return null;
	}
	
	
	@Retention(RetentionPolicy.SOURCE)
	@Target(ElementType.METHOD)
	private @interface GenericMethod {}
}
