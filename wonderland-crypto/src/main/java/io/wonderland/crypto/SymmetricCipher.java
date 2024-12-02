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
public class SymmetricCipher implements ICipher {

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
   * Default cryptographic service provider form CSP.INSTANCE_CONTEXT
   *
   * @param transformation cipher transformation
   * @param secretKey      symmetric secretKey
   * @throws GeneralSecurityException wrapper exception
   */
  public SymmetricCipher(String transformation, SecretKey secretKey)
      throws GeneralSecurityException {
    this(CSP.INSTANCE_CONTEXT.getProvider(), transformation, secretKey);
  }


  /**
   * Block cipher modes of operation supported only :
   * <br>ECB
   *
   * @param provider       cryptographic service provider CSP
   * @param transformation cipher transformation
   * @param secretKey      symmetric secretKey
   * @throws GeneralSecurityException wrapper exception
   */
  public SymmetricCipher(String provider, String transformation, SecretKey secretKey)
      throws GeneralSecurityException {
    this.provider = provider;
    this.transformation = transformation;
    this.encryptCipher = ICipher.createCipher(transformation, provider, Cipher.ENCRYPT_MODE,
        secretKey);
    this.decryptCipher = ICipher.createCipher(transformation, provider, Cipher.DECRYPT_MODE,
        secretKey);
  }


  /**
   * Block cipher modes of operation supported only :
   * <br>CBC
   * <br>CTR
   * <br>
   * Default cryptographic service provider form CSP.INSTANCE_CONTEXT
   *
   * @param transformation cipher transformation
   * @param secretKey      symmetric secretKey
   * @param algParamSpec   algorithm parameter specs at cipher init
   * @throws GeneralSecurityException wrapper exception
   */
  public SymmetricCipher(String transformation, SecretKey secretKey,
      AlgorithmParameterSpec algParamSpec) throws GeneralSecurityException {
    this(CSP.INSTANCE_CONTEXT.getProvider(), transformation, secretKey, algParamSpec);
  }


  /**
   * Block cipher modes of operation supported only :
   * <br>CBC
   * <br>CTR
   *
   * @param provider       cryptographic service provider CSP
   * @param transformation cipher transformation
   * @param secretKey      symmetric secretKey
   * @param algParamSpec   algorithm parameter specs at cipher init
   * @throws GeneralSecurityException wrapper exception
   */
  public SymmetricCipher(String provider, String transformation, SecretKey secretKey,
      AlgorithmParameterSpec algParamSpec) throws GeneralSecurityException {
    this.provider = provider;
    this.transformation = transformation;
    this.algorithmParameterSpec = algParamSpec;
    this.encryptCipher = ICipher.createCipher(transformation, provider, Cipher.ENCRYPT_MODE,
        secretKey, algParamSpec);
    this.decryptCipher = ICipher.createCipher(transformation, provider, Cipher.DECRYPT_MODE,
        secretKey, algParamSpec);
  }


  /**
   * Block cipher modes of operation supported only :
   * <br>CBC
   * <br>CTR
   * <br>
   * Default cryptographic service provider form CSP.INSTANCE_CONTEXT
   *
   * @param transformation cipher transformation
   * @param secretKey      symmetric secretKey
   * @param algParams      algorithm parameters at cipher init
   * @throws GeneralSecurityException wrapper exception
   */
  public SymmetricCipher(String transformation, SecretKey secretKey,
      AlgorithmParameters algParams) throws GeneralSecurityException {
    this(CSP.INSTANCE_CONTEXT.getProvider(), transformation, secretKey, algParams);
  }


  /**
   * Block cipher modes of operation supported only :
   * <br>CBC
   * <br>CTR
   *
   * @param provider       cryptographic service provider CSP
   * @param transformation cipher transformation
   * @param secretKey      symmetric secretKey
   * @param algParams      algorithm parameters at cipher init
   * @throws GeneralSecurityException wrapper exception
   */
  public SymmetricCipher(String provider, String transformation, SecretKey secretKey,
      AlgorithmParameters algParams) throws GeneralSecurityException {
    this.provider = provider;
    this.transformation = transformation;
    this.algorithmParameters = algParams;
    this.encryptCipher = ICipher.createCipher(transformation, provider, Cipher.ENCRYPT_MODE,
        secretKey,
        algParams);
    this.decryptCipher = ICipher.createCipher(transformation, provider, Cipher.DECRYPT_MODE,
        secretKey,
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
      throw new SymmetricCipherException(e);
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
      throw new SymmetricCipherException(e);
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
      throw new SymmetricCipherException(e);
    }
  }

  @Override
  public byte[] encrypt() throws CryptoException {
    try {
      return this.encryptCipher.doFinal();
    } catch (Exception e) {
      throw new SymmetricCipherException(e);
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
      throw new SymmetricCipherException(e);
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
      throw new SymmetricCipherException(e);
    }
  }

  public byte[] decrypt(byte[] input) throws CryptoException {
    try {
      if (ArrayUtils.isEmpty(input)) {
        throw new IllegalArgumentException("Input can't be empty.");
      }
      return this.decryptCipher.doFinal(input);
    } catch (Exception e) {
      throw new SymmetricCipherException(e);
    }
  }

  public byte[] decrypt() throws CryptoException {
    try {
      return this.decryptCipher.doFinal();
    } catch (Exception e) {
      throw new SymmetricCipherException(e);
    }
  }

}
