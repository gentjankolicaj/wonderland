package io.wonderland.rh.utils;

import io.wonderland.base.Arrays;
import org.apache.commons.codec.binary.Base16;

public final class BaseUtils {

  private static final Base16 BASE_16_UPPERCASE = new Base16(false);
  private static final Base16 BASE_16_LOWERCASE = new Base16(true);


  private BaseUtils() {
  }

  public static String getBase16(boolean lowerCase, byte[] data) {
    if (lowerCase) {
      return BASE_16_LOWERCASE.encodeAsString(data);
    } else {
      return BASE_16_UPPERCASE.encodeAsString(data);
    }
  }

  public static String getBase10(byte[] data, char separator) {
    return Arrays.getStringValueOf(data, separator);
  }

  public static String getBase8(byte[] data, char separator) {
    StringBuilder sb = new StringBuilder();
    for (byte b : data) {
      sb.append(Integer.toOctalString(b)).append(separator);
    }
    return sb.toString();
  }

  public static String getBase2(byte[] data, char separator) {
    StringBuilder sb = new StringBuilder();
    for (byte b : data) {
      sb.append(Integer.toBinaryString(b)).append(separator);
    }
    return sb.toString();
  }

}
