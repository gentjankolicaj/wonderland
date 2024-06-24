package io.wonderland.alice.math;

public final class GCD {

  private GCD() {
  }

  public static int gcdInt(int a, int b) {
    int tmp;
    while (b != 0) {
      tmp = b;
      b = a % b;
      a = tmp;
    }
    return a;
  }

  public static long gcdLong(long a, long b) {
    long tmp;
    while (b != 0) {
      tmp = b;
      b = a % b;
      a = tmp;
    }
    return a;
  }

}
