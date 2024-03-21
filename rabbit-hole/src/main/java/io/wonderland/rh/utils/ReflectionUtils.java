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


}
