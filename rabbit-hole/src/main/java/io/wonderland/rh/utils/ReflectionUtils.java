package io.wonderland.rh.utils;

import static org.reflections.scanners.Scanners.SubTypes;

import java.nio.charset.Charset;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;

@Slf4j
public class ReflectionUtils {

  private static final Reflections CHARSET_REFLECTIONS=new Reflections("sun.nio.cs");

  private ReflectionUtils(){}

  public static Set<Class<?>> getCharsetClasses(){
    return CHARSET_REFLECTIONS.get(SubTypes.of(Charset.class).asClass());
  }

  public static <T> Set<Class<? extends T>> getChildClasses(Class<T> t) {
    final Reflections reflections = new Reflections(t.getPackageName());
    return reflections.getSubTypesOf(t);
  }


}
