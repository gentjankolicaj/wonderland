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
class AlgorithmParameterHelperTest extends AbstractTest {


  @Test
  void constructors() {
    AlgorithmParameterHelper algorithmParameterHelper = new AlgorithmParameterHelper();
    assertThat(algorithmParameterHelper.getProvider()).isEqualTo(
        CSP.INSTANCE_CONTEXT.getProvider());

    algorithmParameterHelper = new AlgorithmParameterHelper(CSP.SunJCE);
    assertThat(algorithmParameterHelper.getProvider()).isEqualTo(CSP.SunJCE);
  }


  @Test
  void generateIvParameterSpec() throws GeneralSecurityException {
    AlgorithmParameterHelper algParamHelper = new AlgorithmParameterHelper(CSP.SUN);
    assertThat(algParamHelper.generateIvParameterSpec("SHA1PRNG", 16)).isNotNull();
  }

  @Test
  void getAlgorithmParamGen() throws GeneralSecurityException {
    AlgorithmParameterHelper algParamHelper = new AlgorithmParameterHelper();
    AlgorithmParameterGenerator algorithmParamGen = algParamHelper.getAlgorithmParamGen("GCM");
    AlgorithmParameters algParamsGCM = algorithmParamGen.generateParameters();

    String input = "Hello world223Test023john2043{}{qre|'/.,~wq~!@#$(*)-=-+_";
    SecretKey secretKey = SecretKeyUtils.generateSecretKey("AES");

    SymmetricCipher symmetricCrypto = new SymmetricCipher(CSP_NAME, "AES/GCM/NoPadding"
        , secretKey, algParamsGCM);
    byte[] encryptedInput0 = symmetricCrypto.encrypt(input.getBytes());
    byte[] decryptedInput0 = symmetricCrypto.decrypt(encryptedInput0);
    assertThat(decryptedInput0).containsExactly(input.getBytes());
  }

  @Test
  void generateAlgorithmParameters() throws GeneralSecurityException {
    AlgorithmParameterHelper algParamHelper = new AlgorithmParameterHelper();
    AlgorithmParameters algorithmParameters = algParamHelper.generateAlgorithmParameters("CCM");

    String input = "Hello world223Test023john2043{}{qre|'/.,~wq~!@#$(*)-=-+_";
    SecretKey secretKey = SecretKeyUtils.generateSecretKey("AES");

    SymmetricCipher symmetricCrypto = new SymmetricCipher(CSP_NAME, "AES/CCM/NoPadding"
        , secretKey, algorithmParameters);
    byte[] encryptedInput0 = symmetricCrypto.encrypt(input.getBytes());
    byte[] decryptedInput0 = symmetricCrypto.decrypt(encryptedInput0);
    assertThat(decryptedInput0).containsExactly(input.getBytes());
  }

  @Test
  void getAlgorithmParameterWithKeySize() throws GeneralSecurityException {
    AlgorithmParameterHelper algParamHelper = new AlgorithmParameterHelper();
    AlgorithmParameterGenerator algorithmParamGen = algParamHelper.getAlgorithmParamGen(
        "GCM", 256);
    AlgorithmParameters algParamsGCM = algorithmParamGen.generateParameters();

    String input = "Hello world223Test023john2043{}{qre|'/.,~wq~!@#$(*)-=-+_";
    SecretKey secretKey = SecretKeyUtils.generateSecretKey("AES");

    SymmetricCipher symmetricCrypto = new SymmetricCipher(CSP_NAME, "AES/GCM/NoPadding"
        , secretKey, algParamsGCM);
    byte[] encryptedInput0 = symmetricCrypto.encrypt(input.getBytes());
    byte[] decryptedInput0 = symmetricCrypto.decrypt(encryptedInput0);
    assertThat(decryptedInput0).containsExactly(input.getBytes());
  }

  @Test
  void generateAlgorithmParametersWithKeySize() throws GeneralSecurityException {
    AlgorithmParameterHelper algParamHelper = new AlgorithmParameterHelper();
    AlgorithmParameters algParamsGCM = algParamHelper.generateAlgorithmParameters("GCM",
        256);

    String input = "Hello world223Test023john2043{}{qre|'/.,~wq~!@#$(*)-=-+_";
    SecretKey secretKey = SecretKeyUtils.generateSecretKey("AES");

    SymmetricCipher symmetricCrypto = new SymmetricCipher(CSP_NAME, "AES/GCM/NoPadding"
        , secretKey, algParamsGCM);
    byte[] encryptedInput0 = symmetricCrypto.encrypt(input.getBytes());
    byte[] decryptedInput0 = symmetricCrypto.decrypt(encryptedInput0);
    assertThat(decryptedInput0).containsExactly(input.getBytes());

  }

  @Test
  @Disabled("Disabled because of performance")
  void generateAlgorithmParameterSpec() throws GeneralSecurityException {
    AlgorithmParameterHelper algParamHelper = new AlgorithmParameterHelper();
    DHParameterSpec dhParameterSpec = algParamHelper.generateAlgorithmParameterSpec("DH",
        DHParameterSpec.class);
    KeyPair dhKeyPair = KeyPairUtils.generateKeyPair("DH", dhParameterSpec);
    assertThat(dhKeyPair).isNotNull();
    assertThat(dhKeyPair.getPrivate()).isNotNull();
    assertThat(dhKeyPair.getPublic()).isNotNull();
  }

  @Test
  void generateAlgorithmParameterSpecWithKeySize() throws GeneralSecurityException {
    AlgorithmParameterHelper algParamHelper = new AlgorithmParameterHelper();
    DHParameterSpec dhParameterSpec = algParamHelper.generateAlgorithmParameterSpec("DH",
        256,
        DHParameterSpec.class);
    KeyPair dhKeyPair = KeyPairUtils.generateKeyPair("DH", dhParameterSpec);
    assertThat(dhKeyPair).isNotNull();
    assertThat(dhKeyPair.getPrivate()).isNotNull();
    assertThat(dhKeyPair.getPublic()).isNotNull();
  }

}