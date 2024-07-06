package io.wonderland.crypto;

import java.security.AlgorithmParameterGenerator;
import java.security.AlgorithmParameters;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
@Getter
public class AlgorithmParameterHelper {

  private final String provider;

  public AlgorithmParameterHelper() {
    this.provider = CSP.INSTANCE_CONTEXT.getProvider();
  }

  public AlgorithmParameterHelper(String provider) {
    if (StringUtils.isNotEmpty(provider)) {
      this.provider = provider;
    } else {
      this.provider = CSP.INSTANCE_CONTEXT.getProvider();
    }
  }


  public IvParameterSpec generateIvParameterSpec(String secureRandomAlgorithm,
      int vectorSize) throws GeneralSecurityException {
    SecureRandom secureRandom = SecureRandom.getInstance(secureRandomAlgorithm, provider);
    byte[] vector = new byte[vectorSize];
    secureRandom.nextBytes(vector);
    return new IvParameterSpec(vector);
  }

  /**
   * @param algorithm name
   * @return Algorithm parameter generator
   * @throws GeneralSecurityException exception
   */
  public AlgorithmParameterGenerator getAlgorithmParamGen(String algorithm)
      throws GeneralSecurityException {
    return AlgorithmParameterGenerator.getInstance(algorithm, provider);
  }

  /**
   * @param algorithm name
   * @return Algorithm parameter generator
   * @throws GeneralSecurityException exception
   */
  public AlgorithmParameterGenerator getAlgorithmParamGen(String algorithm, int keySize)
      throws GeneralSecurityException {
    AlgorithmParameterGenerator generator = AlgorithmParameterGenerator.getInstance(algorithm,
        provider);
    generator.init(keySize);
    return generator;
  }

  public AlgorithmParameters generateAlgorithmParameters(String algorithm)
      throws GeneralSecurityException {
    return getAlgorithmParamGen(algorithm).generateParameters();
  }

  public AlgorithmParameters generateAlgorithmParameters(String algorithm, int keySize)
      throws GeneralSecurityException {
    return getAlgorithmParamGen(algorithm, keySize).generateParameters();
  }

  public <T extends AlgorithmParameterSpec> T generateAlgorithmParameterSpec(
      String algorithm,
      Class<T> paramSpec) throws GeneralSecurityException {
    return generateAlgorithmParameters(algorithm).getParameterSpec(paramSpec);
  }

  public <T extends AlgorithmParameterSpec> T generateAlgorithmParameterSpec(
      String algorithm, int keySize,
      Class<T> paramSpec) throws GeneralSecurityException {
    return generateAlgorithmParameters(algorithm, keySize).getParameterSpec(paramSpec);
  }


}
