package io.wonderland.alice.crypto.random;

import java.math.BigInteger;
import java.security.SecureRandom;

public class CommonRandom {

  private static final SecureRandom secureRandom = new SecureRandom();

  public static long secLong(long origin, long bound) {
    long rand;
    do {
      rand = secureRandom.nextInt((int) origin) + origin;
    } while (rand >= bound);
    return rand;
  }

  public static BigInteger secBigInteger(BigInteger origin, BigInteger bound) {
    BigInteger var;
    do {
      byte[] buffer = new byte[bound.toByteArray().length];
      secureRandom.nextBytes(buffer);
      var = new BigInteger(buffer);
    } while (var.compareTo(origin) < 0 || var.compareTo(bound) > 0);
    return var;
  }

}
