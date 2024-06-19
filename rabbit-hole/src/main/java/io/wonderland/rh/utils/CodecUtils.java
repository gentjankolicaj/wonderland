package io.wonderland.rh.utils;

import com.google.common.primitives.Bytes;
import io.wonderland.base.Arrays;
import io.wonderland.base.IntUtils;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

public final class CodecUtils {

  private CodecUtils() {
  }


  public static String encodeBase10(byte[] data, char separator) {
    return Arrays.getStringValueOf(data, separator);
  }

  public static byte[] decodeBase10(String data, char separator) {
    String[] codes = StringUtils.split(data.trim(), separator);
    List<Integer> list = new ArrayList<>();
    for (String code : codes) {
      if (StringUtils.isNotEmpty(code)) {
        list.add(Integer.parseInt(code));
      }
    }

    byte[] container = new byte[0];
    for (Integer i : list) {
      byte[] intBytes = IntUtils.getOptimalBytesBE(i);
      container = Bytes.concat(container, intBytes);
    }
    return container;
  }

  public static String encodeBaseRadix(int radix, byte[] data, char separator) {
    StringBuilder sb = new StringBuilder();
    for (byte b : data) {
      sb.append(Integer.toString(b, radix)).append(separator);
    }
    return sb.toString();
  }

  public static byte[] decodeBaseRadix(int radix, String data, char separator) {
    String[] codes = StringUtils.split(data.trim(), separator);
    List<Integer> list = new ArrayList<>();
    for (String code : codes) {
      if (StringUtils.isNotEmpty(code)) {
        list.add(Integer.parseInt(code, radix));
      }
    }
    byte[] container = new byte[0];
    for (Integer i : list) {
      byte[] intBytes = IntUtils.getOptimalBytesBE(i);
      container = Bytes.concat(container, intBytes);
    }
    return container;
  }


}
