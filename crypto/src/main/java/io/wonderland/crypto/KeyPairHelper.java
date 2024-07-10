package io.wonderland.crypto;

import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.KeySpec;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Getter
public final class KeyPairHelper {

  private final String provider;
  private final String algorithm;
  private final KeyPairGenerator keyPairGenerator;
  private final KeyFactory keyFactory;

  public KeyPairHelper(String algorithm) throws GeneralSecurityException {
    this(CSP.INSTANCE_CONTEXT.getProvider(), algorithm);
  }

  public KeyPairHelper(String provider, String algorithm) throws GeneralSecurityException {
    this.provider = provider;
    this.algorithm = algorithm;
    this.keyPairGenerator = KeyPairGenerator.getInstance(algorithm, provider);
    this.keyFactory = KeyFactory.getInstance(algorithm, provider);
  }


  public KeyPair generateKeyPair() throws GeneralSecurityException {
    return keyPairGenerator.generateKeyPair();
  }

  public KeyPair generateKeyPair(int keySize) throws GeneralSecurityException {
    if (keySize <= 0) {
      throw new IllegalArgumentException("Key size can't be smaller than 0,");
    }
    keyPairGenerator.initialize(keySize);
    return keyPairGenerator.generateKeyPair();
  }

  public KeyPair generateKeyPair(int keySize, SecureRandom secureRandom)
      throws GeneralSecurityException {
    if (keySize <= 0) {
      throw new IllegalArgumentException("Key size can't be smaller than 0,");
    }
    keyPairGenerator.initialize(keySize, secureRandom);
    return keyPairGenerator.generateKeyPair();
  }

  public KeyPair generateKeyPair(AlgorithmParameterSpec algorithmParameterSpec)
      throws GeneralSecurityException {
    keyPairGenerator.initialize(algorithmParameterSpec);
    return keyPairGenerator.generateKeyPair();
  }

  public KeyPair generateKeyPair(AlgorithmParameterSpec algorithmParameterSpec,
      SecureRandom secureRandom) throws GeneralSecurityException {
    keyPairGenerator.initialize(algorithmParameterSpec, secureRandom);
    return keyPairGenerator.generateKeyPair();
  }


  /**
   * Return a private key for algorithm built from the details in keySpec.
   *
   * @param keySpec a key specification holding details of the private key.
   * @return a PrivateKey for algorithm
   */
  public PrivateKey generatePrivateKey(KeySpec keySpec) throws GeneralSecurityException {
    return keyFactory.generatePrivate(keySpec);
  }

  /**
   * Return a public key for algorithm built from the details in keySpec.
   *
   * @param keySpec a key specification holding details of the public key.
   * @return a PublicKey for algorithm
   */
  public PublicKey generatePublicKey(KeySpec keySpec) throws GeneralSecurityException {
    return keyFactory.generatePublic(keySpec);
  }

}
