package io.wonderland.base;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public final class YamlUtils {

  private static final ObjectMapper OBJECT_MAPPER = createMapper();

  private YamlUtils() {
  }


  private static ObjectMapper createMapper() {
    return YAMLMapper.builder(new YAMLFactory())
        .addModule(new ParameterNamesModule())
        .addModule(new Jdk8Module())
        .addModule(new JavaTimeModule())
        .enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS)
        .build();
  }

  public static ObjectMapper getObjectMapper() {
    return OBJECT_MAPPER;
  }

  public static <T> T read(Class<T> clazz, String pathname) throws IOException {
    return OBJECT_MAPPER.readValue(
        Thread.currentThread().getContextClassLoader().getResource(pathname), clazz);
  }

  public static <T> T readUnwrappedRoot(Class<T> clazz, String pathname) throws IOException {
    return createMapper().configure(DeserializationFeature.UNWRAP_ROOT_VALUE, true)
        .readValue(Thread.currentThread().getContextClassLoader().getResource(pathname), clazz);
  }

  public static <T> T readClasspath(Class<T> clazz, String pathname) throws IOException {
    URL url = clazz.getResource(pathname);
    if (url != null) {
      return OBJECT_MAPPER.readValue(url, clazz);
    } else {
      File f = new File(pathname);
      if (f.exists() && !f.isDirectory()) {
        return OBJECT_MAPPER.readValue(f, clazz);
      } else {
        throw new MalformedURLException("Failed to find file: " + pathname);
      }
    }
  }

}
