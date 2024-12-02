package io.wonderland.crypto;

import java.nio.ByteBuffer;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.KeyPair;
import java.util.Objects;
import javax.crypto.Cipher;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;

/**
 * <br>A cipher wrapper class for asymmetric cryptography.
 * <br>On object creation 2 ciphers are initialized for encryption & decryption purposes.
 */
@Slf4j
@Getter
public class AsymmetricCipher implements ICipher {

  /**
   * cryptographic security provider
   */
  private final String provider;
  private final String transformation;
  private final Cipher encryptCipher;
  private final Cipher decryptCipher;

  /**
   * Instance is used for encrypt/decrypt with public-private keys.Default cryptographic service
   * provider form CSP.INSTANCE_CONTEXT
   *
   * @param transformation cipher transformation
   * @param encryptKey     encryption key (public | private key)
   * @param decryptKey     decryption key (public | private key)
   * @throws GeneralSecurityException wrapper exception
   */
  public AsymmetricCipher(String transformation, Key encryptKey, Key decryptKey)
      throws GeneralSecurityException {
    this(CSP.INSTANCE_CONTEXT.getProvider(), transformation, encryptKey, decryptKey);
  }

  /**
   * Instance is used for encrypt/decrypt with public-private keys.
   *
   * @param provider       cryptographic service provider CSP
   * @param transformation cipher transformation
   * @param encryptKey     encryption key (public | private key)
   * @param decryptKey     decryption key (public | private key)
   * @throws GeneralSecurityException wrapper exception
   */
  public AsymmetricCipher(String provider, String transformation, Key encryptKey, Key decryptKey)
      throws GeneralSecurityException {
    this.provider = provider;
    this.transformation = transformation;
    this.encryptCipher = ICipher.createCipher(transformation, provider, Cipher.ENCRYPT_MODE,
        encryptKey);
    this.decryptCipher = ICipher.createCipher(transformation, provider, Cipher.DECRYPT_MODE,
        decryptKey);
  }


  /**
   * Instance is used for encrypt/decrypt with public-private keys. Invoking this constructor sets
   * up for encrypting with private-key and decrypting with public-key.Default cryptographic service
   * provider form CSP.INSTANCE_CONTEXT
   *
   * @param transformation cipher transformation
   * @param keyPair        public-private key pair
   * @throws GeneralSecurityException wrapper exception
   */
  public AsymmetricCipher(String transformation, KeyPair keyPair) throws GeneralSecurityException {
    this(CSP.INSTANCE_CONTEXT.getProvider(), transformation, keyPair);
  }

  /**
   * Instance is used for encrypt/decrypt with public-private keys. Invoking this constructor sets
   * up for encrypting with private-key and decrypting with public-key.
   *
   * @param provider       cryptographic service provider
   * @param transformation cipher transformation
   * @param keyPair        public-private key pair
   * @throws GeneralSecurityException wrapper exception
   */
  public AsymmetricCipher(String provider, String transformation, KeyPair keyPair)
      throws GeneralSecurityException {
    this.provider = provider;
    this.transformation = transformation;
    this.encryptCipher = ICipher.createCipher(transformation, provider, Cipher.ENCRYPT_MODE,
        keyPair.getPrivate());
    this.decryptCipher = ICipher.createCipher(transformation, provider, Cipher.DECRYPT_MODE,
        keyPair.getPublic());
  }

  @Override
  public byte[] encryptUpdate(byte[] input) throws CryptoException {
    try {
      if (ArrayUtils.isEmpty(input)) {
        throw new IllegalArgumentException("Input can't be empty.");
      }
      return this.encryptCipher.update(input);
    } catch (Exception e) {
      throw new AsymmetricCipherException(e);
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
      throw new AsymmetricCipherException(e);
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
      throw new AsymmetricCipherException(e);
    }
  }

  @Override
  public byte[] encrypt() throws CryptoException {
    try {
      return this.encryptCipher.doFinal();
    } catch (Exception e) {
      throw new AsymmetricCipherException(e);
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
      throw new AsymmetricCipherException(e);
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
      throw new AsymmetricCipherException(e);
    }
  }

  public byte[] decrypt(byte[] input) throws CryptoException {
    try {
      if (ArrayUtils.isEmpty(input)) {
        throw new IllegalArgumentException("Input can't be empty.");
      }
      return this.decryptCipher.doFinal(input);
    } catch (Exception e) {
      throw new AsymmetricCipherException(e);
    }
  }

  public byte[] decrypt() throws CryptoException {
    try {
      return this.decryptCipher.doFinal();
    } catch (Exception e) {
      throw new AsymmetricCipherException(e);
    }
  }

}
