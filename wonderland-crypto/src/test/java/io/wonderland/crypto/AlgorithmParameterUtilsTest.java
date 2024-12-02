package io.wonderland.crypto;

import static org.assertj.core.api.Assertions.assertThat;

import java.security.AlgorithmParameterGenerator;
import java.security.AlgorithmParameters;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.spec.DSAParameterSpec;
import javax.crypto.SecretKey;
import javax.crypto.spec.DHParameterSpec;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Slf4j
class AlgorithmParameterUtilsTest extends AbstractTest {

  String aes = "AES";

  @Test
  void generateIvParamSpec() throws GeneralSecurityException {
    assertThat(
        AlgorithmParameterUtils.generateIvParamSpec(CSP.SUN, "SHA1PRNG", 16)).isNotNull();
  }

  @Test
  void getAlgParamGen() throws GeneralSecurityException {
    AlgorithmParameterGenerator algorithmParamGen = AlgorithmParameterUtils.getAlgParamGen(
        CSP_NAME, "GCM");
    AlgorithmParameters algParamsGCM = algorithmParamGen.generateParameters();

    String input = "Hello world223Test023john2043{}{qre|'/.,~wq~!@#$(*)-=-+_";
    SecretKey secretKey = SecretKeyUtils.generateSecretKey(CSP_NAME, aes);

    SymmetricCipher symmetricCrypto = new SymmetricCipher(CSP_NAME, "AES/GCM/NoPadding",
        secretKey,
        algParamsGCM);
    byte[] encryptedInput0 = symmetricCrypto.encrypt(input.getBytes());
    byte[] decryptedInput0 = symmetricCrypto.decrypt(encryptedInput0);
    assertThat(decryptedInput0).containsExactly(input.getBytes());
  }

  @Test
  void generateAlgParams() throws GeneralSecurityException {
    AlgorithmParameters algorithmParameters =
        AlgorithmParameterUtils.generateAlgParams(CSP_NAME, "CCM");

    String input = "Hello world223Test023john2043{}{qre|'/.,~wq~!@#$(*)-=-+_";
    SecretKey secretKey = SecretKeyUtils.generateSecretKey(CSP_NAME, aes);

    SymmetricCipher symmetricCrypto = new SymmetricCipher(CSP_NAME, "AES/CCM/NoPadding",
        secretKey,
        algorithmParameters);
    byte[] encryptedInput0 = symmetricCrypto.encrypt(input.getBytes());
    byte[] decryptedInput0 = symmetricCrypto.decrypt(encryptedInput0);
    assertThat(decryptedInput0).containsExactly(input.getBytes());
  }

  @Test
  void generateAlgParamsDSA() throws GeneralSecurityException {
    AlgorithmParameters params =
        AlgorithmParameterUtils.generateAlgParams(CSP_NAME, "DSA", 1024);

    DSAParameterSpec spec = params.getParameterSpec(DSAParameterSpec.class);
    assertThat(spec).isNotNull();
    assertThat(spec.getG()).isNotNull();
    assertThat(spec.getP()).isNotNull();
    assertThat(spec.getQ()).isNotNull();
  }

  @Test
  void generateAlgParamSpecDSA() throws GeneralSecurityException {
    DSAParameterSpec spec =
        AlgorithmParameterUtils.generateAlgParamSpec(CSP_NAME, "DSA", 1024,
            DSAParameterSpec.class);

    assertThat(spec).isNotNull();
    assertThat(spec.getG()).isNotNull();
    assertThat(spec.getP()).isNotNull();
    assertThat(spec.getQ()).isNotNull();
  }

  @Test
  void getAlgParamsWithKeySize() throws GeneralSecurityException {
    AlgorithmParameterGenerator algorithmParamGen = AlgorithmParameterUtils.getAlgParamGen(
        CSP_NAME, "GCM", 256);
    AlgorithmParameters algParamsGCM = algorithmParamGen.generateParameters();

    String input = "Hello world223Test023john2043{}{qre|'/.,~wq~!@#$(*)-=-+_";
    SecretKey secretKey = SecretKeyUtils.generateSecretKey(CSP_NAME, aes);

    SymmetricCipher symmetricCrypto = new SymmetricCipher(CSP_NAME, "AES/GCM/NoPadding",
        secretKey,
        algParamsGCM);
    byte[] encryptedInput0 = symmetricCrypto.encrypt(input.getBytes());
    byte[] decryptedInput0 = symmetricCrypto.decrypt(encryptedInput0);
    assertThat(decryptedInput0).containsExactly(input.getBytes());
  }

  @Test
  void generateAlgParamsWithKeySize() throws GeneralSecurityException {
    AlgorithmParameters algParamsGCM = AlgorithmParameterUtils.generateAlgParams(CSP_NAME,
        "GCM",
        256);

    String input = "Hello world223Test023john2043{}{qre|'/.,~wq~!@#$(*)-=-+_";
    SecretKey secretKey = SecretKeyUtils.generateSecretKey(CSP_NAME, aes);

    SymmetricCipher symmetricCrypto = new SymmetricCipher(CSP_NAME, "AES/GCM/NoPadding",
        secretKey,
        algParamsGCM);
    byte[] encryptedInput0 = symmetricCrypto.encrypt(input.getBytes());
    byte[] decryptedInput0 = symmetricCrypto.decrypt(encryptedInput0);
    assertThat(decryptedInput0).containsExactly(input.getBytes());
  }

  @Test
  @Disabled("Disabled because of performance")
  void generateAlgParamSpec() throws GeneralSecurityException {
    DHParameterSpec dhParameterSpec = AlgorithmParameterUtils.generateAlgParamSpec(
        CSP_NAME, "DH", DHParameterSpec.class);
    KeyPair dhKeyPair = KeyPairUtils.generateKeyPair(CSP_NAME, "DH", dhParameterSpec);
    assertThat(dhKeyPair).isNotNull();
    assertThat(dhKeyPair.getPrivate()).isNotNull();
    assertThat(dhKeyPair.getPublic()).isNotNull();
  }

  @Test
  void generateAlgParamSpecWithKeySize() throws GeneralSecurityException {
    DHParameterSpec dhParameterSpec = AlgorithmParameterUtils.generateAlgParamSpec(
        CSP_NAME, "DH", 256, DHParameterSpec.class);
    KeyPair dhKeyPair = KeyPairUtils.generateKeyPair(CSP_NAME, "DH", dhParameterSpec);
    assertThat(dhKeyPair).isNotNull();
    assertThat(dhKeyPair.getPrivate()).isNotNull();
    assertThat(dhKeyPair.getPublic()).isNotNull();
  }

}