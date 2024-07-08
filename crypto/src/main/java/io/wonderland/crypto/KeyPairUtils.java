package io.wonderland.crypto;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Path;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.KeySpec;
import java.util.Objects;
import java.util.Optional;
import javax.crypto.Cipher;
import javax.crypto.KeyAgreement;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.jcajce.spec.UserKeyingMaterialSpec;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.util.Arrays;

@Slf4j
public class KeyPairUtils {

  public static final String ALGORITHM_CAN_T_BE_EMPTY = "Algorithm can't be empty.";

  private static JcaPEMKeyConverter jcaPEMKeyConverter;
  private static CertificateFactory x509CertificateFactory;

  static {
    Security.addProvider(new BouncyCastleProvider());
    init();
  }


  private KeyPairUtils() {
  }

  private static void init() {
    try {
      x509CertificateFactory = CertificateFactory.getInstance("X.509", CSP.BC);
      jcaPEMKeyConverter = new JcaPEMKeyConverter();
    } catch (Exception e) {
      log.error("Error on BC init().", e);
      System.exit(1);
    }
  }

  public static void logSetup() {
    try {
      Provider[] providers = Security.getProviders();
      if (ArrayUtils.isNotEmpty(providers)) {
        StringBuilder sb = new StringBuilder();
        for (Provider provider : providers) {
          sb.append(provider.getName()).append(" version:").append(provider.getVersionStr())
              .append(" info:")
              .append(provider.getInfo()).append("\n");
        }
        log.info("Security providers : \n{}", sb);
      } else {
        log.info("Security providers : 0.");
      }
      log.info("AES max key length : {}", Cipher.getMaxAllowedKeyLength("AES"));
    } catch (Exception e) {
      log.error("Error ", e);
    }
  }


  /**
   * <br>On handling pem file keys : <a href="https://www.baeldung.com/java-read-pem-file-keys">Pem
   * file keys</a>
   * <br>PKCS8EncodedKeySpec is class for handling private keys material.More at <a
   * href="https://en.wikipedia.org/wiki/PKCS_8">PKCS8 format standard</a>
   *
   * @param pemPath Path of PEM file containing private key.
   * @return Optional private key, (it can be RSAPrivateKey)
   */
  public static Optional<PrivateKey> loadPrivateKeyPem(Path pemPath) {
    try (FileReader fileReader = new FileReader(pemPath.toFile())) {
      PEMParser pemParser = new PEMParser(fileReader);
      PEMKeyPair pemKeyPair = (PEMKeyPair) pemParser.readObject();
      PrivateKeyInfo privateKeyInfo = PrivateKeyInfo.getInstance(pemKeyPair.getPrivateKeyInfo());
      return Optional.of(jcaPEMKeyConverter.getPrivateKey(privateKeyInfo));
    } catch (Exception e) {
      log.error("Error loading PEM private key : ", e);
      return Optional.empty();
    }
  }

  /**
   * <br>On handling pem file keys : <a href="https://www.baeldung.com/java-read-pem-file-keys">Pem
   * file keys</a>
   * <br>X509EncodedKeySpec is class for handling public keys material.More at <a
   * href="https://en.wikipedia.org/wiki/X.509">X509 format standard</a>
   *
   * @param pemPath Path of pem file containing public key
   * @return Optional public key, (it can be RSAPublicKey)
   */
  public static Optional<PublicKey> loadPublicKeyPem(Path pemPath) {
    try (FileReader fileReader = new FileReader(pemPath.toFile())) {
      PEMParser pemParser = new PEMParser(fileReader);
      SubjectPublicKeyInfo subjectPublicKeyInfo = SubjectPublicKeyInfo.getInstance(
          pemParser.readObject());
      return Optional.of(jcaPEMKeyConverter.getPublicKey(subjectPublicKeyInfo));
    } catch (Exception e) {
      log.error("Error loading PEM public key : ", e);
      return Optional.empty();
    }
  }

  /**
   * Load KeyPair (public-private) contained in pem file.
   *
   * @param pemPath Path of pem file containing a key pair
   * @return Optional key pair
   */
  public static Optional<KeyPair> loadKeyPairPem(Path pemPath) {
    try (FileReader fileReader = new FileReader(pemPath.toFile())) {
      PEMParser pemParser = new PEMParser(fileReader);
      PEMKeyPair pemKeyPair = (PEMKeyPair) pemParser.readObject();
      return Optional.of(jcaPEMKeyConverter.getKeyPair(pemKeyPair));
    } catch (Exception e) {
      log.error("Error loading PEM private key : ", e);
      return Optional.empty();
    }
  }

  /**
   * Load X509Certificate from a cert path.
   *
   * @param certPath certification path
   * @return X509 certificate
   * @throws FileNotFoundException when file not found on a path
   * @throws CertificateException  when there are certificate issues
   */
  public static X509Certificate loadX509Certificate(Path certPath)
      throws FileNotFoundException, CertificateException {
    return (X509Certificate) x509CertificateFactory.generateCertificate(
        new FileInputStream(certPath.toFile()));
  }


  public static KeyPair generateKeyPair(String algorithm) throws GeneralSecurityException {
    if (StringUtils.isEmpty(algorithm)) {
      throw new IllegalArgumentException(ALGORITHM_CAN_T_BE_EMPTY);
    }
    KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(algorithm, CSP.BC);
    return keyPairGenerator.generateKeyPair();
  }

  public static KeyPair generateKeyPair(String provider, String algorithm)
      throws GeneralSecurityException {
    KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(algorithm, provider);
    return keyPairGenerator.generateKeyPair();
  }

  public static KeyPair generateKeyPair(String algorithm, int keySize)
      throws GeneralSecurityException {
    if (StringUtils.isEmpty(algorithm)) {
      throw new IllegalArgumentException(ALGORITHM_CAN_T_BE_EMPTY);
    }
    if (keySize <= 0) {
      throw new IllegalArgumentException("Key size can't be smaller than 0");
    }
    KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(algorithm, CSP.BC);
    keyPairGenerator.initialize(keySize);
    return keyPairGenerator.generateKeyPair();
  }

  public static KeyPair generateKeyPair(String algorithm,
      AlgorithmParameterSpec algorithmParameterSpec)
      throws GeneralSecurityException {
    if (StringUtils.isEmpty(algorithm)) {
      throw new IllegalArgumentException(ALGORITHM_CAN_T_BE_EMPTY);
    }
    if (Objects.isNull(algorithmParameterSpec)) {
      throw new IllegalArgumentException("Algorithm parameter spec can't be null.");
    }
    KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(algorithm, CSP.BC);
    keyPairGenerator.initialize(algorithmParameterSpec);
    return keyPairGenerator.generateKeyPair();
  }

  public static KeyPair generateKeyPair(String algorithm,
      AlgorithmParameterSpec algorithmParameterSpec,
      SecureRandom secureRandom)
      throws GeneralSecurityException {
    if (StringUtils.isEmpty(algorithm)) {
      throw new IllegalArgumentException(ALGORITHM_CAN_T_BE_EMPTY);
    }
    if (Objects.isNull(algorithmParameterSpec)) {
      throw new IllegalArgumentException("Algorithm parameter spec can't be null.");
    }
    KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(algorithm, CSP.BC);
    keyPairGenerator.initialize(algorithmParameterSpec, secureRandom);
    return keyPairGenerator.generateKeyPair();
  }


  /**
   * Return a private key for algorithm built from the details in keySpec.
   *
   * @param algorithm the algorithm the key specification is for.
   * @param keySpec   a key specification holding details of the private key.
   * @return a PrivateKey for algorithm
   */
  public static PrivateKey createPrivateKey(String algorithm, KeySpec keySpec)
      throws GeneralSecurityException {
    KeyFactory keyFactory = KeyFactory.getInstance(algorithm, CSP.BC);
    return keyFactory.generatePrivate(keySpec);
  }

  /**
   * Return a public key for algorithm built from the details in keySpec.
   *
   * @param algorithm the algorithm the key specification is for.
   * @param keySpec   a key specification holding details of the public key.
   * @return a PublicKey for algorithm
   */
  public static PublicKey createPublicKey(String algorithm, KeySpec keySpec)
      throws GeneralSecurityException {
    KeyFactory keyFactory = KeyFactory.getInstance(algorithm, CSP.BC);
    return keyFactory.generatePublic(keySpec);
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
