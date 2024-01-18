package io.wonderland.alice.jca.util;

import java.security.AccessController;
import java.security.PrivilegedAction;

public class ClassUtil {

  public ClassUtil() {
  }

  public static Class loadClass(Class cls, final String clazz) {
    try {
      ClassLoader classLoader = cls.getClassLoader();
      Class loadedClass = classLoader != null ? classLoader.loadClass(clazz) : (Class) AccessController.doPrivileged(
          (PrivilegedAction) () -> {
            try {
              return Class.forName(clazz);
            } catch (Exception var2) {
              return null;
            }
          });

      return loadedClass;
    } catch (ClassNotFoundException var3) {
      return null;
    }
  }

}
