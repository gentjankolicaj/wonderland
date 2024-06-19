package io.wonderland.crypto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.security.AlgorithmParameters;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.PSSParameterSpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Objects;
import java.util.Optional;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.DHParameterSpec;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.jcajce.spec.DHUParameterSpec;
import org.bouncycastle.jcajce.spec.MQVParameterSpec;
import org.bouncycastle.jcajce.spec.SM2ParameterSpec;
import org.bouncycastle.util.encoders.Base64;
import org.bouncycastle.util.encoders.Hex;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Slf4j
class KeyUtilsTest {

  static final String CSP = "BC";

  @Test
  void logSetup() {
    assertThatCode(KeyUtils::logSetup).doesNotThrowAnyException();
  }

  @Test
  void loadPrivateKeyPem() {
    Path privateKeyPem = Path.of(
        Objects.requireNonNull(
                Thread.currentThread().getContextClassLoader().getResource("private-key.pem"))
            .getPath());
    Optional<PrivateKey> optional = KeyUtils.loadPrivateKeyPem(privateKeyPem);
    assertThat(optional).isPresent();

  }

  @Test
  void loadPublicKeyPem() {
    Path publicKeyPem = Path.of(
        Objects.requireNonNull(
                Thread.currentThread().getContextClassLoader().getResource("public-key.pem"))
            .getPath());
    Optional<PublicKey> optional = KeyUtils.loadPublicKeyPem(publicKeyPem);
    assertThat(optional).isPresent();
  }

  @Test
  void loadKeyPairPem() {
    //NOTE: this pem file also contains public key
    Path privateKeyPem = Path.of(
        Objects.requireNonNull(
                Thread.currentThread().getContextClassLoader().getResource("private-key.pem"))
            .getPath());
    Optional<KeyPair> optional = KeyUtils.loadKeyPairPem(privateKeyPem);
    assertThat(optional).isPresent();
    assertThat(optional.get().getPublic()).isNotNull();
    assertThat(optional.get().getPrivate()).isNotNull();
  }


  @Test
  void loadX509Certificate() throws FileNotFoundException, CertificateException {
    Path certPath = Path.of(
        Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("bc.cer"))
            .getPath());
    X509Certificate x509Certificate = KeyUtils.loadX509Certificate(certPath);
    assertThat(x509Certificate).isNotNull();
  }


  @Test
  void generateSecretKey() throws GeneralSecurityException {
    assertThat(KeyUtils.generateSecretKey("HmacSHA256")).isNotNull();
    assertThat(KeyUtils.generateSecretKey("AES")).isNotNull();
  }

  @Test
  void createSecretKeySpec() {
    byte[] keyBytes = Hex.decode(
        "2ccd85dfc8d18cb5d84fef4b198554679fece6e8692c9147b0da983f5b7bd417");
    assertThat(KeyUtils.createSecretKeySpec("HmacSHA256", keyBytes)).isNotNull();
  }


  @Test
  void RSAGenerateKeyPair() throws GeneralSecurityException {
    KeyPair keyPair0 = KeyUtils.generateKeyPair("RSA", 2048);
    assertThat(keyPair0).isNotNull();
    assertThat(keyPair0.getPrivate()).isNotNull();
    assertThat(keyPair0.getPublic()).isNotNull();
  }

  @Test
  void DSAGenerateKeyPair() throws GeneralSecurityException {
    KeyPair dsaKeyPair = KeyUtils.generateKeyPair("DSA", 2048);
    assertThat(dsaKeyPair).isNotNull();
    assertThat(dsaKeyPair.getPrivate()).isNotNull();
    assertThat(dsaKeyPair.getPublic()).isNotNull();
  }

  @Test
  void ECSM2GenerateKeyPair() throws GeneralSecurityException {
    KeyPair ecKeyPair = KeyUtils.generateKeyPair("EC", new ECGenParameterSpec("sm2p256v1"));
    assertThat(ecKeyPair).isNotNull();
    assertThat(ecKeyPair.getPrivate()).isNotNull();
    assertThat(ecKeyPair.getPublic()).isNotNull();
  }

  @Test
  void ED448GenerateKeyPair() throws GeneralSecurityException {
    KeyPair dsaKeyPair = KeyUtils.generateKeyPair("ED448");
    assertThat(dsaKeyPair).isNotNull();
    assertThat(dsaKeyPair.getPrivate()).isNotNull();
    assertThat(dsaKeyPair.getPublic()).isNotNull();
  }

  @Test
  void DSTU4145GenerateKeyPair() throws GeneralSecurityException {
    KeyPair dstu4145KeyPair = KeyUtils.generateKeyPair("DSTU4145",
        new ECGenParameterSpec("1.2.804.2.1.1.1.1.3.1.1.2.3"));
    assertThat(dstu4145KeyPair).isNotNull();
    assertThat(dstu4145KeyPair.getPrivate()).isNotNull();
    assertThat(dstu4145KeyPair.getPublic()).isNotNull();
  }

  @Test
  @Disabled("disable because of performance")
  void DHGenerateKeyPair() throws GeneralSecurityException {
    KeyPair dhKeyPair0 = KeyUtils.generateKeyPair("DH", 256);
    assertThat(dhKeyPair0).isNotNull();
    assertThat(dhKeyPair0.getPrivate()).isNotNull();
    assertThat(dhKeyPair0.getPublic()).isNotNull();

    //second key pair with algorithm parameter specs
    AlgorithmParameters dhAlgorithmParams = AlgorithmParameterUtils.generateAlgorithmParameters(
        "DH");
    KeyPair dhKeyPair1 = KeyUtils.generateKeyPair("DH",
        dhAlgorithmParams.getParameterSpec(DHParameterSpec.class));
    assertThat(dhKeyPair1).isNotNull();
    assertThat(dhKeyPair1.getPrivate()).isNotNull();
    assertThat(dhKeyPair1.getPublic()).isNotNull();
  }

  @Test
  void generatePKCS5Scheme2() {
    String input = "Hello world223Test023john2043{}{qre|'/.,~wq~!@#$(*)-=-+_";
    byte[] salt = Hex.decode("bbaa99887766554433221100");
    assertThat(KeyUtils.generatePKCS5Scheme2(input.toCharArray(), salt, 2, new SHA256Digest(),
        256)).isNotNull();

    //hasSize(32) because returns byte[] and 256/8=32 bytes length
    assertThat(KeyUtils.generatePKCS5Scheme2(input.toCharArray(), salt, 2, new SHA256Digest(),
        256)).hasSize(32);
  }

  @Test
  void generateSCRYPT() {
    String input = "Hello world223Test023john2043{}{qre|'/.,~wq~!@#$(*)-=-+_";
    byte[] salt = Hex.decode("bbaa99887766554433221100");
    assertThat(KeyUtils.generateSCRYPT(input.toCharArray(), salt, 2, 4, 2, 16)).isNotNull()
        .hasSize(16);
    assertThat(KeyUtils.generateSCRYPT(input.toCharArray(), salt, 2, 4, 2, 32)).isNotNull()
        .hasSize(32);
    assertThat(KeyUtils.generateSCRYPT(input.toCharArray(), salt, 2, 4, 2, 256)).isNotNull()
        .hasSize(256);
    log.debug("generateSCRYPT() : {}",
        new String(KeyUtils.generateSCRYPT(input.toCharArray(), salt, 2, 4, 2, 64)));
  }

  @Test
  void sign() throws GeneralSecurityException {
    String input = "Hello world223Test023john2043{}{qre|'/.,~wq~!@#$(*)-=-+_";
    KeyPair dsaKeyPair = KeyUtils.generateKeyPair("DSA", 2048);
    assertThatCode(
        () -> KeyUtils.sign("SHA256WithDSA", dsaKeyPair.getPrivate(),
            input.getBytes())).doesNotThrowAnyException();
    assertThat(
        KeyUtils.sign("SHA256WithDSA", dsaKeyPair.getPrivate(), input.getBytes())).isNotNull()
        .hasSizeGreaterThan(32);
  }

  @Test
  void verifySign() throws GeneralSecurityException {
    String input = "Hello world223Test023john2043{}{qre|'/.,~wq~!@#$(*)-=-+_";
    KeyPair dsaKeyPair = KeyUtils.generateKeyPair("DSA", 2048);
    byte[] dsaSignedInput = KeyUtils.sign("SHA256WithDSA", dsaKeyPair.getPrivate(),
        input.getBytes());
    assertThat(KeyUtils.verifySign("SHA256WithDSA", dsaKeyPair.getPublic(), input.getBytes(),
        dsaSignedInput)).isTrue();
  }

  @Test
  void verifySignWithCertificate() throws GeneralSecurityException, FileNotFoundException {
    String input = "Hello world223Test023john2043{}{qre|'/.,~wq~!@#$(*)-=-+_";

    //load private key from resources
    Path privateKeyPem = Path.of(
        Objects.requireNonNull(
                Thread.currentThread().getContextClassLoader().getResource("private-key.pem"))
            .getPath());
    Optional<PrivateKey> optional = KeyUtils.loadPrivateKeyPem(privateKeyPem);

    //load x509cert from resources
    Path certPath = Path.of(
        Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("bc.cer"))
            .getPath());
    X509Certificate x509Certificate = KeyUtils.loadX509Certificate(certPath);

    //sign with private key
    byte[] rsaSignedInput = KeyUtils.sign("SHA256WithRSA", optional.get(), input.getBytes());

    //assert with x509certificate
    assertThat(KeyUtils.verifySign("SHA256WithRSA", x509Certificate, input.getBytes(),
        rsaSignedInput)).isTrue();
  }

  @Test
  void verifySignDSTU4145() throws GeneralSecurityException {
    String input = "Hello world223Test023john2043{}{qre|'/.,~wq~!@#$(*)-=-+_";
    KeyPair dstu4145KeyPair = KeyUtils.generateKeyPair("DSTU4145",
        new ECGenParameterSpec("1.2.804.2.1.1.1.1.3.1.1.2.3"));
    byte[] signedInput = KeyUtils.sign("DSTU4145", dstu4145KeyPair.getPrivate(), input.getBytes());
    assertThat(KeyUtils.verifySign("DSTU4145", dstu4145KeyPair.getPublic(), input.getBytes(),
        signedInput)).isTrue();
  }

  @Test
  void verifySignECDSA() throws GeneralSecurityException {
    String input = "Hello world223Test023john2043{}{qre|'/.,~wq~!@#$(*)-=-+_";
    KeyPair ecKeyPair = KeyUtils.generateKeyPair("EC", 256);
    byte[] signedInput = KeyUtils.sign("SHA256withECDSA", ecKeyPair.getPrivate(), input.getBytes());
    assertThat(KeyUtils.verifySign("SHA256withECDSA", ecKeyPair.getPublic(), input.getBytes(),
        signedInput)).isTrue();
  }

  @Test
  void verifySignECDDSA() throws GeneralSecurityException {
    String input = "Hello world223Test023john2043{}{qre|'/.,~wq~!@#$(*)-=-+_";
    KeyPair ecKeyPair = KeyUtils.generateKeyPair("EC", 256);
    byte[] signedInput = KeyUtils.sign("SHA256withECDDSA", ecKeyPair.getPrivate(),
        input.getBytes());
    assertThat(KeyUtils.verifySign("SHA256withECDDSA", ecKeyPair.getPublic(), input.getBytes(),
        signedInput)).isTrue();
  }

  @Test
  void verifySignECP256() throws GeneralSecurityException {
    String input = "Hello world223Test023john2043{}{qre|'/.,~wq~!@#$(*)-=-+_";
    KeyPair ecp256KeyPair = KeyUtils.generateKeyPair("EC", new ECGenParameterSpec("P-256"));
    byte[] signedInput = KeyUtils.sign("SHA256withECDSA", ecp256KeyPair.getPrivate(),
        input.getBytes());
    assertThat(
        KeyUtils.verifySign("SHA256withECDSA", ecp256KeyPair.getPublic(), input.getBytes(),
            signedInput)).isTrue();
  }

  @Test
  void verifySignED448DSA() throws GeneralSecurityException {
    String input = "Hello world223Test023john2043{}{qre|'/.,~wq~!@#$(*)-=-+_";
    KeyPair ed448KeyPair = KeyUtils.generateKeyPair("Ed448");
    byte[] signedInput = KeyUtils.sign("EdDSA", ed448KeyPair.getPrivate(), input.getBytes());
    assertThat(KeyUtils.verifySign("EdDSA", ed448KeyPair.getPublic(), input.getBytes(),
        signedInput)).isTrue();
  }

  @Test
  void verifySignECGOST3410_2012() throws GeneralSecurityException {
    String input = "Hello world223Test023john2043{}{qre|'/.,~wq~!@#$(*)-=-+_";
    KeyPair gostKeyPair = KeyUtils.generateKeyPair("ECGOST3410-2012",
        new ECGenParameterSpec("Tc26-Gost-3410-12-512-paramSetA"));
    byte[] signedInput = KeyUtils.sign("ECGOST3410-2012-512", gostKeyPair.getPrivate(),
        input.getBytes());
    assertThat(
        KeyUtils.verifySign("ECGOST3410-2012-512", gostKeyPair.getPublic(), input.getBytes(),
            signedInput)).isTrue();
  }

  @Test
  void verifySignRSAPSS() throws GeneralSecurityException {
    String input = "Hello world223Test023john2043{}{qre|'/.,~wq~!@#$(*)-=-+_";
    KeyPair rsaKeyPair = KeyUtils.generateKeyPair("RSA", 2048);
    byte[] signedInput = KeyUtils.sign("SHA256withRSAandMGF1", rsaKeyPair.getPrivate(),
        input.getBytes());
    assertThat(
        KeyUtils.verifySign("SHA256withRSAandMGF1", rsaKeyPair.getPublic(), input.getBytes(),
            signedInput)).isTrue();
  }

  @Test
  void verifySignECSM2() throws GeneralSecurityException {
    String input = "Hello world223Test023john2043{}{qre|'/.,~wq~!@#$(*)-=-+_";
    KeyPair ecsm2KeyPair = KeyUtils.generateKeyPair("EC", new ECGenParameterSpec("sm2p256v1"));
    SM2ParameterSpec sm2ParameterSpec = new SM2ParameterSpec("Signer@ID_STRING".getBytes());
    byte[] signedInput = KeyUtils.sign("SM3withSM2", ecsm2KeyPair.getPrivate(), sm2ParameterSpec,
        input.getBytes());
    assertThat(KeyUtils.verifySign("SM3withSM2", ecsm2KeyPair.getPublic(), sm2ParameterSpec,
        input.getBytes(),
        signedInput)).isTrue();
  }

  @Test
  void verifySignWithAlgorithmParameterSpecRSAPSS() throws GeneralSecurityException {
    String input = "Hello world223Test023john2043{}{qre|'/.,~wq~!@#$(*)-=-+_";
    KeyPair rsaKeyPair = KeyUtils.generateKeyPair("RSA", 2048);
    PSSParameterSpec pssParameterSpec = new PSSParameterSpec("SHA-256", "MGF1",
        new MGF1ParameterSpec("SHA-256"),
        32, 1);

    byte[] signedInput = KeyUtils.sign("RSAPSS", rsaKeyPair.getPrivate(), pssParameterSpec,
        input.getBytes());
    assertThat(
        KeyUtils.verifySign("RSAPSS", rsaKeyPair.getPublic(), pssParameterSpec, input.getBytes(),
            signedInput)).isTrue();
  }

  @Test
  void createPrivateKey() throws GeneralSecurityException {
    PKCS8EncodedKeySpec privateKeyEncodedSpec = new PKCS8EncodedKeySpec(
        Base64.decode("MIIEpAIBAAKCAQEAw9w9gsya8eQyIDoG8JZhFBPPdD92pkhKsKZX4lXOPJJ3eCpA\n"
            + "0jCRzJ4ooS0F/oizp/7ND82Aq/rZf9AK/Pem2cCL0hr5PmPO0Z3tsb6cVl7tg5u+\n"
            + "J5Tj1rXVnPNsU1OlPqa6UVwKyhny6AgvvHMbL6M76UL/YM14EpnExNEeT4TR/Ija\n"
            + "F7owy0I7ORwUreNb2DdZFAPdnzurcQj3zaVWfhmcKgPfobdwNjFC7O/sUJcim4Uj\n"
            + "R3y1BnOSnsgfPd1f87EoetZVZ/igE2erT/EWQeoI9/+2ufn3CWhgnxS2burFMwEi\n"
            + "O0On+lVOGLj+0gcgyRh2z7qjxcrcH/B2wjljTwIDAQABAoIBAFmIqZnMfJxNS9jN\n"
            + "jfSXWeN6tuAWTt/uti4QrKYrwW6RKgoFjsJHL69RMZOUaGQWC8KlSQqLT+HOd3Tl\n"
            + "HtDLSTvLuF8gs4WgzJ+oSUtyrjcRiBQcsw2XE5xIXVE1OfTRjP2Z7BxbLhd7Sz5k\n"
            + "16WXHPtm7HFSjjmrU9N09a1fRzLj4MkvlqHdE5zyVWLgYp/KHj45Y17+zwyzsbwB\n"
            + "8eioPGrEnMR+DKmcibGydngdWjHlfAQRqJkkC6uBhHXoEpmjJyG0mNmr8GUW49Rt\n"
            + "pMSJdnRLle9JPkeXMwuko/JoV1M6YrBaVfzTz2+oO3mnPhKHIcPkbjTKA9sOaD+V\n"
            + "Q07AnBkCgYEA6hKHUqW81ItjmhntPOkEwFw0UCIkaREwQt0deksoupENC+KQo40c\n"
            + "oe+EpvRxLF8haDIBiDs+aINg/cHPuWdOxcG4N0AIiQ3HwPRITcz0RvBmMF+W9UNB\n"
            + "TshmeLpFVO5ZcoR5M0xaKrQfg5Bz/PhdfsimwG1JGnspSxGJildBQDUCgYEA1jVR\n"
            + "iZ7Rvhb04uQWbxu0x30BmCOLIfrkotTqfcCblYjF6tr21/hT2prBfem2ubqWKEDp\n"
            + "NWx4xiVBW2/jgDxzKpB7ih1gbhnAI07cBTbq+footij44Sz7eUq5znjTBfjDp1+k\n"
            + "qtP/O/GTGf5LsRfiumOU7PN8ERjG+42FlX1DzfMCgYEA4G+pr1ZZa9bHRwArGGc5\n"
            + "dhQy2M8T6GZhxwrq89LTF6hzQP0ZwKhSVvcpU0g4p9oDVzvzeiOMIHwwaMAII/bp\n"
            + "cfbgYqGUTY2YBex005x8cPSalzFgtoSpPxgqIQJB7kCoJYTeDZDdN+sD+Itum5Wt\n"
            + "WB6evQ1MtgZ3vpHvNmWZnC0CgYBkJ1XSVLGYgT9Kfn6GwJuL0kTWj3fUEWypPYfN\n"
            + "+CpGhkaTgoF7hR4fzc++QXIv8K+YbpEba3YknvKp/+yM3rayJg+9CfM2R0/wskRp\n"
            + "I75F1tMGKK4FCnUhxvCNOyzfU+qW7T8eqDRkIJU4yA835AUcRMcy6r0NeVo/73GP\n"
            + "7ZuwRQKBgQDUeQaatVcT/HRcfFcQv0Khk5Wq5R4CHlkIALeIlyMG11k4G9+AU+J8\n"
            + "8NQfFMs0ch+HgIAym6KDXTTWwSlwpp92FdhW0DVOCMAR0AbT0/o1/o/v9YT9re86\n"
            + "ixbJo5dE0MkKkswVFLw4QCfKIGzFUQh4OsVbi6BoSqxd9raPAsEEbg=="));
    assertThat(KeyUtils.createPrivateKey("RSA", privateKeyEncodedSpec)).isNotNull();
  }

  @Test
  void createPublicKey() throws GeneralSecurityException {
    X509EncodedKeySpec publicKeyEncodedSpec = new X509EncodedKeySpec(
        Base64.decode("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAw9w9gsya8eQyIDoG8JZh\n"
            + "FBPPdD92pkhKsKZX4lXOPJJ3eCpA0jCRzJ4ooS0F/oizp/7ND82Aq/rZf9AK/Pem\n"
            + "2cCL0hr5PmPO0Z3tsb6cVl7tg5u+J5Tj1rXVnPNsU1OlPqa6UVwKyhny6AgvvHMb\n"
            + "L6M76UL/YM14EpnExNEeT4TR/IjaF7owy0I7ORwUreNb2DdZFAPdnzurcQj3zaVW\n"
            + "fhmcKgPfobdwNjFC7O/sUJcim4UjR3y1BnOSnsgfPd1f87EoetZVZ/igE2erT/EW\n"
            + "QeoI9/+2ufn3CWhgnxS2burFMwEiO0On+lVOGLj+0gcgyRh2z7qjxcrcH/B2wjlj\n"
            + "TwIDAQAB")
    );
    assertThat(KeyUtils.createPublicKey("RSA", publicKeyEncodedSpec)).isNotNull();
  }


  @Test
  void keyAgreementGenerateSecret() throws GeneralSecurityException {
    KeyPair aDHKeyPair = KeyUtils.generateKeyPair("DH", 256);
    KeyPair bDHKeyPair = KeyUtils.generateKeyPair("DH", 256);
    assertThat(KeyUtils.generateSecret("DH", aDHKeyPair.getPrivate(), bDHKeyPair.getPublic()))
        .isNotNull().hasSize(32);
    log.debug("generateSecret() , key agreement secret {}",
        new String(KeyUtils.generateSecret("DH", aDHKeyPair.getPrivate(), bDHKeyPair.getPublic())));
  }

  @Test
  void keyAgreementGenerateSecretKey() throws GeneralSecurityException {
    KeyPair aDHKeyPair = KeyUtils.generateKeyPair("DH", 256);
    KeyPair bDHKeyPair = KeyUtils.generateKeyPair("DH", 256);
    byte[] agreedSecret = KeyUtils.generateSecret("DH", aDHKeyPair.getPrivate(),
        bDHKeyPair.getPublic());
    SecretKey secretKey = KeyUtils.generateSecretKey("DH", "AES", aDHKeyPair.getPrivate(),
        bDHKeyPair.getPublic());
    assertThat(secretKey.getEncoded()).containsExactly(agreedSecret);
  }

  @Test
  void keyAgreementGenerateSecretKeyWithKeyMaterial() throws GeneralSecurityException {
    byte[] keyMaterial = "Hello world for AES Key".getBytes();
    KeyPair aECKeyPair = KeyUtils.generateKeyPair("EC", new ECGenParameterSpec("P-256"));
    KeyPair bECKeyPair = KeyUtils.generateKeyPair("EC", new ECGenParameterSpec("P-256"));

    //generate first secret key with key material
    SecretKey aKey = KeyUtils.generateSecretKey("ECCDHwithSHA256KDF", "AES",
        aECKeyPair.getPrivate(),
        bECKeyPair.getPublic(), keyMaterial);

    //generate second secret key with key material
    SecretKey bKey = KeyUtils.generateSecretKey("ECCDHwithSHA256KDF", "AES",
        bECKeyPair.getPrivate(),
        aECKeyPair.getPublic(), keyMaterial);
    assertThat(aKey.getEncoded()).containsExactly(bKey.getEncoded());
    assertThat(aKey.getAlgorithm()).isEqualTo(bKey.getAlgorithm());
  }

  /**
   * Basic Unified Diffie-Hellman example showing use of two key pairs per party in the protocol,
   * with one set being regarded as ephemeral.
   */
  @Test
  void DHUGenerateSecretKey() throws GeneralSecurityException {
    byte[] keyMaterial = "Hello world for AES Key".getBytes();
    KeyPair aDHKeyPair = KeyUtils.generateKeyPair("DH", 256);
    KeyPair bDHKeyPair = KeyUtils.generateKeyPair("DH", 256);

    //Ephemeral key pairs
    KeyPair aDHKeyPairEph = KeyUtils.generateKeyPair("DH", 256);
    KeyPair bDHKeyPairEph = KeyUtils.generateKeyPair("DH", 256);

    //generate first secret key with key material
    DHUParameterSpec aDHUParameterSpec = new DHUParameterSpec(aDHKeyPairEph.getPublic(),
        aDHKeyPairEph.getPrivate(),
        bDHKeyPairEph.getPublic(), keyMaterial);
    SecretKey aKey = KeyUtils.generateSecretKey("DHUwithSHA256KDF", "AES", aDHKeyPair.getPrivate(),
        bDHKeyPair.getPublic(), aDHUParameterSpec);

    //generate second secret key with key material
    DHUParameterSpec bDHUParameterSpec = new DHUParameterSpec(bDHKeyPairEph.getPublic(),
        bDHKeyPairEph.getPrivate(),
        aDHKeyPairEph.getPublic(), keyMaterial);
    SecretKey bKey = KeyUtils.generateSecretKey("DHUwithSHA256KDF", "AES", bDHKeyPair.getPrivate(),
        aDHKeyPair.getPublic(), bDHUParameterSpec);

    assertThat(aKey.getEncoded()).containsExactly(bKey.getEncoded());
    assertThat(aKey.getAlgorithm()).isEqualTo(bKey.getAlgorithm());
  }

  /**
   * Basic Diffie-Hellman MQV example showing use of two key pairs per party in the protocol, with
   * one set being regarded as ephemeral.
   */
  @Test
  void MQVGenerateSecretKey() throws GeneralSecurityException {
    byte[] keyMaterial = "Hello world for AES Key".getBytes();
    KeyPair aDHKeyPair = KeyUtils.generateKeyPair("DH", 256);
    KeyPair bDHKeyPair = KeyUtils.generateKeyPair("DH", 256);

    //Ephemeral key pairs
    KeyPair aDHKeyPairEph = KeyUtils.generateKeyPair("DH", 256);
    KeyPair bDHKeyPairEph = KeyUtils.generateKeyPair("DH", 256);

    //generate first secret key with key material
    MQVParameterSpec aDHUParameterSpec = new MQVParameterSpec(aDHKeyPairEph.getPublic(),
        aDHKeyPairEph.getPrivate(),
        bDHKeyPairEph.getPublic(), keyMaterial);
    SecretKey aKey = KeyUtils.generateSecretKey("MQVwithSHA256KDF", "AES", aDHKeyPair.getPrivate(),
        bDHKeyPair.getPublic(), aDHUParameterSpec);

    //generate second secret key with key material
    MQVParameterSpec bDHUParameterSpec = new MQVParameterSpec(bDHKeyPairEph.getPublic(),
        bDHKeyPairEph.getPrivate(),
        aDHKeyPairEph.getPublic(), keyMaterial);
    SecretKey bKey = KeyUtils.generateSecretKey("MQVwithSHA256KDF", "AES", bDHKeyPair.getPrivate(),
        aDHKeyPair.getPublic(), bDHUParameterSpec);

    assertThat(aKey.getEncoded()).containsExactly(bKey.getEncoded());
    assertThat(aKey.getAlgorithm()).isEqualTo(bKey.getAlgorithm());
  }


  @Test
  void wrapKey() throws GeneralSecurityException {
    KeyPair rsaKeyPair = KeyUtils.generateKeyPair("RSA", 2048);
    SecretKey aesKey = KeyUtils.generateSecretKey("AES");

    assertThat(
        KeyUtils.wrapKey("RSA/NONE/OAEPwithSHA256andMGF1Padding", rsaKeyPair.getPublic(), aesKey))
        .isNotNull().hasSizeGreaterThan(0);
  }

  @Test
  void unwrapKey() throws GeneralSecurityException {
    KeyPair rsaKeyPair = KeyUtils.generateKeyPair("RSA", 2048);
    SecretKey aesKey = KeyUtils.generateSecretKey("AES");
    byte[] wrappedKey = KeyUtils.wrapKey("RSA/NONE/OAEPwithSHA256andMGF1Padding",
        rsaKeyPair.getPublic(), aesKey);
    Key key = KeyUtils.unwrapKey("RSA/NONE/OAEPwithSHA256andMGF1Padding", rsaKeyPair.getPrivate(),
        wrappedKey, "AES",
        Cipher.SECRET_KEY);

    assertThat(key.getAlgorithm()).isEqualTo(aesKey.getAlgorithm());
    assertThat(key.getEncoded()).containsExactly(aesKey.getEncoded());
  }

  @Test
  void unwrapKeyElGamal() throws GeneralSecurityException {
    KeyPair dhKeyPair = KeyUtils.generateKeyPair("DH", 2048);
    SecretKey aesKey = KeyUtils.generateSecretKey("AES");

    byte[] wrappedKey = KeyUtils.wrapKey("ElGamal/NONE/OAEPwithSHA256andMGF1Padding",
        dhKeyPair.getPublic(), aesKey);
    Key key = KeyUtils.unwrapKey("ElGamal/NONE/OAEPwithSHA256andMGF1Padding",
        dhKeyPair.getPrivate(), wrappedKey, "AES",
        Cipher.SECRET_KEY);

    assertThat(key.getAlgorithm()).isEqualTo(aesKey.getAlgorithm());
    assertThat(key.getEncoded()).containsExactly(aesKey.getEncoded());
  }
}