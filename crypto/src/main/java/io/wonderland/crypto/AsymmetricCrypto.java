package io.wonderland.crypto;

import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.KeyPair;
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
public class AsymmetricCrypto {

  /**
   * cryptographic security provider
   */
  private final String provider;
  private final String transformation;
  private final Cipher encryptCipher;
  private final Cipher decryptCipher;

  /**
   * Instance is used for encrypt/decrypt with public-private keys.
   *
   * @param provider       cryptographic service provider CSP
   * @param transformation cipher transformation
   * @param encryptKey     encryption key (public | private key)
   * @param decryptKey     decryption key (public | private key)
   * @throws GeneralSecurityException wrapper exception
   */
  public AsymmetricCrypto(String provider, String transformation, Key encryptKey, Key decryptKey)
      throws GeneralSecurityException {
    this.provider = provider;
    this.transformation = transformation;
    this.encryptCipher = createCipher(transformation, Cipher.ENCRYPT_MODE, encryptKey);
    this.decryptCipher = createCipher(transformation, Cipher.DECRYPT_MODE, decryptKey);
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
  public AsymmetricCrypto(String provider, String transformation, KeyPair keyPair)
      throws GeneralSecurityException {
    this.provider = provider;
    this.transformation = transformation;
    this.encryptCipher = createCipher(transformation, Cipher.ENCRYPT_MODE, keyPair.getPrivate());
    this.decryptCipher = createCipher(transformation, Cipher.DECRYPT_MODE, keyPair.getPublic());
  }

  private Cipher createCipher(String transformation, int opmode, Key key)
      throws GeneralSecurityException {
    Cipher cipher = Cipher.getInstance(transformation, provider);
    cipher.init(opmode, key);
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
