package io.wonderland.base;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class FilesUtilsTest {

  @Test
  void fileToByte() throws IOException, URISyntaxException {
    URI uri = Objects.requireNonNull(FilesUtils.class.getResource("/empty.txt")).toURI();
    File file = new File(uri);
    Assertions.assertThat(FilesUtils.fileToByte(file.toPath())).hasSizeGreaterThan(1);
  }
}