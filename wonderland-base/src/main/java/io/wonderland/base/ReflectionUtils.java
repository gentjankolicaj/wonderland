package io.wonderland.base;


import static org.reflections.scanners.Scanners.SubTypes;

import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;

@Slf4j
public class ReflectionUtils {

  private static final Reflections CHARSET_REFLECTIONS = new Reflections("sun.nio.cs");

  private ReflectionUtils() {
  }


  public static Method getMethod(String name, Class c) {
    Method[] methods = c.getDeclaredMethods();
    for (Method method : methods) {
      if (method.getName().equals(name)) {
        return method;
      }
    }
    throw new UnsupportedOperationException("Method '" + name + "' not found at class " + c);
  }

  public static Set<Class<?>> getCharsetClasses() {
    return CHARSET_REFLECTIONS.get(SubTypes.of(Charset.class).asClass());
  }

  public static <T> Set<Class<? extends T>> getChildClasses(Class<T> t) {
    final Reflections reflections = new Reflections(t.getPackageName());
    return reflections.getSubTypesOf(t);
  }


  public static <T> T newInstance(Class<? extends T> clazz) {
    try {
      return clazz.getDeclaredConstructor().newInstance();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }


  public static <T> T newInstance(Class<? extends T> clazz, Class<?>[] constructorParamTypes,
      Object... constructorArgs) {
    try {
      return clazz.getConstructor(constructorParamTypes).newInstance(constructorArgs);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }


}
