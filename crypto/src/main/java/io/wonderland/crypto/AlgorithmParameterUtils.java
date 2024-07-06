package io.wonderland.crypto;

import java.security.AlgorithmParameterGenerator;
import java.security.AlgorithmParameters;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AlgorithmParameterUtils {


  private AlgorithmParameterUtils() {
  }


  /**
   * @param provider              cryptographic service provider
   * @param secureRandomAlgorithm random algorithm
   * @param vectorSize            random vector size
   * @return
   * @throws GeneralSecurityException
   */
  public static IvParameterSpec generateIvParameterSpec(String provider,
      String secureRandomAlgorithm, int vectorSize) throws GeneralSecurityException {
    SecureRandom secureRandom = SecureRandom.getInstance(secureRandomAlgorithm, provider);
    byte[] vector = new byte[vectorSize];
    secureRandom.nextBytes(vector);
    return new IvParameterSpec(vector);
  }

  /**
   * @param provider cryptographic service provider
   * @param algorithm name
   * @return Algorithm parameter generator
   * @throws GeneralSecurityException exception
   */
  public static AlgorithmParameterGenerator getAlgorithmParamGen(String provider,String algorithm)
      throws GeneralSecurityException {
    return AlgorithmParameterGenerator.getInstance(algorithm, provider);
  }


  /**
   * @param provider cryptographic service provider
   * @param algorithm name
   * @return Algorithm parameter generator
   * @throws GeneralSecurityException exception
   */
  public static AlgorithmParameterGenerator getAlgorithmParamGen(String provider, String algorithm,
      int keySize) throws GeneralSecurityException {
    AlgorithmParameterGenerator generator = AlgorithmParameterGenerator.getInstance(algorithm,
        provider);
    generator.init(keySize);
    return generator;
  }

  public static AlgorithmParameters generateAlgorithmParameters(String provider,String algorithm)
      throws GeneralSecurityException {
    return getAlgorithmParamGen(provider, algorithm).generateParameters();
  }

  public static AlgorithmParameters generateAlgorithmParameters(String provider, String algorithm,
      int keySize) throws GeneralSecurityException {
    return getAlgorithmParamGen(provider, algorithm, keySize).generateParameters();
  }

  public static <T extends AlgorithmParameterSpec> T generateAlgorithmParameterSpec(String provider
      , String algorithm, Class<T> paramSpec) throws GeneralSecurityException {
    return generateAlgorithmParameters(provider, algorithm).getParameterSpec(paramSpec);
  }

  public static <T extends AlgorithmParameterSpec> T generateAlgorithmParameterSpec(String provider,
      String algorithm, int keySize, Class<T> paramSpec) throws GeneralSecurityException {
    return generateAlgorithmParameters(provider, algorithm, keySize).getParameterSpec(paramSpec);
  }


}
