package io.wonderland.base;

public final class CharUtils {

  private CharUtils() {
  }


  /**
   * Get optimized byte array of char value, big-endian ordering.
   *
   * @param c value
   * @return big endian optimized byte array
   */
  public static byte[] getOptimalBytesBE(char c) {
    if (c >= (1 << 8)) {
      byte[] array = new byte[2];
      array[0] = (byte) (c >> 8);
      array[1] = (byte) c;
      return array;
    } else {
      return new byte[]{(byte) c};
    }
  }


  /**
   * Get byte array of char value, big-endian ordering.
   *
   * @param c value
   * @return big endian byte array
   */
  public static byte[] getBytesBE(char c) {
    return new byte[]{(byte) (c >> 8), (byte) c};
  }

}
