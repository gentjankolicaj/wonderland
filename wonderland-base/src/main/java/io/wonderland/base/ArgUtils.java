package io.wonderland.base;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

public final class ArgUtils {


  /**
   * Helper method to build string argument from strings during runtime.
   *
   * @param args string args
   * @return string argument
   */
  public static String stringArg(String... args) {
    if (ArrayUtils.isEmpty(args)) {
      return StringUtils.EMPTY;
    } else {
      StringBuilder sb = new StringBuilder();
      for (String arg : args) {
        sb.append(arg);
      }
      return sb.toString();
    }
  }

  /**
   * Helper method to build string argument from strings during runtime.
   *
   * @param separator arg separator
   * @param args      string args
   * @return string argument
   */
  public static String stringArg(char separator, String... args) {
    if (ArrayUtils.isEmpty(args)) {
      return StringUtils.EMPTY;
    } else {
      StringBuilder sb = new StringBuilder();
      for (String arg : args) {
        sb.append(arg).append(separator);
      }
      return sb.toString();
    }
  }

}
