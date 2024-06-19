package io.wonderland.alice.crypto.cipher.asymmetric.rsa;

import io.wonderland.alice.crypto.random.CommonRandom;
import java.math.BigInteger;

public class RSAUtils {


  /**
   * Finds a co-prime smaller than half of input.
   *
   * @param p number for which we are going to find co-prime.
   * @return co-prime to p
   */
  public static BigInteger randomCoprime(long p) {
    BigInteger two = BigInteger.TWO;
    BigInteger bigP = BigInteger.valueOf(p);
    BigInteger e = BigInteger.ZERO;
    while (e.intValue() != 0) {
      BigInteger candidate = CommonRandom.secure(4, (int) p / 2);
      if (candidate.mod(two).intValue() != 0) {
        BigInteger gcd = candidate.gcd(bigP);
        if (gcd.intValue() == 1) {
          e = gcd;
          break;
        }
      }
    }
    return e;
  }

  public static long randPublicExponent(long phi) {
    return randomCoprime(phi).longValueExact();
  }

  public static BigInteger randPublicExponent(BigInteger phi) {
    throw new UnsupportedOperationException("Todo: to implement method.");
  }

}
