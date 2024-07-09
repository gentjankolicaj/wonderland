package io.wonderland.crypto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
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
class SignatureHelperTest extends AbstractTest {


  @Test
  void constructors() throws NoSuchAlgorithmException, NoSuchProviderException {
    String algorithm = "SHA256WithDSA";
    SignatureHelper signatureHelper = new SignatureHelper(algorithm);
    assertThat(signatureHelper).isNotNull();
    assertThat(signatureHelper.getSignature()).isNotNull();
    assertThat(signatureHelper.getAlgorithm()).isEqualTo(algorithm);
    assertThat(signatureHelper.getProvider()).isEqualTo(CSP.INSTANCE_CONTEXT.getProvider());

    SignatureHelper signatureHelper1 = new SignatureHelper(CSP.SUN, algorithm);
    assertThat(signatureHelper1).isNotNull();
    assertThat(signatureHelper1.getSignature()).isNotNull();
    assertThat(signatureHelper1.getAlgorithm()).isEqualTo(algorithm);
    assertThat(signatureHelper1.getProvider()).isEqualTo(CSP.SUN);

  }


  @Test
  void createSignature() throws GeneralSecurityException {
    KeyPair dsaKeyPair = KeyPairUtils.generateKeyPair("DSA", 2048);
    SignatureHelper signatureHelper = new SignatureHelper(CSP_NAME, "SHA256WithDSA");
    assertThatCode(
        () -> signatureHelper.createSignature(dsaKeyPair.getPrivate())).doesNotThrowAnyException();
    assertThat(signatureHelper.createSignature(dsaKeyPair.getPrivate()))
        .isNotNull()
        .hasSizeGreaterThan(32);
  }

  @Test
  void sign() throws GeneralSecurityException {
    String input = "Hello world223Test023john2043{}{qre|'/.,~wq~!@#$(*)-=-+_";
    KeyPair dsaKeyPair = KeyPairUtils.generateKeyPair("DSA", 2048);
    SignatureHelper signatureHelper = new SignatureHelper(CSP_NAME, "SHA256WithDSA");
    assertThatCode(
        () -> signatureHelper.sign(dsaKeyPair.getPrivate(),
            input.getBytes())).doesNotThrowAnyException();
    assertThat(signatureHelper.sign(dsaKeyPair.getPrivate(),
        input.getBytes())).isNotNull()
        .hasSizeGreaterThan(32);
  }

  @Test
  void verifySign() throws GeneralSecurityException {
    String input = "Hello world223Test023john2043{}{qre|'/.,~wq~!@#$(*)-=-+_";
    KeyPair dsaKeyPair = KeyPairUtils.generateKeyPair("DSA", 2048);
    SignatureHelper signatureHelper = new SignatureHelper(CSP_NAME, "SHA256WithDSA");
    byte[] dsaSignedInput = signatureHelper.sign(dsaKeyPair.getPrivate(), input.getBytes());
    assertThat(signatureHelper.verifySign(dsaKeyPair.getPublic(), input.getBytes(),
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

    SignatureHelper signatureHelper = new SignatureHelper(CSP_NAME, "SHA256WithRSA");

    //sign with a private key
    byte[] rsaSignedInput = signatureHelper.sign(optional.get(), input.getBytes());

    //assert with x509certificate
    assertThat(
        signatureHelper.verifySign(x509Certificate, input.getBytes(), rsaSignedInput)).isTrue();
  }

  @Test
  void verifySignDSTU4145() throws GeneralSecurityException {
    String input = "Hello world223Test023john2043{}{qre|'/.,~wq~!@#$(*)-=-+_";
    KeyPair dstu4145KeyPair = KeyPairUtils.generateKeyPair("DSTU4145",
        new ECGenParameterSpec("1.2.804.2.1.1.1.1.3.1.1.2.3"));

    SignatureHelper signatureHelper = new SignatureHelper(CSP_NAME, "DSTU4145");
    byte[] signedInput = signatureHelper.sign(dstu4145KeyPair.getPrivate(), input.getBytes());
    assertThat(
        signatureHelper.verifySign(dstu4145KeyPair.getPublic(), input.getBytes(), signedInput))
        .isTrue();
  }

  @Test
  void verifySignECDSA() throws GeneralSecurityException {
    String input = "Hello world223Test023john2043{}{qre|'/.,~wq~!@#$(*)-=-+_";
    KeyPair ecKeyPair = KeyPairUtils.generateKeyPair("EC", 256);
    SignatureHelper signatureHelper = new SignatureHelper(CSP_NAME, "SHA256WithECDSA");
    byte[] signedInput = signatureHelper.sign(ecKeyPair.getPrivate(), input.getBytes());

    assertThat(
        signatureHelper.verifySign(ecKeyPair.getPublic(), input.getBytes(), signedInput)).isTrue();
  }

  @Test
  void verifySignECDDSA() throws GeneralSecurityException {
    String input = "Hello world223Test023john2043{}{qre|'/.,~wq~!@#$(*)-=-+_";
    KeyPair ecKeyPair = KeyPairUtils.generateKeyPair("EC", 256);
    SignatureHelper signatureHelper = new SignatureHelper(CSP_NAME, "SHA256WithECDDSA");
    byte[] signedInput = signatureHelper.sign(ecKeyPair.getPrivate(), input.getBytes());
    assertThat(
        signatureHelper.verifySign(ecKeyPair.getPublic(), input.getBytes(), signedInput)).isTrue();
  }

  @Test
  void verifySignECP256() throws GeneralSecurityException {
    String input = "Hello world223Test023john2043{}{qre|'/.,~wq~!@#$(*)-=-+_";
    KeyPair ecp256KeyPair = KeyPairUtils.generateKeyPair("EC", new ECGenParameterSpec("P-256"));
    SignatureHelper signatureHelper = new SignatureHelper(CSP_NAME, "SHA256WithECDSA");
    byte[] signedInput = signatureHelper.sign(ecp256KeyPair.getPrivate(), input.getBytes());

    assertThat(
        signatureHelper.verifySign(ecp256KeyPair.getPublic(), input.getBytes(),
            signedInput)).isTrue();
  }

  @Test
  void verifySignED448DSA() throws GeneralSecurityException {
    String input = "Hello world223Test023john2043{}{qre|'/.,~wq~!@#$(*)-=-+_";
    KeyPair ed448KeyPair = KeyPairUtils.generateKeyPair("Ed448");
    SignatureHelper signatureHelper = new SignatureHelper(CSP_NAME, "EdDSA");
    byte[] signedInput = signatureHelper.sign(ed448KeyPair.getPrivate(), input.getBytes());
    assertThat(
        signatureHelper.verifySign(ed448KeyPair.getPublic(), input.getBytes(),
            signedInput)).isTrue();
  }

  @Test
  void verifySignECGOST3410_2012() throws GeneralSecurityException {
    String input = "Hello world223Test023john2043{}{qre|'/.,~wq~!@#$(*)-=-+_";
    KeyPair gostKeyPair = KeyPairUtils.generateKeyPair("ECGOST3410-2012",
        new ECGenParameterSpec("Tc26-Gost-3410-12-512-paramSetA"));
    SignatureHelper signatureHelper = new SignatureHelper(CSP_NAME, "ECGOST3410-2012-512");
    byte[] signedInput = signatureHelper.sign(gostKeyPair.getPrivate(), input.getBytes());
    assertThat(
        signatureHelper.verifySign(gostKeyPair.getPublic(), input.getBytes(),
            signedInput)).isTrue();
  }

  @Test
  void verifySignRSAPSS() throws GeneralSecurityException {
    String input = "Hello world223Test023john2043{}{qre|'/.,~wq~!@#$(*)-=-+_";
    KeyPair rsaKeyPair = KeyPairUtils.generateKeyPair("RSA", 2048);
    SignatureHelper signatureHelper = new SignatureHelper(CSP_NAME, "SHA256withRSAandMGF1");
    byte[] signedInput = signatureHelper.sign(rsaKeyPair.getPrivate(), input.getBytes());
    assertThat(
        signatureHelper.verifySign(rsaKeyPair.getPublic(), input.getBytes(), signedInput)).isTrue();
  }

  @Test
  void verifySignECSM2() throws GeneralSecurityException {
    String input = "Hello world223Test023john2043{}{qre|'/.,~wq~!@#$(*)-=-+_";
    KeyPair ecsm2KeyPair = KeyPairUtils.generateKeyPair("EC", new ECGenParameterSpec("sm2p256v1"));
    SM2ParameterSpec sm2ParameterSpec = new SM2ParameterSpec("Signer@ID_STRING".getBytes());
    SignatureHelper signatureHelper = new SignatureHelper(CSP_NAME, "SM3withSM2");
    byte[] signedInput = signatureHelper.sign(ecsm2KeyPair.getPrivate(), sm2ParameterSpec,
        input.getBytes());

    assertThat(
        signatureHelper.verifySign(ecsm2KeyPair.getPublic(), sm2ParameterSpec, input.getBytes(),
            signedInput)).isTrue();
  }

  @Test
  void verifySignWithAlgorithmParameterSpecRSAPSS() throws GeneralSecurityException {
    String input = "Hello world223Test023john2043{}{qre|'/.,~wq~!@#$(*)-=-+_";
    KeyPair rsaKeyPair = KeyPairUtils.generateKeyPair("RSA", 2048);
    PSSParameterSpec pssParameterSpec = new PSSParameterSpec("SHA-256", "MGF1",
        new MGF1ParameterSpec("SHA-256"), 32, 1);
    SignatureHelper signatureHelper = new SignatureHelper(CSP_NAME, "RSAPSS");
    byte[] signedInput = signatureHelper.sign(rsaKeyPair.getPrivate(), pssParameterSpec,
        input.getBytes());
    assertThat(
        signatureHelper.verifySign(rsaKeyPair.getPublic(), pssParameterSpec, input.getBytes(),
            signedInput)).isTrue();
  }


}