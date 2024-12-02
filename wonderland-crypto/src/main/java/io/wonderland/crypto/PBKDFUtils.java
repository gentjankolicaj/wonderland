package io.wonderland.crypto;

import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.crypto.util.PBKDF2Config;
import org.bouncycastle.crypto.util.PBKDFConfig;
import org.bouncycastle.crypto.util.ScryptConfig;

public final class PBKDFUtils {


  private PBKDFUtils() {
  }

  /**
   * Get a PBKDF password-base-key-derivation-function config from scrypt.
   *
   * @param costParameter            cost parameter
   * @param blockSize                block size because PBKDF are base on block ciphers (PRP)
   * @param parallelizationParameter parallelization param
   * @param saltLength               salt length
   * @return config
   */
  public static PBKDFConfig createPbkdfConfigWithScrypt(int costParameter, int blockSize,
      int parallelizationParameter, int saltLength) {
    return new ScryptConfig.Builder(costParameter, blockSize,
        parallelizationParameter).withSaltLength(saltLength).build();
  }


  /**
   * Get a PBKDF password-base-key-derivation-function config 2.
   *
   * @param prf            pseudo random function
   * @param iterationCount iteration counter
   * @param saltLength     salt length
   * @return config
   */
  public static PBKDF2Config createPbkdf2Config(AlgorithmIdentifier prf, int iterationCount,
      int saltLength) {
    return new PBKDF2Config.Builder().withPRF(prf).withIterationCount(iterationCount)
        .withSaltLength(saltLength).build();
  }

}
