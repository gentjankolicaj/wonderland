package io.wonderland.crypto;

import static org.assertj.core.api.Assertions.assertThat;

import java.security.AlgorithmParameterGenerator;
import java.security.AlgorithmParameters;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import javax.crypto.SecretKey;
import javax.crypto.spec.DHParameterSpec;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Slf4j
class AlgorithmParameterUtilsTest extends AbstractTest {

  String aes = "AES";

  @Test
  void generateIvParameterSpec() throws GeneralSecurityException {
    assertThat(
        AlgorithmParameterUtils.generateIvParameterSpec(CSP.SUN, "SHA1PRNG", 16)).isNotNull();
  }

  @Test
  void getAlgorithmParamGen() throws GeneralSecurityException {
    AlgorithmParameterGenerator algorithmParamGen = AlgorithmParameterUtils.getAlgorithmParamGen(
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
  void generateAlgorithmParameters() throws GeneralSecurityException {
    AlgorithmParameters algorithmParameters =
        AlgorithmParameterUtils.generateAlgorithmParameters(CSP_NAME, "CCM");

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
  void getAlgorithmParameterWithKeySize() throws GeneralSecurityException {
    AlgorithmParameterGenerator algorithmParamGen = AlgorithmParameterUtils.getAlgorithmParamGen(
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
  void generateAlgorithmParametersWithKeySize() throws GeneralSecurityException {
    AlgorithmParameters algParamsGCM = AlgorithmParameterUtils.generateAlgorithmParameters(CSP_NAME,
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
  void generateAlgorithmParameterSpec() throws GeneralSecurityException {
    DHParameterSpec dhParameterSpec = AlgorithmParameterUtils.generateAlgorithmParameterSpec(
        CSP_NAME, "DH", DHParameterSpec.class);
    KeyPair dhKeyPair = KeyPairUtils.generateKeyPair("DH", dhParameterSpec);
    assertThat(dhKeyPair).isNotNull();
    assertThat(dhKeyPair.getPrivate()).isNotNull();
    assertThat(dhKeyPair.getPublic()).isNotNull();
  }

  @Test
  void generateAlgorithmParameterSpecWithKeySize() throws GeneralSecurityException {
    DHParameterSpec dhParameterSpec = AlgorithmParameterUtils.generateAlgorithmParameterSpec(
        CSP_NAME, "DH", 256, DHParameterSpec.class);
    KeyPair dhKeyPair = KeyPairUtils.generateKeyPair("DH", dhParameterSpec);
    assertThat(dhKeyPair).isNotNull();
    assertThat(dhKeyPair.getPrivate()).isNotNull();
    assertThat(dhKeyPair.getPublic()).isNotNull();
  }

}