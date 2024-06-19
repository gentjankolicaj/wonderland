package io.wonderland.crypto;

import java.security.AlgorithmParameterGenerator;
import java.security.AlgorithmParameters;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class AlgorithmParameterUtils {

  public static final String ALGORITHM_CAN_T_BE_EMPTY = "Algorithm can't be empty.";


  private AlgorithmParameterUtils() {
  }


  public static IvParameterSpec generateIvParameterSpec(String secureRandomAlgorithm,
      int vectorLength)
      throws GeneralSecurityException {
    if (StringUtils.isEmpty(secureRandomAlgorithm)) {
      throw new IllegalArgumentException(ALGORITHM_CAN_T_BE_EMPTY);
    }
    SecureRandom secureRandom = SecureRandom.getInstance(secureRandomAlgorithm);
    byte[] vector = new byte[vectorLength];
    secureRandom.nextBytes(vector);
    return new IvParameterSpec(vector);
  }

  /**
   * @param algorithm name
   * @return Algorithm parameter generator
   * @throws GeneralSecurityException exception
   */
  public static AlgorithmParameterGenerator getAlgorithmParamGen(String algorithm)
      throws GeneralSecurityException {
    if (StringUtils.isEmpty(algorithm)) {
      throw new IllegalArgumentException(ALGORITHM_CAN_T_BE_EMPTY);
    }
    return AlgorithmParameterGenerator.getInstance(algorithm, Constants.BC_CSP);
  }

  /**
   * @param algorithm name
   * @return Algorithm parameter generator
   * @throws GeneralSecurityException exception
   */
  public static AlgorithmParameterGenerator getAlgorithmParamGen(String algorithm, int keySize)
      throws GeneralSecurityException {
    if (StringUtils.isEmpty(algorithm)) {
      throw new IllegalArgumentException(ALGORITHM_CAN_T_BE_EMPTY);
    }
    AlgorithmParameterGenerator generator = AlgorithmParameterGenerator.getInstance(algorithm,
        Constants.BC_CSP);
    generator.init(keySize);
    return generator;
  }

  public static AlgorithmParameters generateAlgorithmParameters(String algorithm)
      throws GeneralSecurityException {
    return getAlgorithmParamGen(algorithm).generateParameters();
  }

  public static AlgorithmParameters generateAlgorithmParameters(String algorithm, int keySize)
      throws GeneralSecurityException {
    return getAlgorithmParamGen(algorithm, keySize).generateParameters();
  }

  public static <T extends AlgorithmParameterSpec> T generateAlgorithmParameterSpec(
      String algorithm,
      Class<T> paramSpec) throws GeneralSecurityException {
    return generateAlgorithmParameters(algorithm).getParameterSpec(paramSpec);
  }

  public static <T extends AlgorithmParameterSpec> T generateAlgorithmParameterSpec(
      String algorithm, int keySize,
      Class<T> paramSpec) throws GeneralSecurityException {
    return generateAlgorithmParameters(algorithm, keySize).getParameterSpec(paramSpec);
  }


}
