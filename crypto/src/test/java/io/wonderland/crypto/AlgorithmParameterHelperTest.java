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
  void generateIvParamSpec() throws GeneralSecurityException {
    AlgorithmParameterHelper algParamHelper = new AlgorithmParameterHelper(CSP.SUN);
    assertThat(algParamHelper.generateIvParamSpec("SHA1PRNG", 16)).isNotNull();
  }

  @Test
  void getAlgParamGen() throws GeneralSecurityException {
    AlgorithmParameterHelper algParamHelper = new AlgorithmParameterHelper();
    AlgorithmParameterGenerator algorithmParamGen = algParamHelper.getAlgParamGen("GCM");
    AlgorithmParameters algParamsGCM = algorithmParamGen.generateParameters();

    String input = "Hello world223Test023john2043{}{qre|'/.,~wq~!@#$(*)-=-+_";
    SecretKey secretKey = SecretKeyUtils.generateSecretKey(CSP_NAME, "AES");

    SymmetricCipher symmetricCrypto = new SymmetricCipher(CSP_NAME, "AES/GCM/NoPadding"
        , secretKey, algParamsGCM);
    byte[] encryptedInput0 = symmetricCrypto.encrypt(input.getBytes());
    byte[] decryptedInput0 = symmetricCrypto.decrypt(encryptedInput0);
    assertThat(decryptedInput0).containsExactly(input.getBytes());
  }

  @Test
  void generateAlgParams() throws GeneralSecurityException {
    AlgorithmParameterHelper algParamHelper = new AlgorithmParameterHelper();
    AlgorithmParameters algorithmParameters = algParamHelper.generateAlgParams("CCM");

    String input = "Hello world223Test023john2043{}{qre|'/.,~wq~!@#$(*)-=-+_";
    SecretKey secretKey = SecretKeyUtils.generateSecretKey(CSP_NAME, "AES");

    SymmetricCipher symmetricCrypto = new SymmetricCipher(CSP_NAME, "AES/CCM/NoPadding"
        , secretKey, algorithmParameters);
    byte[] encryptedInput0 = symmetricCrypto.encrypt(input.getBytes());
    byte[] decryptedInput0 = symmetricCrypto.decrypt(encryptedInput0);
    assertThat(decryptedInput0).containsExactly(input.getBytes());
  }

  @Test
  void getAlgParamsWithKeySize() throws GeneralSecurityException {
    AlgorithmParameterHelper algParamHelper = new AlgorithmParameterHelper();
    AlgorithmParameterGenerator algorithmParamGen = algParamHelper.getAlgParamGen(
        "GCM", 256);
    AlgorithmParameters algParamsGCM = algorithmParamGen.generateParameters();

    String input = "Hello world223Test023john2043{}{qre|'/.,~wq~!@#$(*)-=-+_";
    SecretKey secretKey = SecretKeyUtils.generateSecretKey(CSP_NAME, "AES");

    SymmetricCipher symmetricCrypto = new SymmetricCipher(CSP_NAME, "AES/GCM/NoPadding"
        , secretKey, algParamsGCM);
    byte[] encryptedInput0 = symmetricCrypto.encrypt(input.getBytes());
    byte[] decryptedInput0 = symmetricCrypto.decrypt(encryptedInput0);
    assertThat(decryptedInput0).containsExactly(input.getBytes());
  }

  @Test
  void generateAlgParamsWithKeySize() throws GeneralSecurityException {
    AlgorithmParameterHelper algParamHelper = new AlgorithmParameterHelper();
    AlgorithmParameters algParamsGCM = algParamHelper.generateAlgParams("GCM",
        256);

    String input = "Hello world223Test023john2043{}{qre|'/.,~wq~!@#$(*)-=-+_";
    SecretKey secretKey = SecretKeyUtils.generateSecretKey(CSP_NAME, "AES");

    SymmetricCipher symmetricCrypto = new SymmetricCipher(CSP_NAME, "AES/GCM/NoPadding"
        , secretKey, algParamsGCM);
    byte[] encryptedInput0 = symmetricCrypto.encrypt(input.getBytes());
    byte[] decryptedInput0 = symmetricCrypto.decrypt(encryptedInput0);
    assertThat(decryptedInput0).containsExactly(input.getBytes());

  }

  @Test
  @Disabled("Disabled because of performance")
  void generateAlgParamSpec() throws GeneralSecurityException {
    AlgorithmParameterHelper algParamHelper = new AlgorithmParameterHelper();
    DHParameterSpec dhParameterSpec = algParamHelper.generateAlgParamSpec("DH",
        DHParameterSpec.class);
    KeyPair dhKeyPair = KeyPairUtils.generateKeyPair(CSP_NAME, "DH", dhParameterSpec);
    assertThat(dhKeyPair).isNotNull();
    assertThat(dhKeyPair.getPrivate()).isNotNull();
    assertThat(dhKeyPair.getPublic()).isNotNull();
  }

  @Test
  void generateAlgParamSpecWithKeySize() throws GeneralSecurityException {
    AlgorithmParameterHelper algParamHelper = new AlgorithmParameterHelper();
    DHParameterSpec dhParameterSpec = algParamHelper.generateAlgParamSpec("DH",
        256, DHParameterSpec.class);
    KeyPair dhKeyPair = KeyPairUtils.generateKeyPair(CSP_NAME, "DH", dhParameterSpec);
    assertThat(dhKeyPair).isNotNull();
    assertThat(dhKeyPair.getPrivate()).isNotNull();
    assertThat(dhKeyPair.getPublic()).isNotNull();
  }

}