package io.wonderland.base;

public class Arrays {

  public static boolean[] clone(boolean[] data) {
    return null == data ? null : data.clone();
  }

  public static byte[] clone(byte[] data) {
    return null == data ? null : data.clone();
  }

  public static char[] clone(char[] data) {
    return null == data ? null : data.clone();
  }

  public static int[] clone(int[] data) {
    return null == data ? null : data.clone();
  }

  public static long[] clone(long[] data) {
    return null == data ? null : data.clone();
  }

  public static short[] clone(short[] data) {
    return null == data ? null : data.clone();
  }

  public static String getStringValueOf(byte[] data) {
    if (data == null || data.length == 0) {
      return null;
    } else {
      StringBuilder sb = new StringBuilder();
      for (byte b : data) {
        sb.append(b);
      }
      return sb.toString();
    }
  }

  public static String getStringValueOf(byte[] data, char separator) {
    if (data == null || data.length == 0) {
      return null;
    } else {
      StringBuilder sb = new StringBuilder();
      for (byte b : data) {
        sb.append(b).append(separator);
      }
      return sb.toString();
    }
  }

  @SafeVarargs
  public static <T> T[] asArray(T... values) {
    return values;
  }

}
