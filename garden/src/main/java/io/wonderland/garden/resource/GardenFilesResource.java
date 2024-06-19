package io.wonderland.garden.resource;


import io.atlassian.fugue.Pair;
import io.wonderland.base.ApplicationException;
import io.wonderland.base.Dao;
import io.wonderland.garden.domain.Grapheme;
import io.wonderland.garden.domain.LetterFreq;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public final class GardenFilesResource {

  private static final String LETTER_FREQ_RESOURCE_DIR = "letter_freq";

  private GardenFilesResource() {
  }

  public static void loadInRedis(Dao<LetterFreq, String> langDao,
      Dao<Grapheme, String> graphemeDao) {
    try {
      long startTime = System.nanoTime();
      Map<String, Map<String, Pair<Path, Function<Path, Optional<byte[]>>>>> langFreqMap = byteLetterFreqResource();

      LetterFreq letterFreq = new LetterFreq(null);
      List<Grapheme> graphemes = new ArrayList<>();

      for (Entry<String, Map<String, Pair<Path, Function<Path, Optional<byte[]>>>>> entry : langFreqMap.entrySet()) {
        letterFreq.setLangCode(entry.getKey());

        Map<String, byte[]> graphemeFreq = new LinkedHashMap<>();
        entry.getValue().forEach(
            (k, v) -> v.right().apply(v.left()).ifPresent(array -> graphemeFreq.put(k, array)));
        graphemes.add(new Grapheme(entry.getKey(), graphemeFreq));
      }
      langDao.save(letterFreq);
      graphemeDao.saveAll(graphemes.toArray(new Grapheme[0]));

      log.info("Cache : local grapheme freq build time [{}] millis.",
          Duration.ofNanos(System.nanoTime() - startTime).toMillis());
    } catch (Exception e) {
      throw new ApplicationException(e);
    }
  }

  static Map<String, Map<String, Pair<Path, Function<Path, Optional<byte[]>>>>> byteLetterFreqResource() {
    Map<String, List<Path>> resourcePaths = getLetterFreqResourcePaths();
    log.info("Resource paths size {}", resourcePaths);
    return resourcePaths.entrySet().stream().map(entry -> {
      Map<String, Pair<Path, Function<Path, Optional<byte[]>>>> gramMap = new HashMap<>();
      entry.getValue().forEach(graphemePath ->
          gramMap.put(getGraphemeType(graphemePath),
              new Pair<>(graphemePath, GardenFilesResource::getByteArray))
      );
      return new Pair<>(entry.getKey(), gramMap);
    }).collect(Collectors.toMap(Pair::left, Pair::right));
  }

  static String getGraphemeType(Path resourcePath) {
    if (Objects.isNull(resourcePath) || StringUtils.isEmpty(resourcePath.toString())) {
      return "" + System.currentTimeMillis();
    }
    String path = resourcePath.toString();
    char separator = File.separatorChar;
    int fileNameIndex = 0;
    for (int i = path.length() - 1; i >= 0; i--) {
      if (path.charAt(i) == separator) {
        fileNameIndex = i;
        break;
      }
    }
    String filename = path.substring(fileNameIndex + 1);
    int splitIndex = filename.length();
    if (filename.contains("_")) {
      splitIndex = filename.indexOf("_");
    }
    if (splitIndex == filename.length()) {
      splitIndex = filename.indexOf(".");
    }
    return filename.substring(0, splitIndex);
  }


  static Map<String, List<Path>> getLetterFreqResourcePaths() {
    FileSystem fileSystem = null;
    try {
      Map<String, List<Path>> paths = new HashMap<>();
      URI uri = Objects.requireNonNull(
              GardenFilesResource.class.getClassLoader().getResource(LETTER_FREQ_RESOURCE_DIR))
          .toURI();
      Path resourcePath;
      if (uri.getScheme().equals("jar")) {
        //we need to create a new file system for files inside jar in order to read them.
        fileSystem = FileSystems.newFileSystem(uri, Collections.emptyMap());
        resourcePath = fileSystem.getPath(LETTER_FREQ_RESOURCE_DIR);

      } else {
        resourcePath = Paths.get(uri);
      }
      try (Stream<Path> stream = Files.walk(resourcePath)) {
        stream.filter(Files::isRegularFile)
            .forEach(path -> paths.compute(getLangCode(path), (k, v) -> {
              if (CollectionUtils.isEmpty(v)) {
                List<Path> list = new ArrayList<>();
                list.add(path);
                return list;
              } else {
                v.add(path);
              }
              return v;
            }));
      }
      paths.remove(LETTER_FREQ_RESOURCE_DIR);
      return paths;
    } catch (IOException | URISyntaxException e) {
      log.error("", e);
    } finally {
      //I Close file system after I have finished reading file paths on jar.
      if (fileSystem != null && fileSystem.isOpen()) {
        try {
          fileSystem.close();
        } catch (IOException e) {
          e.getStackTrace();
        }
      }
    }
    return Map.of();
  }

  static String getLangCode(Path path) {
    char separator = File.separatorChar;
    String absPath = path.toString()
        .substring(
            path.toString().indexOf(LETTER_FREQ_RESOURCE_DIR) + LETTER_FREQ_RESOURCE_DIR.length()
                + 1);
    return absPath.substring(0, absPath.indexOf(separator));
  }


  static Optional<byte[]> getByteArray(Path resourcePath) {
    try {
      byte[] arr = IOUtils.toByteArray(resourcePath.toUri());
      return Optional.of(arr);
    } catch (Exception e) {
      log.error("", e);
    }
    return Optional.empty();
  }


}
