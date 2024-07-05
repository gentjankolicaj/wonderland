package io.wonderland.crypto;

import java.nio.ByteBuffer;
import java.security.AlgorithmParameters;
import java.security.GeneralSecurityException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Objects;
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
public class SymmetricCrypto implements Crypto {

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
    this.encryptCipher = Crypto.createCipher(transformation, provider, Cipher.ENCRYPT_MODE,
        secretSecretKey);
    this.decryptCipher = Crypto.createCipher(transformation, provider, Cipher.DECRYPT_MODE,
        secretSecretKey);
  }

  /**
   * Block cipher modes of operation supported only :
   * <br>CBC
   * <br>CTR
   *
   * @param provider               cryptographic service provider CSP
   * @param transformation         cipher transformation
   * @param secretSecretKey        symmetric secretSecretKey
   * @param algParamSpec algorithm parameter specs at cipher init
   * @throws GeneralSecurityException wrapper exception
   */
  public SymmetricCrypto(String provider, String transformation, SecretKey secretSecretKey,
      AlgorithmParameterSpec algParamSpec) throws GeneralSecurityException {
    this.provider = provider;
    this.transformation = transformation;
    this.algorithmParameterSpec = algParamSpec;
    this.encryptCipher = Crypto.createCipher(transformation, provider, Cipher.ENCRYPT_MODE,
        secretSecretKey, algParamSpec);
    this.decryptCipher = Crypto.createCipher(transformation, provider, Cipher.DECRYPT_MODE,
        secretSecretKey, algParamSpec);
  }

  /**
   * Block cipher modes of operation supported only :
   * <br>CBC
   * <br>CTR
   *
   * @param provider            cryptographic service provider CSP
   * @param transformation      cipher transformation
   * @param secretSecretKey     symmetric secretSecretKey
   * @param algParams algorithm parameters at cipher init
   * @throws GeneralSecurityException wrapper exception
   */
  public SymmetricCrypto(String provider, String transformation, SecretKey secretSecretKey,
      AlgorithmParameters algParams) throws GeneralSecurityException {
    this.provider = provider;
    this.transformation = transformation;
    this.algorithmParameters = algParams;
    this.encryptCipher = Crypto.createCipher(transformation, provider, Cipher.ENCRYPT_MODE,
        secretSecretKey,
        algParams);
    this.decryptCipher = Crypto.createCipher(transformation, provider, Cipher.DECRYPT_MODE,
        secretSecretKey,
        algParams);
  }


  @Override
  public byte[] encryptUpdate(byte[] input) throws CryptoException {
    try {
      if (ArrayUtils.isEmpty(input)) {
        throw new IllegalArgumentException("Input can't be empty.");
      }
      return this.encryptCipher.update(input);
    } catch (Exception e) {
      throw new SymmetricCryptoException(e);
    }
  }

  @Override
  public int encryptBuffer(ByteBuffer input, ByteBuffer output) throws CryptoException {
    try {
      if (Objects.isNull(input) || Objects.isNull(output)) {
        throw new IllegalArgumentException("Buffer arguments can't be null");
      }
      return this.encryptCipher.update(input, output);
    } catch (Exception e) {
      throw new SymmetricCryptoException(e);
    }
  }

  @Override
  public byte[] encrypt(byte[] input) throws CryptoException {
    try {
      if (ArrayUtils.isEmpty(input)) {
        throw new IllegalArgumentException("Input can't be empty.");
      }
      return this.encryptCipher.doFinal(input);
    } catch (Exception e) {
      throw new SymmetricCryptoException(e);
    }
  }

  @Override
  public byte[] encrypt() throws CryptoException {
    try {
      return this.encryptCipher.doFinal();
    } catch (Exception e) {
      throw new SymmetricCryptoException(e);
    }
  }

  @Override
  public byte[] decryptUpdate(byte[] input) throws CryptoException {
    try {
      if (ArrayUtils.isEmpty(input)) {
        throw new IllegalArgumentException("Input can't be empty.");
      }
      return this.decryptCipher.update(input);
    } catch (Exception e) {
      throw new SymmetricCryptoException(e);
    }
  }

  @Override
  public int decryptBuffer(ByteBuffer input, ByteBuffer output) throws CryptoException {
    try {
      if (Objects.isNull(input) || Objects.isNull(output)) {
        throw new IllegalArgumentException("Buffer arguments can't be null");
      }
      return this.decryptCipher.update(input, output);
    } catch (Exception e) {
      throw new SymmetricCryptoException(e);
    }
  }

  public byte[] decrypt(byte[] input) throws CryptoException {
    try {
      if (ArrayUtils.isEmpty(input)) {
        throw new IllegalArgumentException("Input can't be empty.");
      }
      return this.decryptCipher.doFinal(input);
    } catch (Exception e) {
      throw new SymmetricCryptoException(e);
    }
  }

  public byte[] decrypt() throws CryptoException {
    try {
      return this.decryptCipher.doFinal();
    } catch (Exception e) {
      throw new SymmetricCryptoException(e);
    }
  }

}
