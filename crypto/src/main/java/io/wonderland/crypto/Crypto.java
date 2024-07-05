package io.wonderland.crypto;

import java.nio.ByteBuffer;
import java.security.AlgorithmParameters;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;

public interface Crypto {

  static Cipher createCipher(String transformation, String provider, int opmode, Key key)
      throws GeneralSecurityException {
    Cipher cipher = Cipher.getInstance(transformation, provider);
    cipher.init(opmode, key);
    return cipher;
  }

  static Cipher createCipher(String transformation, String provider, int opmode,
      SecretKey secretKey)
      throws GeneralSecurityException {
    Cipher cipher = Cipher.getInstance(transformation, provider);
    cipher.init(opmode, secretKey);
    return cipher;
  }

  static Cipher createCipher(String transformation, String provider, int opmode,
      SecretKey secretKey,
      AlgorithmParameterSpec algorithmParameterSpec) throws GeneralSecurityException {
    Cipher cipher = Cipher.getInstance(transformation, provider);
    cipher.init(opmode, secretKey, algorithmParameterSpec);
    return cipher;

  }

  static Cipher createCipher(String transformation, String provider, int opmode,
      SecretKey secretKey,
      AlgorithmParameters algorithmParameters)
      throws GeneralSecurityException {
    Cipher cipher = Cipher.getInstance(transformation, provider);
    cipher.init(opmode, secretKey, algorithmParameters);
    return cipher;

  }

  String getProvider();

  String getTransformation();

  Cipher getEncryptCipher();

  Cipher getDecryptCipher();

  byte[] encryptUpdate(byte[] input) throws CryptoException;

  int encryptBuffer(ByteBuffer input, ByteBuffer output) throws CryptoException;

  byte[] encrypt(byte[] input) throws CryptoException;

  byte[] encrypt() throws CryptoException;

  byte[] decryptUpdate(byte[] input) throws CryptoException;

  int decryptBuffer(ByteBuffer input, ByteBuffer output) throws CryptoException;

  byte[] decrypt(byte[] input) throws CryptoException;

  byte[] decrypt() throws CryptoException;

}
