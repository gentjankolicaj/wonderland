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
    this(CSP.INSTANCE_CONTEXT.getProvider());
  }

  public AlgorithmParameterHelper(String provider) {
    if (StringUtils.isNotEmpty(provider)) {
      this.provider = provider;
    } else {
      this.provider = CSP.INSTANCE_CONTEXT.getProvider();
    }
  }


  public IvParameterSpec generateIvParamSpec(String secureRandomAlgorithm,
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
  public AlgorithmParameterGenerator getAlgParamGen(String algorithm)
      throws GeneralSecurityException {
    return AlgorithmParameterGenerator.getInstance(algorithm, provider);
  }

  /**
   * @param algorithm name
   * @return Algorithm parameter generator
   * @throws GeneralSecurityException exception
   */
  public AlgorithmParameterGenerator getAlgParamGen(String algorithm, int keySize)
      throws GeneralSecurityException {
    AlgorithmParameterGenerator generator = AlgorithmParameterGenerator.getInstance(algorithm,
        provider);
    generator.init(keySize);
    return generator;
  }

  public AlgorithmParameters generateAlgParams(String algorithm)
      throws GeneralSecurityException {
    return getAlgParamGen(algorithm).generateParameters();
  }

  public AlgorithmParameters generateAlgParams(String algorithm, int keySize)
      throws GeneralSecurityException {
    return getAlgParamGen(algorithm, keySize).generateParameters();
  }

  public <T extends AlgorithmParameterSpec> T generateAlgParamSpec(
      String algorithm,
      Class<T> paramSpec) throws GeneralSecurityException {
    return generateAlgParams(algorithm).getParameterSpec(paramSpec);
  }

  public <T extends AlgorithmParameterSpec> T generateAlgParamSpec(
      String algorithm, int keySize,
      Class<T> paramSpec) throws GeneralSecurityException {
    return generateAlgParams(algorithm, keySize).getParameterSpec(paramSpec);
  }

}
