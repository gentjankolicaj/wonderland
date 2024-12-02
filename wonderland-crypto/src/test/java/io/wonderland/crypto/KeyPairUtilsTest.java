package io.wonderland.crypto;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Path;
import java.security.AlgorithmParameters;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Objects;
import java.util.Optional;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.DHParameterSpec;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.util.encoders.Base64;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Slf4j
class KeyPairUtilsTest extends AbstractTest {


  @Test
  void getPrivateKeyPem() {
    Path privateKeyPem = Path.of(
        Objects.requireNonNull(
                Thread.currentThread().getContextClassLoader().getResource("private-key.pem"))
            .getPath());
    Optional<PrivateKey> optional = KeyPairUtils.getPrivateKeyFromPem(privateKeyPem);
    assertThat(optional).isPresent();

  }

  @Test
  void getPublicKeyFromPem() {
    Path publicKeyPem = Path.of(
        Objects.requireNonNull(
                Thread.currentThread().getContextClassLoader().getResource("public-key.pem"))
            .getPath());
    Optional<PublicKey> optional = KeyPairUtils.getPublicKeyFromPem(publicKeyPem);
    assertThat(optional).isPresent();
  }

  @Test
  void getKeyPairFromPem() {
    //NOTE: this pem file also contains public key
    Path privateKeyPem = Path.of(
        Objects.requireNonNull(
                Thread.currentThread().getContextClassLoader().getResource("private-key.pem"))
            .getPath());
    Optional<KeyPair> optional = KeyPairUtils.getKeyPairFromPem(privateKeyPem);
    assertThat(optional).isPresent();
    assertThat(optional.get().getPublic()).isNotNull();
    assertThat(optional.get().getPrivate()).isNotNull();
  }


  @Test
  void getX509Certificate() throws CertificateException {
    Path certPath = Path.of(
        Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("bc.cer"))
            .getPath());
    X509Certificate x509Certificate = KeyPairUtils.getX509Certificate(certPath);
    assertThat(x509Certificate).isNotNull();
  }


  @Test
  void RSAGenerateKeyPair() throws GeneralSecurityException {
    KeyPair keyPair0 = KeyPairUtils.generateKeyPair(CSP_NAME, "RSA", 2048);
    assertThat(keyPair0).isNotNull();
    assertThat(keyPair0.getPrivate()).isNotNull();
    assertThat(keyPair0.getPublic()).isNotNull();

    KeyPair keyPair1 = KeyPairUtils.generateKeyPair(CSP_NAME, "RSA", 1028, new SecureRandom());
    assertThat(keyPair1).isNotNull();
    assertThat(keyPair1.getPrivate()).isNotNull();
    assertThat(keyPair1.getPublic()).isNotNull();

  }

  @Test
  void DSAGenerateKeyPair() throws GeneralSecurityException {
    KeyPair dsaKeyPair = KeyPairUtils.generateKeyPair(CSP_NAME, "DSA", 2048);
    assertThat(dsaKeyPair).isNotNull();
    assertThat(dsaKeyPair.getPrivate()).isNotNull();
    assertThat(dsaKeyPair.getPublic()).isNotNull();
  }

  @Test
  void ECSM2GenerateKeyPair() throws GeneralSecurityException {
    KeyPair ecKeyPair = KeyPairUtils.generateKeyPair(CSP_NAME, "EC",
        new ECGenParameterSpec("sm2p256v1"));
    assertThat(ecKeyPair).isNotNull();
    assertThat(ecKeyPair.getPrivate()).isNotNull();
    assertThat(ecKeyPair.getPublic()).isNotNull();
  }

  @Test
  void ED448GenerateKeyPair() throws GeneralSecurityException {
    KeyPair dsaKeyPair = KeyPairUtils.generateKeyPair(CSP_NAME, "ED448");
    assertThat(dsaKeyPair).isNotNull();
    assertThat(dsaKeyPair.getPrivate()).isNotNull();
    assertThat(dsaKeyPair.getPublic()).isNotNull();
  }

  @Test
  void DSTU4145GenerateKeyPair() throws GeneralSecurityException {
    KeyPair dstu4145KeyPair = KeyPairUtils.generateKeyPair(CSP_NAME, "DSTU4145",
        new ECGenParameterSpec("1.2.804.2.1.1.1.1.3.1.1.2.3"));
    assertThat(dstu4145KeyPair).isNotNull();
    assertThat(dstu4145KeyPair.getPrivate()).isNotNull();
    assertThat(dstu4145KeyPair.getPublic()).isNotNull();
  }

  @Test
  @Disabled("disable because of performance")
  void DHGenerateKeyPair() throws GeneralSecurityException {
    KeyPair dhKeyPair0 = KeyPairUtils.generateKeyPair(CSP_NAME, "DH", 256);
    assertThat(dhKeyPair0).isNotNull();
    assertThat(dhKeyPair0.getPrivate()).isNotNull();
    assertThat(dhKeyPair0.getPublic()).isNotNull();

    //second key pair with algorithm parameter specs
    AlgorithmParameters dhAlgorithmParams = AlgorithmParameterUtils.generateAlgParams(
        CSP_NAME, "DH");
    KeyPair dhKeyPair1 = KeyPairUtils.generateKeyPair(CSP_NAME, "DH",
        dhAlgorithmParams.getParameterSpec(DHParameterSpec.class));
    assertThat(dhKeyPair1).isNotNull();
    assertThat(dhKeyPair1.getPrivate()).isNotNull();
    assertThat(dhKeyPair1.getPublic()).isNotNull();

    // with spec & secure random
    DHParameterSpec spec = dhAlgorithmParams.getParameterSpec(DHParameterSpec.class);
    KeyPair dhKeyPair2 = KeyPairUtils.generateKeyPair(CSP_NAME, "DH", spec, new SecureRandom());
    assertThat(dhKeyPair2).isNotNull();
    assertThat(dhKeyPair2.getPrivate()).isNotNull();
    assertThat(dhKeyPair2.getPublic()).isNotNull();
  }


  @Test
  void generatePrivateKey() throws GeneralSecurityException {
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

    assertThat(KeyPairUtils.generatePrivateKey(CSP_NAME, "RSA", privateKeyEncodedSpec)).isNotNull();
  }

  @Test
  void generatePublicKey() throws GeneralSecurityException {
    X509EncodedKeySpec publicKeyEncodedSpec = new X509EncodedKeySpec(
        Base64.decode("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAw9w9gsya8eQyIDoG8JZh\n"
            + "FBPPdD92pkhKsKZX4lXOPJJ3eCpA0jCRzJ4ooS0F/oizp/7ND82Aq/rZf9AK/Pem\n"
            + "2cCL0hr5PmPO0Z3tsb6cVl7tg5u+J5Tj1rXVnPNsU1OlPqa6UVwKyhny6AgvvHMb\n"
            + "L6M76UL/YM14EpnExNEeT4TR/IjaF7owy0I7ORwUreNb2DdZFAPdnzurcQj3zaVW\n"
            + "fhmcKgPfobdwNjFC7O/sUJcim4UjR3y1BnOSnsgfPd1f87EoetZVZ/igE2erT/EW\n"
            + "QeoI9/+2ufn3CWhgnxS2burFMwEiO0On+lVOGLj+0gcgyRh2z7qjxcrcH/B2wjlj\n"
            + "TwIDAQAB")
    );
    assertThat(KeyPairUtils.generatePublicKey(CSP_NAME, "RSA", publicKeyEncodedSpec)).isNotNull();
  }


  @Test
  void wrapAESKeyWithRSA() throws GeneralSecurityException {
    //wrapping aes key with rsa cipher + public key
    KeyPair rsaKeyPair = KeyPairUtils.generateKeyPair(CSP_NAME, "RSA", 2048);
    SecretKey aesKey = SecretKeyUtils.generateSecretKey(CSP_NAME, "AES");
    String transformation = "RSA/NONE/OAEPwithSHA256andMGF1Padding";

    assertThat(
        KeyPairUtils.wrapKey(CSP_NAME, transformation, rsaKeyPair.getPublic(), aesKey))
        .isNotNull().hasSizeGreaterThan(0);
  }

  @Test
  void unwrapAESKeyWithRSA() throws GeneralSecurityException {
    //wrapping aes key with rsa cipher + public key
    KeyPair rsaKeyPair = KeyPairUtils.generateKeyPair(CSP_NAME, "RSA", 2048);
    SecretKey aesKey = SecretKeyUtils.generateSecretKey(CSP_NAME, "AES");
    String transformation = "RSA/NONE/OAEPwithSHA256andMGF1Padding";

    byte[] wrappedKey = KeyPairUtils.wrapKey(CSP_NAME, transformation,
        rsaKeyPair.getPublic(), aesKey);

    Key key = KeyPairUtils.unwrapKey(CSP_NAME, transformation, rsaKeyPair.getPrivate(), wrappedKey,
        "AES", Cipher.SECRET_KEY);

    assertThat(key.getAlgorithm()).isEqualTo(aesKey.getAlgorithm());
    assertThat(key.getEncoded()).containsExactly(aesKey.getEncoded());
  }

  @Test
  void unwrapAESKeyElGamal() throws GeneralSecurityException {
    KeyPair dhKeyPair = KeyPairUtils.generateKeyPair(CSP_NAME, "DH", 2048);
    SecretKey aesKey = SecretKeyUtils.generateSecretKey(CSP_NAME, "AES");
    String transformation = "ElGamal/NONE/OAEPwithSHA256andMGF1Padding";

    byte[] wrappedKey = KeyPairUtils.wrapKey(CSP_NAME, transformation,
        dhKeyPair.getPublic(), aesKey);

    Key key = KeyPairUtils.unwrapKey(CSP_NAME, transformation,
        dhKeyPair.getPrivate(), wrappedKey, "AES",
        Cipher.SECRET_KEY);

    assertThat(key.getAlgorithm()).isEqualTo(aesKey.getAlgorithm());
    assertThat(key.getEncoded()).containsExactly(aesKey.getEncoded());
  }
}