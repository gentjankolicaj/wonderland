package io.wonderland.base;

import io.wonderland.base.Map.KeyOption;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class FilesUtils {

  private FilesUtils() {
  }

  public static byte[] fileToByte(Path path) throws IOException {
    File tmpFile = path.toFile();
    byte[] buffer = new byte[(int) tmpFile.length()];
    int count;
    try (FileInputStream fis = new FileInputStream(tmpFile)) {
      count = fis.read(buffer);
    }
    if (count != tmpFile.length()) {
      throw new IOException("Failed to read all bytes.");
    }
    return buffer;
  }

  public static Optional<byte[]> optFileToByte(Path path) {
    try {
      File tmpFile = path.toFile();
      byte[] buffer = new byte[(int) tmpFile.length()];
      int count;
      try (FileInputStream fis = new FileInputStream(tmpFile)) {
        count = fis.read(buffer);
      }
      if (count != tmpFile.length()) {
        throw new IOException("Failed to read all bytes.");
      }
      return Optional.of(buffer);
    } catch (IOException ioe) {
      log.error(ioe.getMessage());
      return Optional.empty();
    }
  }

  public static <K, V> Map<K, V> fileToMap(String filePath, KeyOption keyOption,
      String pairSeparator,
      Function<String, K> keyMapper, Function<String, V> valueMapper) {
    HashMap<K, V> map = new HashMap<>();
    String line;
    try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
      while ((line = reader.readLine()) != null) {
        String[] keyValuePair = line.split(pairSeparator, 2);
        if (keyValuePair.length > 1) {
          K key = keyMapper.apply(keyValuePair[0]);
          V value = valueMapper.apply(keyValuePair[1]);
          if (KeyOption.OVERWRITE == keyOption) {
            map.put(key, value);
          } else if (KeyOption.DISCARD == keyOption) {
            map.putIfAbsent(key, value);
          }
        }
      }
    } catch (IOException e) {
      log.error(e.getMessage());
    }
    return map;
  }

  public static void writeFile(String filePath, byte[] bytes) {
    try {
      Files.write(Path.of(filePath), bytes);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static void writeFileAsync(String filePath, byte[] bytes) {
    CompletableFuture.runAsync(() -> {
      try {
        Files.write(Path.of(filePath), bytes);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    });
  }

}
