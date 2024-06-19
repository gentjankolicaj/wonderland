package io.wonderland.base;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;

public final class IntUtils {

  private IntUtils() {
  }

  /**
   * Parse a byte array to int.
   * <pre>
   *   Ex:
   *   IntUtils.parseInt(new byte[]{1,2}) => 12
   *   IntUtils.parseInt(new byte[]{12}) => 12
   *   IntUtils.parseInt(new byte[]{1,2,3} => 123
   *   IntUtils.parseInt(new byte[]{12,3} => 123
   *    IntUtils.parseInt(new byte[]{1,23} => 123
   * </pre>
   *
   * @param array to be parsed
   * @return int
   */
  public static int parseInt(byte[] array) {
    if (ArrayUtils.isEmpty(array) || array.length >= 5) {
      throw new IllegalArgumentException("Byte array invalid, array length l must be : 0 < l <=4");
    }
    int len = array.length;
    if (len == 1) {
      return array[0];
    } else if (len == 2) {
      return (array[0] * 10) + array[1];
    } else if (len == 3) {
      return (array[0] * 100) + (array[1] * 10) + array[2];
    } else {
      return (array[0] * 1000) + (array[1] * 100) + (array[2] * 10) + array[3];
    }
  }

  public static List<Integer> parseIntList(String... strings) {
    if (ArrayUtils.isEmpty(strings)) {
      throw new IllegalArgumentException("Can't parse null/empty array of strings");
    }
    return Arrays.stream(strings).map(Integer::parseInt).collect(Collectors.toList());
  }


  /**
   * Get int from byte array Big-Endian
   * <pre>
   *   value= a * 2^24 + b*2^16 + c * 2^8 + d*2^0
   *   Ex:
   *   IntUtils.getIntBE(new byte[]{1,2}) => 1*2^8 + 2 * 2^0 = 258
   *   IntUtils.getIntBE(new byte[]{1,2,3} => 1*2^16 + 2*2^8 + 3*2^0 = 66051
   *   IntUtils.getIntBE(new byte[]{1,2,3,4} => 1*2^24 + 2*2^16 + 3*2^8 + 4*2^0 = 16909060
   *
   * </pre>
   *
   * @param array byte array big-endian
   * @return int value
   */
  public static int getIntBE(byte[] array) {
    if (ArrayUtils.isEmpty(array) || array.length >= 5) {
      throw new IllegalArgumentException("Byte array invalid, array length l must be : 0 < l <=4");
    }
    int len = array.length;
    if (len == 1) {
      return array[0];
    } else if (len == 2) {
      return (array[0] << 8) | array[1];
    } else if (len == 3) {
      return (array[0] << 16) | (array[1] << 8) | array[2];
    } else {
      return (array[0] << 24) | (array[1] << 16) | (array[2] << 8) | array[3];
    }
  }

  /**
   * Get optimized byte array of integer value, big-endian ordering.
   *
   * @param i value
   * @return big endian optimized byte array
   */
  public static byte[] getOptimalBytesBE(int i) {
    if (i >= (1 << 24)) {
      byte[] array = new byte[4];
      array[0] = (byte) (i >> 24);
      array[1] = (byte) (i >> 16);
      array[2] = (byte) (i >> 8);
      array[3] = (byte) i;
      return array;
    } else if (i >= (1 << 16)) {
      byte[] array = new byte[3];
      array[0] = (byte) (i >> 16);
      array[1] = (byte) (i >> 8);
      array[2] = (byte) i;
      return array;
    } else if (i >= (1 << 8)) {
      byte[] array = new byte[2];
      array[0] = (byte) (i >> 8);
      array[1] = (byte) i;
      return array;
    } else {
      return new byte[]{(byte) i};
    }
  }

  /**
   * Get byte array of integer value, big-endian ordering.
   *
   * @param i value
   * @return big endian byte array
   */
  public static byte[] getBytesBE(int i) {
    return new byte[]{(byte) (i >> 24), (byte) (i >> 16), (byte) (i >> 8), (byte) i};
  }

  /**
   * Get a list of integers from byte array, big-endian ordering. Total ints : value.length/4 ,
   * where each int is formed by more than 0 bytes & less than 4.
   *
   * @param value byte array
   * @return list of integers
   */
  public static List<Integer> getIntListBE(byte[] value) {
    List<Integer> ints = new LinkedList<>();
    int len = value.length;
    int q = len / 4;
    int r = len % 4;

    if (q != 0) {
      for (int i = 0, rounds = q * 4; i < rounds - 1; ) {
        int res =
            ((value[i] & 0xff) << 24) | ((value[++i] & 0xff) << 16) | ((value[++i] & 0xff) << 8) | (
                value[++i] & 0xff);
        ints.add(res);
      }
    }
    if (r != 0) {
      if (r == 1) {
        ints.add((int) value[len - 1]);
      } else if (r == 2) {
        int var = ((value[len - 2] & 0xff) << 8) | (value[len - 1] & 0xff);
        ints.add(var);
      } else {
        int var = ((value[len - 3] & 0xff) << 16) | ((value[len - 2] & 0xff) << 8) | (value[len - 1]
            & 0xff);
        ints.add(var);
      }
    }
    return ints;
  }

  public static int[] array(List<Integer> ints) {
    if (CollectionUtils.isEmpty(ints)) {
      throw new IllegalArgumentException("List is empty or null");
    }
    int[] arr = new int[ints.size()];
    for (int i = 0, len = ints.size(); i < len; i++) {
      arr[i] = ints.get(i);
    }
    return arr;
  }

  public static Integer[] arrayWrapped(List<Integer> ints) {
    if (CollectionUtils.isEmpty(ints)) {
      throw new IllegalArgumentException("List is empty or null");
    }
    Integer[] arr = new Integer[ints.size()];
    for (int i = 0, len = ints.size(); i < len; i++) {
      arr[i] = ints.get(i);
    }
    return arr;
  }


}
