package io.wonderland.base;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Objects;
import org.apache.commons.io.IOUtils;
import org.apache.commons.text.StringSubstitutor;
import org.apache.commons.text.lookup.StringLookupFactory;

public final class YamlConfigurations {

  private static final ObjectMapper OBJECT_MAPPER = YamlUtils.getObjectMapper();
  private static final StringSubstitutor STRING_SUBSTITUTOR = new StringSubstitutor(
      StringLookupFactory.INSTANCE.environmentVariableStringLookup());

  private YamlConfigurations() {
  }


  public static <T> T load(Class<T> clazz, String resource) throws ConfigurationException {
    try {
      URI uri = Objects.requireNonNull(clazz.getResource(resource)).toURI();
      Path resourcePath;
      if (uri.getScheme().equals("jar")) {
        //we need to create a new file system for files inside jar in order to read them.
        try (FileSystem fileSystem = FileSystems.newFileSystem(uri, Collections.emptyMap())) {
          resourcePath = fileSystem.getPath(resource);
        }
      } else {
        resourcePath = Paths.get(uri);
      }

      String contents = STRING_SUBSTITUTOR.replace(
          new String(IOUtils.toByteArray(resourcePath.toUri())));
      return OBJECT_MAPPER.readValue(contents, clazz);
    } catch (IOException | URISyntaxException e) {
      throw new ConfigurationException(e);
    }
  }

  public static <T> T load(Class<T> clazz, URL url) throws ConfigurationException {
    try {
      String contents = STRING_SUBSTITUTOR.replace(
          new String(Files.readAllBytes(Path.of(url.toURI()))));
      return OBJECT_MAPPER.readValue(contents, clazz);
    } catch (IOException | URISyntaxException e) {
      throw new ConfigurationException(e);
    }
  }


}
