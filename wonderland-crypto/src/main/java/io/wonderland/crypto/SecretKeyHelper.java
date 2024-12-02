package io.wonderland.crypto;

import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public final class SecretKeyHelper {

  private final String provider;
  private final String algorithm;
  private final KeyGenerator keyGenerator;

  public SecretKeyHelper(String algorithm)
      throws NoSuchAlgorithmException, NoSuchProviderException {
    this(CSP.INSTANCE_CONTEXT.getProvider(), algorithm);
  }

  public SecretKeyHelper(String provider, String algorithm)
      throws NoSuchAlgorithmException, NoSuchProviderException {
    this.provider = provider;
    this.algorithm = algorithm;
    this.keyGenerator = KeyGenerator.getInstance(algorithm, provider);
  }

  public KeyGenerator createGenerator() throws GeneralSecurityException {
    return KeyGenerator.getInstance(algorithm, provider);
  }

  public SecretKey generateSecretKey() throws GeneralSecurityException {
    return keyGenerator.generateKey();
  }

  public SecretKey generateSecretKey(int keySize) throws GeneralSecurityException {
    keyGenerator.init(keySize);
    return keyGenerator.generateKey();
  }

  public SecretKey generateSecretKey(int keySize, SecureRandom secureRandom)
      throws GeneralSecurityException {
    keyGenerator.init(keySize, secureRandom);
    return keyGenerator.generateKey();
  }

  public SecretKey generateSecretKey(AlgorithmParameterSpec spec)
      throws GeneralSecurityException {
    keyGenerator.init(spec);
    return keyGenerator.generateKey();
  }

}
