package io.wonderland.crypto;

import java.security.AlgorithmParameters;
import java.security.GeneralSecurityException;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;

/**
 * <br>A cipher wrapper class for symmetric cryptography.
 * <br>On object creation 2 ciphers are initialized for encryption & decryption purposes.
 */
@Slf4j
@Getter
public class SymmetricCrypto {

  /**
   * cryptographic security provider
   */
  private final String provider;
  private final String transformation;
  private final Cipher encryptCipher;
  private final Cipher decryptCipher;
  private AlgorithmParameterSpec algorithmParameterSpec;
  private AlgorithmParameters algorithmParameters;


  /**
   * Block cipher modes of operation supported only :
   * <br>ECB
   *
   * @param provider        cryptographic service provider CSP
   * @param transformation  cipher transformation
   * @param secretSecretKey symmetric secretSecretKey
   * @throws GeneralSecurityException wrapper exception
   */
  public SymmetricCrypto(String provider, String transformation, SecretKey secretSecretKey)
      throws GeneralSecurityException {
    this.provider = provider;
    this.transformation = transformation;
    this.encryptCipher = createCipher(transformation, Cipher.ENCRYPT_MODE, secretSecretKey);
    this.decryptCipher = createCipher(transformation, Cipher.DECRYPT_MODE, secretSecretKey);
  }

  /**
   * Block cipher modes of operation supported only :
   * <br>CBC
   * <br>CTR
   *
   * @param provider               cryptographic service provider CSP
   * @param transformation         cipher transformation
   * @param secretSecretKey        symmetric secretSecretKey
   * @param algorithmParameterSpec algorithm parameter specs at cipher init
   * @throws GeneralSecurityException wrapper exception
   */
  public SymmetricCrypto(String provider, String transformation, SecretKey secretSecretKey,
      AlgorithmParameterSpec algorithmParameterSpec)
      throws GeneralSecurityException {
    this.provider = provider;
    this.transformation = transformation;
    this.algorithmParameterSpec = algorithmParameterSpec;
    this.encryptCipher = createCipher(transformation, Cipher.ENCRYPT_MODE, secretSecretKey,
        algorithmParameterSpec);
    this.decryptCipher = createCipher(transformation, Cipher.DECRYPT_MODE, secretSecretKey,
        algorithmParameterSpec);
  }

  /**
   * Block cipher modes of operation supported only :
   * <br>CBC
   * <br>CTR
   *
   * @param provider            cryptographic service provider CSP
   * @param transformation      cipher transformation
   * @param secretSecretKey     symmetric secretSecretKey
   * @param algorithmParameters algorithm parameters at cipher init
   * @throws GeneralSecurityException wrapper exception
   */
  public SymmetricCrypto(String provider, String transformation, SecretKey secretSecretKey,
      AlgorithmParameters algorithmParameters)
      throws GeneralSecurityException {
    this.provider = provider;
    this.transformation = transformation;
    this.algorithmParameters = algorithmParameters;
    this.encryptCipher = createCipher(transformation, Cipher.ENCRYPT_MODE, secretSecretKey,
        algorithmParameters);
    this.decryptCipher = createCipher(transformation, Cipher.DECRYPT_MODE, secretSecretKey,
        algorithmParameters);
  }


  private Cipher createCipher(String transformation, int opmode, SecretKey secretSecretKey)
      throws GeneralSecurityException {
    Cipher cipher = Cipher.getInstance(transformation, provider);
    cipher.init(opmode, secretSecretKey);
    return cipher;
  }

  private Cipher createCipher(String transformation, int opmode, SecretKey secretSecretKey,
      AlgorithmParameterSpec algorithmParameterSpec)
      throws GeneralSecurityException {
    Cipher cipher = Cipher.getInstance(transformation, provider);
    cipher.init(opmode, secretSecretKey, algorithmParameterSpec);
    return cipher;

  }

  private Cipher createCipher(String transformation, int opmode, SecretKey secretSecretKey,
      AlgorithmParameters algorithmParameters)
      throws GeneralSecurityException {
    Cipher cipher = Cipher.getInstance(transformation, provider);
    cipher.init(opmode, secretSecretKey, algorithmParameters);
    return cipher;

  }

  public byte[] encrypt(byte[] input) throws CryptoException {
    try {
      if (ArrayUtils.isEmpty(input)) {
        throw new IllegalArgumentException("Input can't be empty.");
      }
      return this.encryptCipher.doFinal(input);
    } catch (Exception e) {
      throw new CryptoException(e);
    }
  }

  public byte[] decrypt(byte[] input) throws CryptoException {
    try {
      if (ArrayUtils.isEmpty(input)) {
        throw new IllegalArgumentException("Input can't be empty.");
      }
      return this.decryptCipher.doFinal(input);
    } catch (Exception e) {
      throw new CryptoException(e);
    }
  }

}
