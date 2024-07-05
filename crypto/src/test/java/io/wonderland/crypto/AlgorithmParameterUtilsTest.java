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
class AlgorithmParameterUtilsTest {

  static final String CSP = "BC";


  @Test
  void generateIvParameterSpec() throws GeneralSecurityException {
    assertThat(AlgorithmParameterUtils.generateIvParameterSpec("SHA1PRNG", 16)).isNotNull();
  }

  @Test
  void getAlgorithmParamGen() throws GeneralSecurityException {
    AlgorithmParameterGenerator algorithmParamGen = AlgorithmParameterUtils.getAlgorithmParamGen(
        "GCM");
    AlgorithmParameters algParamsGCM = algorithmParamGen.generateParameters();

    String input = "Hello world223Test023john2043{}{qre|'/.,~wq~!@#$(*)-=-+_";
    SecretKey secretKey = SecretKeyUtils.generateSecretKey("AES");

    SymmetricCrypto symmetricCrypto = new SymmetricCrypto(CSP, "AES/GCM/NoPadding", secretKey,
        algParamsGCM);
    byte[] encryptedInput0 = symmetricCrypto.encrypt(input.getBytes());
    byte[] decryptedInput0 = symmetricCrypto.decrypt(encryptedInput0);
    assertThat(decryptedInput0).containsExactly(input.getBytes());
    log.debug("GCM encryptedInput0 : {}", new String(encryptedInput0));
    log.debug("GCM decryptedInput0 : {}", new String(decryptedInput0));
  }

  @Test
  void generateAlgorithmParameters() throws GeneralSecurityException {
    AlgorithmParameters algorithmParameters = AlgorithmParameterUtils.generateAlgorithmParameters(
        "CCM");

    String input = "Hello world223Test023john2043{}{qre|'/.,~wq~!@#$(*)-=-+_";
    SecretKey secretKey = SecretKeyUtils.generateSecretKey("AES");

    SymmetricCrypto symmetricCrypto = new SymmetricCrypto(CSP, "AES/CCM/NoPadding", secretKey,
        algorithmParameters);
    byte[] encryptedInput0 = symmetricCrypto.encrypt(input.getBytes());
    byte[] decryptedInput0 = symmetricCrypto.decrypt(encryptedInput0);
    assertThat(decryptedInput0).containsExactly(input.getBytes());
    log.debug("GCM encryptedInput0 : {}", new String(encryptedInput0));
    log.debug("GCM decryptedInput0 : {}", new String(decryptedInput0));
  }

  @Test
  void getAlgorithmParameterWithKeySize() throws GeneralSecurityException {
    AlgorithmParameterGenerator algorithmParamGen = AlgorithmParameterUtils.getAlgorithmParamGen(
        "GCM", 256);
    AlgorithmParameters algParamsGCM = algorithmParamGen.generateParameters();

    String input = "Hello world223Test023john2043{}{qre|'/.,~wq~!@#$(*)-=-+_";
    SecretKey secretKey = SecretKeyUtils.generateSecretKey("AES");

    SymmetricCrypto symmetricCrypto = new SymmetricCrypto(CSP, "AES/GCM/NoPadding", secretKey,
        algParamsGCM);
    byte[] encryptedInput0 = symmetricCrypto.encrypt(input.getBytes());
    byte[] decryptedInput0 = symmetricCrypto.decrypt(encryptedInput0);
    assertThat(decryptedInput0).containsExactly(input.getBytes());
    log.debug("getAlgorithmParameterWithKeySize encryptedInput0 : {}", new String(encryptedInput0));
    log.debug("getAlgorithmParameterWithKeySize decryptedInput0 : {}", new String(decryptedInput0));
  }

  @Test
  void generateAlgorithmParametersWithKeySize() throws GeneralSecurityException {
    AlgorithmParameters algParamsGCM = AlgorithmParameterUtils.generateAlgorithmParameters("GCM",
        256);

    String input = "Hello world223Test023john2043{}{qre|'/.,~wq~!@#$(*)-=-+_";
    SecretKey secretKey = SecretKeyUtils.generateSecretKey("AES");

    SymmetricCrypto symmetricCrypto = new SymmetricCrypto(CSP, "AES/GCM/NoPadding", secretKey,
        algParamsGCM);
    byte[] encryptedInput0 = symmetricCrypto.encrypt(input.getBytes());
    byte[] decryptedInput0 = symmetricCrypto.decrypt(encryptedInput0);
    assertThat(decryptedInput0).containsExactly(input.getBytes());
    log.debug("getAlgorithmParameterWithKeySize encryptedInput0 : {}", new String(encryptedInput0));
    log.debug("getAlgorithmParameterWithKeySize decryptedInput0 : {}", new String(decryptedInput0));
  }

  @Test
  @Disabled("because of performance")
  void generateAlgorithmParameterSpec() throws GeneralSecurityException {
    DHParameterSpec dhParameterSpec = AlgorithmParameterUtils.generateAlgorithmParameterSpec("DH",
        DHParameterSpec.class);
    KeyPair dhKeyPair = KeyPairUtils.generateKeyPair("DH", dhParameterSpec);
    assertThat(dhKeyPair).isNotNull();
    assertThat(dhKeyPair.getPrivate()).isNotNull();
    assertThat(dhKeyPair.getPublic()).isNotNull();
  }

  @Test
  void generateAlgorithmParameterSpecWithKeySize() throws GeneralSecurityException {
    DHParameterSpec dhParameterSpec = AlgorithmParameterUtils.generateAlgorithmParameterSpec("DH",
        256,
        DHParameterSpec.class);
    KeyPair dhKeyPair = KeyPairUtils.generateKeyPair("DH", dhParameterSpec);
    assertThat(dhKeyPair).isNotNull();
    assertThat(dhKeyPair.getPrivate()).isNotNull();
    assertThat(dhKeyPair.getPublic()).isNotNull();
  }

}