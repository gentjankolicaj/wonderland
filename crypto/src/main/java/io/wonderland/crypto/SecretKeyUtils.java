package io.wonderland.crypto;

import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.cert.CertificateFactory;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.Cipher;
import javax.crypto.KeyAgreement;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.PBEParametersGenerator;
import org.bouncycastle.crypto.generators.PKCS5S2ParametersGenerator;
import org.bouncycastle.crypto.generators.SCrypt;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.jcajce.spec.UserKeyingMaterialSpec;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.util.Arrays;

@Slf4j
public class SecretKeyUtils {

  public static final String ALGORITHM_CAN_T_BE_EMPTY = "Algorithm can't be empty.";

  private static JcaPEMKeyConverter jcaPEMKeyConverter;
  private static CertificateFactory x509CertificateFactory;

  static {
    Security.addProvider(new BouncyCastleProvider());
  }


  private SecretKeyUtils() {
  }


  public static SecretKey generateSecretKey(String algorithm) throws GeneralSecurityException {
    if (StringUtils.isEmpty(algorithm)) {
      throw new IllegalArgumentException(ALGORITHM_CAN_T_BE_EMPTY);
    }
    KeyGenerator keyGenerator = KeyGenerator.getInstance(algorithm, CSP.BC);
    return keyGenerator.generateKey();
  }

  public static SecretKey generateSecretKey(String algorithm, int keySize)
      throws GeneralSecurityException {
    if (StringUtils.isEmpty(algorithm)) {
      throw new IllegalArgumentException(ALGORITHM_CAN_T_BE_EMPTY);
    }
    KeyGenerator keyGenerator = KeyGenerator.getInstance(algorithm, CSP.BC);
    keyGenerator.init(keySize);
    return keyGenerator.generateKey();
  }


  public static SecretKeySpec createSecretKeySpec(String algorithm, byte[] keyBytes) {
    if (StringUtils.isEmpty(algorithm)) {
      throw new IllegalArgumentException(ALGORITHM_CAN_T_BE_EMPTY);
    }
    if (ArrayUtils.isEmpty(keyBytes)) {
      throw new IllegalArgumentException("Key bytes can't be empty.");
    }
    return new SecretKeySpec(keyBytes, algorithm);
  }


  /**
   * Calculate a derived key using PBKDF2 based on digest using BC low-level api.
   *
   * @param password       password input
   * @param salt           salt parameter
   * @param iterationCount iteration count parameter
   * @param digest         Digest type
   * @param keySize        key size
   * @return Password-based key derived 2
   */
  public static byte[] generatePKCS5Scheme2(char[] password, byte[] salt, int iterationCount,
      Digest digest,
      int keySize) {
    PBEParametersGenerator generator = new PKCS5S2ParametersGenerator(digest);
    generator.init(PBEParametersGenerator.PKCS5PasswordToUTF8Bytes(password), salt, iterationCount);
    return ((KeyParameter) generator.generateDerivedParameters(keySize)).getKey();
  }

  /**
   * Calculate a derived key using SCRYPT using BC low-level api.
   *
   * @param password                 password input
   * @param salt                     salt parameter
   * @param costParameter            N – CPU/Memory cost parameter. Must be larger than 1, a power
   *                                 of 2 and less than 2^(128 * r / 8).
   * @param blockSize                the block size, must be >= 1.
   * @param parallelizationParameter Parallelization parameter. Must be a positive integer less than
   *                                 or equal to Integer.MAX_VALUE / (128 * r * 8).
   * @param keyLength                the length of the key to generate.
   * @return Password-based key derived SCRYPT
   */
  public static byte[] generateSCRYPT(char[] password, byte[] salt, int costParameter,
      int blockSize,
      int parallelizationParameter, int keyLength) {
    return SCrypt.generate(PBEParametersGenerator.PKCS5PasswordToUTF8Bytes(password), salt,
        costParameter, blockSize,
        parallelizationParameter, keyLength);
  }


  /**
   * Generate an agreed secret byte value .
   *
   * @param keyAgreementAlgorithm Key agreement algorithm
   * @param aPrivateKey           Party A's private key.
   * @param bPublicKey            Party B's public key.
   * @return bytes of the generated secret.
   */
  public static byte[] generateSecret(String keyAgreementAlgorithm, PrivateKey aPrivateKey,
      PublicKey bPublicKey)
      throws GeneralSecurityException {
    KeyAgreement agreement = KeyAgreement.getInstance(keyAgreementAlgorithm, CSP.BC);
    agreement.init(aPrivateKey);
    agreement.doPhase(bPublicKey, true);
    byte[] secretBuffer = agreement.generateSecret();
    return Arrays.copyOfRange(secretBuffer, 0, secretBuffer.length);
  }

  /**
   * Generate an agreed secret key value .
   *
   * @param keyAgreementAlgorithm Key agreement algorithm
   * @param secretKeyAlgorithm    algorithm for secret key
   * @param aPrivateKey           Party A's private key.
   * @param bPublicKey,           Party B's public key.
   * @return the generated secret key .
   */
  public static SecretKey generateSecretKey(String keyAgreementAlgorithm, String secretKeyAlgorithm,
      PrivateKey aPrivateKey, PublicKey bPublicKey) throws GeneralSecurityException {
    KeyAgreement agreement = KeyAgreement.getInstance(keyAgreementAlgorithm, CSP.BC);
    agreement.init(aPrivateKey);
    agreement.doPhase(bPublicKey, true);
    return agreement.generateSecret(secretKeyAlgorithm);
  }


  /**
   * Generate an agreed secret key value .
   *
   * @param keyAgreementAlgorithm Key agreement algorithm
   * @param secretKeyAlgorithm    algorithm for secret key
   * @param aPrivateKey           Party A's private key.
   * @param bPublicKey,           Party B's public key.
   * @param keyMaterial           key material
   * @return the generated secret key.
   */
  public static SecretKey generateSecretKey(String keyAgreementAlgorithm, String secretKeyAlgorithm,
      PrivateKey aPrivateKey, PublicKey bPublicKey, byte[] keyMaterial)
      throws GeneralSecurityException {
    KeyAgreement agreement = KeyAgreement.getInstance(keyAgreementAlgorithm, CSP.BC);
    agreement.init(aPrivateKey, new UserKeyingMaterialSpec(keyMaterial));
    agreement.doPhase(bPublicKey, true);
    return agreement.generateSecret(secretKeyAlgorithm);
  }


  /**
   * Generate an agreed secret key value using the Unified Diffie-Hellman model.
   *
   * @param keyAgreementAlgorithm  Key agreement algorithm
   * @param secretKeyAlgorithm     algorithm for secret key
   * @param aPrivateKey            Party A's private key.
   * @param bPublicKey             Party B's public key.
   * @param algorithmParameterSpec Algorithm parameter spec (Maybe it can be DHUParameterSpec |
   *                               MQVParameterSpec...)
   * @return the generated secret key.
   */
  public static SecretKey generateSecretKey(String keyAgreementAlgorithm, String secretKeyAlgorithm,
      PrivateKey aPrivateKey, PublicKey bPublicKey, AlgorithmParameterSpec algorithmParameterSpec)
      throws GeneralSecurityException {
    KeyAgreement agreement = KeyAgreement.getInstance(keyAgreementAlgorithm, CSP.BC);
    agreement.init(aPrivateKey, algorithmParameterSpec);
    agreement.doPhase(bPublicKey, true);
    return agreement.generateSecret(secretKeyAlgorithm);
  }

  /**
   * Wraps a key using a public key as base.
   *
   * @param transformation cipher transformation
   * @param publicKey      public key to base for wrapping
   * @param key            key to be encrypted/wrapped
   * @return wrapped key
   * @throws GeneralSecurityException generic exception
   */
  public static byte[] wrapKey(String transformation, PublicKey publicKey, Key key)
      throws GeneralSecurityException {
    Cipher cipher = Cipher.getInstance(transformation, CSP.BC);
    cipher.init(Cipher.WRAP_MODE, publicKey);
    return cipher.wrap(key);
  }

  /**
   * Unwraps a key using private key.
   *
   * @param transformation      cipher transformation
   * @param privateKey          private key for unwrapping
   * @param wrappedKey          wrapped key
   * @param wrappedKeyAlgorithm algorithm of key wrapped
   * @param wrappedKeyType      wrapped key type
   * @return key unwrapped
   * @throws GeneralSecurityException generic exception
   */
  public static Key unwrapKey(String transformation, PrivateKey privateKey, byte[] wrappedKey,
      String wrappedKeyAlgorithm, int wrappedKeyType) throws GeneralSecurityException {
    Cipher cipher = Cipher.getInstance(transformation, CSP.BC);
    cipher.init(Cipher.UNWRAP_MODE, privateKey);
    return cipher.unwrap(wrappedKey, wrappedKeyAlgorithm, wrappedKeyType);
  }


}
