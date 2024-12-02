package io.wonderland.base;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

@SuppressWarnings("unused")
@Slf4j
public final class JSONUtils {

  private static final ObjectMapper OBJECT_MAPPER = createMapper();

  private JSONUtils() {
  }

  private static ObjectMapper createMapper() {
    return JsonMapper.builder(new JsonFactory())
        .addModule(new ParameterNamesModule())
        .addModule(new Jdk8Module())
        .addModule(new JavaTimeModule())
        .enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS)
        .build();
  }

  public static <T> T read(String file, Class<T> clazz) throws IOException {
    return OBJECT_MAPPER.readValue(
        Thread.currentThread().getContextClassLoader().getResource(file), clazz);
  }

  public static <T> T read(Path path, Class<T> clazz) throws IOException {
    return OBJECT_MAPPER.readValue(path.toFile(), clazz);
  }

  public static <T> T readFromNode(String file, String nodeKey, Class<T> clazz)
      throws IOException {
    JsonNode rootNode = OBJECT_MAPPER.readTree(
        Thread.currentThread().getContextClassLoader().getResource(file));
    return OBJECT_MAPPER.treeToValue(rootNode.get(nodeKey), clazz);
  }

  public static <T> T readFromNode(String file, String[] nodePaths, Class<T> clazz)
      throws IOException {
    JsonNode rootNode = OBJECT_MAPPER.readTree(
        Thread.currentThread().getContextClassLoader().getResource(file));
    if (ArrayUtils.isEmpty(nodePaths)) {
      throw new IllegalArgumentException("Node paths can't be empty.");
    }
    JsonNode node = rootNode.path(nodePaths[0]);
    for (int i = 1; i < nodePaths.length; i++) {
      node = node.path(nodePaths[i]);
    }
    return OBJECT_MAPPER.treeToValue(node, clazz);
  }

  public static void write(String file, Object obj) throws IOException {
    OBJECT_MAPPER.writeValue(new File(file), obj);
  }

  public static void write(Path path, Object obj) throws IOException {
    OBJECT_MAPPER.writeValue(path.toFile(), obj);
  }

  public static String writeValueAsString(Object obj) {
    try {
      return OBJECT_MAPPER.writeValueAsString(obj);
    } catch (JsonProcessingException e) {
      log.error("Error writing value as string ", e);
      return StringUtils.EMPTY;
    }
  }

  public static byte[] writeValueAsBytes(Object obj) throws JsonProcessingException {
    return OBJECT_MAPPER.writeValueAsBytes(obj);
  }

  public static <T> T readAsString(String content, Class<T> clazz)
      throws JsonProcessingException {
    return OBJECT_MAPPER.readValue(content, clazz);
  }

  public static <T> T readAsByte(byte[] src, Class<T> clazz) throws IOException {
    return OBJECT_MAPPER.readValue(src, clazz);
  }

}
