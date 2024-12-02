package io.wonderland.rh.utils;

import java.io.File;
import java.io.OutputStream;
import java.security.Key;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

@Slf4j
public final class KeyUtils {

  private KeyUtils() {
  }

  public static void exportKey(Key key, String pathname) {
    try (OutputStream os = FileUtils.openOutputStream(new File(pathname))) {
      os.write(key.getEncoded());
      os.flush();
    } catch (Exception e) {
      log.error(e.getMessage());
    }
  }

}
