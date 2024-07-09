package io.wonderland.crypto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.PSSParameterSpec;
import java.util.Objects;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jcajce.spec.SM2ParameterSpec;
import org.junit.jupiter.api.Test;

@Slf4j
class SignatureUtilsTest extends AbstractTest {


  @Test
  void createSignature() throws GeneralSecurityException {
    KeyPair dsaKeyPair = KeyPairUtils.generateKeyPair(CSP_NAME, "DSA", 2048);
    assertThatCode(
        () -> SignatureUtils.createSignature(CSP_NAME, "SHA256WithDSA",
            dsaKeyPair.getPrivate())).doesNotThrowAnyException();
    assertThat(
        SignatureUtils.createSignature(CSP_NAME, "SHA256WithDSA",
            dsaKeyPair.getPrivate())).isNotNull()
        .hasSizeGreaterThan(32);
  }

  @Test
  void sign() throws GeneralSecurityException {
    String input = "Hello world223Test023john2043{}{qre|'/.,~wq~!@#$(*)-=-+_";
    KeyPair dsaKeyPair = KeyPairUtils.generateKeyPair(CSP_NAME, "DSA", 2048);
    assertThatCode(
        () -> SignatureUtils.sign(CSP_NAME, "SHA256WithDSA", dsaKeyPair.getPrivate(),
            input.getBytes())).doesNotThrowAnyException();
    assertThat(SignatureUtils.sign(CSP_NAME, "SHA256WithDSA", dsaKeyPair.getPrivate(),
        input.getBytes())).isNotNull()
        .hasSizeGreaterThan(32);
  }

  @Test
  void verifySign() throws GeneralSecurityException {
    String input = "Hello world223Test023john2043{}{qre|'/.,~wq~!@#$(*)-=-+_";
    KeyPair dsaKeyPair = KeyPairUtils.generateKeyPair(CSP_NAME, "DSA", 2048);
    byte[] dsaSignedInput = SignatureUtils.sign(CSP_NAME, "SHA256WithDSA",
        dsaKeyPair.getPrivate(), input.getBytes());
    assertThat(
        SignatureUtils.verifySign(CSP_NAME, "SHA256WithDSA", dsaKeyPair.getPublic(),
            input.getBytes(),
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
    Optional<PrivateKey> optional = KeyPairUtils.loadPrivateKeyPem(privateKeyPem);

    //load x509cert from resources
    Path certPath = Path.of(
        Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("bc.cer"))
            .getPath());
    X509Certificate x509Certificate = KeyPairUtils.loadX509Certificate(certPath);

    //sign with a private key
    byte[] rsaSignedInput = SignatureUtils.sign(CSP_NAME, "SHA256WithRSA", optional.get(),
        input.getBytes());

    //assert with x509certificate
    assertThat(
        SignatureUtils.verifySign(CSP_NAME, "SHA256WithRSA", x509Certificate, input.getBytes(),
            rsaSignedInput)).isTrue();
  }

  @Test
  void verifySignDSTU4145() throws GeneralSecurityException {
    String input = "Hello world223Test023john2043{}{qre|'/.,~wq~!@#$(*)-=-+_";
    KeyPair dstu4145KeyPair = KeyPairUtils.generateKeyPair(CSP_NAME, "DSTU4145",
        new ECGenParameterSpec("1.2.804.2.1.1.1.1.3.1.1.2.3"));
    byte[] signedInput = SignatureUtils.sign(CSP_NAME, "DSTU4145", dstu4145KeyPair.getPrivate(),
        input.getBytes());
    assertThat(
        SignatureUtils.verifySign(CSP_NAME, "DSTU4145", dstu4145KeyPair.getPublic(),
            input.getBytes(),
            signedInput)).isTrue();
  }

  @Test
  void verifySignECDSA() throws GeneralSecurityException {
    String input = "Hello world223Test023john2043{}{qre|'/.,~wq~!@#$(*)-=-+_";
    KeyPair ecKeyPair = KeyPairUtils.generateKeyPair(CSP_NAME, "EC", 256);
    byte[] signedInput = SignatureUtils.sign(CSP_NAME, "SHA256withECDSA", ecKeyPair.getPrivate(),
        input.getBytes());
    assertThat(
        SignatureUtils.verifySign(CSP_NAME, "SHA256withECDSA", ecKeyPair.getPublic(),
            input.getBytes(),
            signedInput)).isTrue();
  }

  @Test
  void verifySignECDDSA() throws GeneralSecurityException {
    String input = "Hello world223Test023john2043{}{qre|'/.,~wq~!@#$(*)-=-+_";
    KeyPair ecKeyPair = KeyPairUtils.generateKeyPair(CSP_NAME, "EC", 256);
    byte[] signedInput = SignatureUtils.sign(CSP_NAME, "SHA256withECDDSA", ecKeyPair.getPrivate(),
        input.getBytes());
    assertThat(
        SignatureUtils.verifySign(CSP_NAME, "SHA256withECDDSA", ecKeyPair.getPublic(),
            input.getBytes(),
            signedInput)).isTrue();
  }

  @Test
  void verifySignECP256() throws GeneralSecurityException {
    String input = "Hello world223Test023john2043{}{qre|'/.,~wq~!@#$(*)-=-+_";
    KeyPair ecp256KeyPair = KeyPairUtils.generateKeyPair(CSP_NAME, "EC",
        new ECGenParameterSpec("P-256"));
    byte[] signedInput = SignatureUtils.sign(CSP_NAME, "SHA256withECDSA",
        ecp256KeyPair.getPrivate(),
        input.getBytes());
    assertThat(
        SignatureUtils.verifySign(CSP_NAME, "SHA256withECDSA", ecp256KeyPair.getPublic(),
            input.getBytes(),
            signedInput)).isTrue();
  }

  @Test
  void verifySignED448DSA() throws GeneralSecurityException {
    String input = "Hello world223Test023john2043{}{qre|'/.,~wq~!@#$(*)-=-+_";
    KeyPair ed448KeyPair = KeyPairUtils.generateKeyPair(CSP_NAME, "Ed448");
    byte[] signedInput = SignatureUtils.sign(CSP_NAME, "EdDSA", ed448KeyPair.getPrivate(),
        input.getBytes());
    assertThat(
        SignatureUtils.verifySign(CSP_NAME, "EdDSA", ed448KeyPair.getPublic(), input.getBytes(),
            signedInput)).isTrue();
  }

  @Test
  void verifySignECGOST3410_2012() throws GeneralSecurityException {
    String input = "Hello world223Test023john2043{}{qre|'/.,~wq~!@#$(*)-=-+_";
    KeyPair gostKeyPair = KeyPairUtils.generateKeyPair(CSP_NAME, "ECGOST3410-2012",
        new ECGenParameterSpec("Tc26-Gost-3410-12-512-paramSetA"));
    byte[] signedInput = SignatureUtils.sign(CSP_NAME, "ECGOST3410-2012-512",
        gostKeyPair.getPrivate(),
        input.getBytes());
    assertThat(
        SignatureUtils.verifySign(CSP_NAME, "ECGOST3410-2012-512", gostKeyPair.getPublic(),
            input.getBytes(),
            signedInput)).isTrue();
  }

  @Test
  void verifySignRSAPSS() throws GeneralSecurityException {
    String input = "Hello world223Test023john2043{}{qre|'/.,~wq~!@#$(*)-=-+_";
    KeyPair rsaKeyPair = KeyPairUtils.generateKeyPair(CSP_NAME, "RSA", 2048);
    byte[] signedInput = SignatureUtils.sign(CSP_NAME, "SHA256withRSAandMGF1",
        rsaKeyPair.getPrivate(),
        input.getBytes());
    assertThat(
        SignatureUtils.verifySign(CSP_NAME, "SHA256withRSAandMGF1", rsaKeyPair.getPublic(),
            input.getBytes(),
            signedInput)).isTrue();
  }

  @Test
  void verifySignECSM2() throws GeneralSecurityException {
    String input = "Hello world223Test023john2043{}{qre|'/.,~wq~!@#$(*)-=-+_";
    KeyPair ecsm2KeyPair = KeyPairUtils.generateKeyPair(CSP_NAME, "EC",
        new ECGenParameterSpec("sm2p256v1"));
    SM2ParameterSpec sm2ParameterSpec = new SM2ParameterSpec("Signer@ID_STRING".getBytes());
    byte[] signedInput = SignatureUtils.sign(CSP_NAME, "SM3withSM2", ecsm2KeyPair.getPrivate(),
        sm2ParameterSpec,
        input.getBytes());
    assertThat(
        SignatureUtils.verifySign(CSP_NAME, "SM3withSM2", ecsm2KeyPair.getPublic(),
            sm2ParameterSpec,
            input.getBytes(),
            signedInput)).isTrue();
  }

  @Test
  void verifySignWithAlgorithmParameterSpecRSAPSS() throws GeneralSecurityException {
    String input = "Hello world223Test023john2043{}{qre|'/.,~wq~!@#$(*)-=-+_";
    KeyPair rsaKeyPair = KeyPairUtils.generateKeyPair(CSP_NAME, "RSA", 2048);
    PSSParameterSpec pssParameterSpec = new PSSParameterSpec("SHA-256", "MGF1",
        new MGF1ParameterSpec("SHA-256"),
        32, 1);

    byte[] signedInput = SignatureUtils.sign(CSP_NAME, "RSAPSS", rsaKeyPair.getPrivate(),
        pssParameterSpec,
        input.getBytes());
    assertThat(
        SignatureUtils.verifySign(CSP_NAME, "RSAPSS", rsaKeyPair.getPublic(), pssParameterSpec,
            input.getBytes(),
            signedInput)).isTrue();
  }


}