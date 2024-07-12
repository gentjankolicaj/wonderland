package io.wonderland.crypto;

import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.PBEParametersGenerator;
import org.bouncycastle.crypto.generators.PKCS5S2ParametersGenerator;
import org.bouncycastle.crypto.generators.SCrypt;
import org.bouncycastle.crypto.params.KeyParameter;

@Slf4j
public final class SecretKeyUtils {

  private SecretKeyUtils() {
  }

  public static KeyGenerator createGenerator(String provider, String algorithm)
      throws GeneralSecurityException {
    return KeyGenerator.getInstance(algorithm, provider);
  }

  public static SecretKey generateSecretKey(String provider, String algorithm)
      throws GeneralSecurityException {
    KeyGenerator keyGenerator = KeyGenerator.getInstance(algorithm, provider);
    return keyGenerator.generateKey();
  }

  public static SecretKey generateSecretKey(String provider, String algorithm, int keySize)
      throws GeneralSecurityException {
    KeyGenerator keyGenerator = KeyGenerator.getInstance(algorithm, provider);
    keyGenerator.init(keySize);
    return keyGenerator.generateKey();
  }

  public static SecretKey generateSecretKey(String provider, String algorithm, int keySize,
      SecureRandom secureRandom)
      throws GeneralSecurityException {
    KeyGenerator keyGenerator = KeyGenerator.getInstance(algorithm, provider);
    keyGenerator.init(keySize, secureRandom);
    return keyGenerator.generateKey();
  }


  public static SecretKeySpec createSecretKeySpec(String algorithm, byte[] keyBytes) {
    return new SecretKeySpec(keyBytes, algorithm);
  }


  /**
   * Calculate a derived key using PBKDF2 based on digest using BC low-level api.
   *
   * @param password       password parameter
   * @param salt           salt parameter
   * @param iterationCount iteration count parameter
   * @param digest         Digest type
   * @param keySize        key size
   * @return Password-based key derived 2 from hash
   */
  public static byte[] generateWithPKCS5S2(char[] password, byte[] salt, int iterationCount,
      Digest digest, int keySize) {
    PBEParametersGenerator generator = new PKCS5S2ParametersGenerator(digest);
    generator.init(PBEParametersGenerator.PKCS5PasswordToUTF8Bytes(password), salt, iterationCount);
    return ((KeyParameter) generator.generateDerivedParameters(keySize)).getKey();
  }

  /**
   * Calculate a derived key using SCRYPT using BC low-level api.
   *
   * @param password                 password parameter
   * @param salt                     salt parameter
   * @param costParameter            N – CPU/Memory cost parameter. Must be larger than 1, a power
   *                                 of 2 and less than 2^(128 * r / 8).
   * @param blockSize                the block size, must be >= 1.
   * @param parallelizationParameter Parallelization parameter. Must be a positive integer less than
   *                                 or equal to Integer.MAX_VALUE / (128 * r * 8).
   * @param keyLength                the length of the key to generate.
   * @return Password-based key derived SCRYPT
   */
  public static byte[] generateWithSCRYPT(char[] password, byte[] salt, int costParameter,
      int blockSize, int parallelizationParameter, int keyLength) {
    return SCrypt.generate(PBEParametersGenerator.PKCS5PasswordToUTF8Bytes(password), salt,
        costParameter, blockSize,
        parallelizationParameter, keyLength);
  }


  /**
   * Wraps a key using a secret key as base.
   *
   * @param provider       cryptographic service provider
   * @param transformation cipher transformation
   * @param cipherKey      cipher key base for wrapping
   * @param keyToWrap      key to be encrypted/wrapped
   * @return wrapped key
   * @throws GeneralSecurityException generic exception
   */
  public static byte[] wrapKey(String provider, String transformation, Key cipherKey, Key keyToWrap)
      throws GeneralSecurityException {
    Cipher cipher = Cipher.getInstance(transformation, provider);
    cipher.init(Cipher.WRAP_MODE, cipherKey);
    return cipher.wrap(keyToWrap);
  }


  /**
   * Wraps a key using a secret key as base.
   *
   * @param provider       cryptographic service provider
   * @param transformation cipher transformation
   * @param cipherKey      cipher key base for wrapping
   * @param spec           algorithm param spec for cipher
   * @param keyToWrap      key to be encrypted/wrapped
   * @return wrapped key
   * @throws GeneralSecurityException generic exception
   */
  public static byte[] wrapKey(String provider, String transformation, Key cipherKey,
      AlgorithmParameterSpec spec, Key keyToWrap)
      throws GeneralSecurityException {
    Cipher cipher = Cipher.getInstance(transformation, provider);
    cipher.init(Cipher.WRAP_MODE, cipherKey, spec);
    return cipher.wrap(keyToWrap);
  }

  /**
   * Unwraps a key using secret key as base.
   *
   * @param provider            cryptographic service provider
   * @param transformation      cipher transformation
   * @param cipherKey           cipher key for unwrapping
   * @param wrappedKey          wrapped key
   * @param wrappedKeyAlgorithm algorithm of key wrapped
   * @param wrappedKeyType      wrapped key type
   * @return key unwrapped
   * @throws GeneralSecurityException generic exception
   */
  public static Key unwrapKey(String provider, String transformation, Key cipherKey,
      byte[] wrappedKey, String wrappedKeyAlgorithm, int wrappedKeyType)
      throws GeneralSecurityException {
    Cipher cipher = Cipher.getInstance(transformation, provider);
    cipher.init(Cipher.UNWRAP_MODE, cipherKey);
    return cipher.unwrap(wrappedKey, wrappedKeyAlgorithm, wrappedKeyType);
  }

  /**
   * Unwraps a key using secret key as base.
   *
   * @param provider            cryptographic service provider
   * @param transformation      cipher transformation
   * @param cipherKey           cipher key for unwrapping
   * @param spec                algorithm param spec for cipher
   * @param wrappedKey          wrapped key
   * @param wrappedKeyAlgorithm algorithm of key wrapped
   * @param wrappedKeyType      wrapped key type
   * @return key unwrapped
   * @throws GeneralSecurityException generic exception
   */
  public static Key unwrapKey(String provider, String transformation, Key cipherKey,
      AlgorithmParameterSpec spec, byte[] wrappedKey, String wrappedKeyAlgorithm,
      int wrappedKeyType)
      throws GeneralSecurityException {
    Cipher cipher = Cipher.getInstance(transformation, provider);
    cipher.init(Cipher.UNWRAP_MODE, cipherKey, spec);
    return cipher.unwrap(wrappedKey, wrappedKeyAlgorithm, wrappedKeyType);
  }

}
