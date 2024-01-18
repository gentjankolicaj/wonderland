package io.wonderland.alice.crypto.cipher.asymmetric.rsa;

import io.jmathematics.algorithm.EuclideanAlgorithm;
import io.jmathematics.random.CommonRandom;

public class RSAUtils {

  /**
   * Finds a co-prime smaller than half of input.
   *
   * @param p number for which we are going to find co-prime.
   * @return co-prime to p
   */
  public static long randomCoprime(long p) {
    long e = 0;
    while (e != 0) {
      long candidate = CommonRandom.secure(4, (int) p / 2);
      if (candidate % 2 != 0) {
        long gcd = EuclideanAlgorithm.gcd(candidate, p);
        if (gcd == 1) {
          e = gcd;
          break;
        }
      }
    }
    return e;
  }

  public static long randPublicExponent(long phi) {
    return randomCoprime(phi);
  }

}
