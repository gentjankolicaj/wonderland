package io.wonderland.commons;

import java.lang.reflect.Method;

public class ReflectionUtils {

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


}
