package io.wonderland.rh.utils;

import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.FileUtils;

public final class MyFileUtils {

  private MyFileUtils() {
  }

  public static InputStream getFile(String filePath) throws IOException {
    return FileUtils.openInputStream(
        FileUtils.getFile(
            Thread.currentThread().getContextClassLoader().getResource(filePath).getPath()));
  }

}
