package io.wonderland.alice.charset;

import io.wonderland.alice.exception.CharsetException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public final class CharsetsUtils {

  public static final int UTF_16_CHARS_ENCODED = 1_112_064;
  private static final int UTF_8_CHARS_ENCODED = 1_112_064;
  private static final int US_ASCII_CHARS_ENCODED = 128;
  private static final int ISO_8859_1_CHARS_ENCODED = 256;

  private CharsetsUtils() {
  }

  public static int getDefaultAlphabetSize() {
    Charset charset = Charset.defaultCharset();
    if (charset.equals(StandardCharsets.UTF_8)) {
      return UTF_8_CHARS_ENCODED;
    } else if (charset.equals(StandardCharsets.US_ASCII)) {
      return US_ASCII_CHARS_ENCODED;
    } else if (charset.equals(StandardCharsets.ISO_8859_1)) {
      return ISO_8859_1_CHARS_ENCODED;
    } else if (charset.equals(StandardCharsets.UTF_16)) {
      return UTF_16_CHARS_ENCODED;
    } else {
      throw new CharsetException("Unknown encoded characters in charset");
    }
  }

}
