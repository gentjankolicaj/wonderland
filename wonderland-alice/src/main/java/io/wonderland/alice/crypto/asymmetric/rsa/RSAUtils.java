package io.wonderland.alice.crypto.asymmetric.rsa;

import io.wonderland.alice.crypto.random.CommonRandom;
import io.wonderland.alice.math.GCD;
import java.math.BigInteger;

public class RSAUtils {


  /**
   * Finds a co-prime smaller than half of input.
   *
   * @param p number for which we are going to find co-prime.
   * @return co-prime to p
   */
  public static long randomCoprime(long p) {
    long e = 0;
    long origin = 4;
    long bound = p / 2;
    do {
      long candidate = CommonRandom.secLong(origin, bound);
      if (GCD.gcdLong(candidate, p) == 1) {
        e = candidate;
      }
    } while (e == 0);

    return e;
  }


  /**
   * Finds a co-prime smaller than half of input.
   *
   * @param p number for which we are going to find co-prime.
   * @return co-prime to p
   */
  public static BigInteger randCoprime(BigInteger p) {
    BigInteger origin = BigInteger.TEN;
    BigInteger bound = p.divide(BigInteger.TWO);
    BigInteger e = BigInteger.ZERO;
    do {
      BigInteger candidate = CommonRandom.secBigInteger(origin, bound);
      if (candidate.gcd(p).compareTo(BigInteger.ONE) == 0) {
        e = candidate;
      }
    } while (e.compareTo(BigInteger.ZERO) == 0);
    return e;
  }

  public static BigInteger qInv(BigInteger p, BigInteger q) {
    return q.modInverse(p);
  }

}
