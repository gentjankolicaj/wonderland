package io.wonderland.crypto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.security.GeneralSecurityException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jcajce.spec.AEADParameterSpec;
import org.junit.jupiter.api.Test;

@Slf4j
class SymmetricCipherTest extends AbstractTest {


  @Test
  void constructors() throws GeneralSecurityException {
    String algorithm = "AES";
    SecretKey secretKey = SecretKeyUtils.generateSecretKey(algorithm);

    SymmetricCipher symmetricCipher = new SymmetricCipher(algorithm, secretKey);
    assertThat(symmetricCipher.getProvider()).isEqualTo(CSP.INSTANCE_CONTEXT.getProvider());
    assertThat(symmetricCipher.getDecryptCipher()).isNotNull();
    assertThat(symmetricCipher.getEncryptCipher()).isNotNull();
    assertThat(symmetricCipher.getTransformation()).isEqualTo(algorithm);

    //with alg parameters spec
    IvParameterSpec ivParameterSpec1 = AlgorithmParameterUtils.generateIvParameterSpec(CSP.SUN,
        "SHA1PRNG", 16);
    SymmetricCipher symmetricCipher1 = new SymmetricCipher(algorithm, secretKey, ivParameterSpec1);
    assertThat(symmetricCipher1.getProvider()).isEqualTo(CSP.INSTANCE_CONTEXT.getProvider());
    assertThat(symmetricCipher1.getDecryptCipher()).isNotNull();
    assertThat(symmetricCipher1.getEncryptCipher()).isNotNull();
    assertThat(symmetricCipher1.getTransformation()).isEqualTo(algorithm);

    // alg parameters
    byte[] aad = "Additional associated data".getBytes();
    IvParameterSpec ivParameterSpec2 = AlgorithmParameterUtils.generateIvParameterSpec(CSP.SUN,
        "SHA1PRNG", 12);
    AEADParameterSpec spec = new AEADParameterSpec(ivParameterSpec2.getIV(), 128, aad);
    SymmetricCipher symmetricCipher2 = new SymmetricCipher("AES/CCM/NoPadding", secretKey, spec);
    assertThat(symmetricCipher2.getProvider()).isEqualTo(CSP.INSTANCE_CONTEXT.getProvider());
    assertThat(symmetricCipher2.getDecryptCipher()).isNotNull();
    assertThat(symmetricCipher2.getEncryptCipher()).isNotNull();
    assertThat(symmetricCipher2.getTransformation()).isEqualTo("AES/CCM/NoPadding");

  }



  @Test
  void ecbNoPadding() throws GeneralSecurityException {
    String input0 = "Hello world ~!#@#$@!$@#%$%^%$*^&*(*))_(*&^%$@#@!~`122234536890-=";
    String input1 = "12344455";
    String input2 = "1234445523456";

    SecretKey secretKey = SecretKeyUtils.generateSecretKey("AES");

    //positive test case
    SymmetricCipher symmetricCrypto0 = new SymmetricCipher(CSP_NAME, "AES/ECB/NoPadding",
        secretKey);
    byte[] encryptedInput0 = symmetricCrypto0.encrypt(input0.getBytes());
    byte[] decryptedInput0 = symmetricCrypto0.decrypt(encryptedInput0);
    assertThat(decryptedInput0).containsExactly(input0.getBytes());

    //negative test case
    IvParameterSpec iv = AlgorithmParameterUtils.generateIvParameterSpec(CSP_NAME,
        "SHA1PRNG", 16);
    assertThatThrownBy(
        () -> new SymmetricCipher(CSP_NAME, "AES/ECB/NoPadding", secretKey, iv))
        .isInstanceOf(GeneralSecurityException.class);

    //encrypt
    symmetricCrypto0.encryptUpdate(input0.getBytes());
    symmetricCrypto0.encryptUpdate(input1.getBytes());
    symmetricCrypto0.encryptUpdate(input2.getBytes());

    //Because this is block cipher & no padding is applied for plaintext
    assertThatThrownBy(symmetricCrypto0::encrypt)
        .hasMessage("data not block size aligned").isInstanceOf(CryptoException.class);

  }


  @Test
  void cbcNoPadding() throws GeneralSecurityException {
    String plainText = "Hello world ~!#@#$@!$@#%$%^%$*^&*(*))_(*&^%$@#@!~`122234536890-=";
    SecretKey secretKey = SecretKeyUtils.generateSecretKey("AES");

    //negative test case
    assertThatThrownBy(() -> new SymmetricCipher(CSP_NAME, "AES/CBC/NoPadding", secretKey))
        .isInstanceOf(GeneralSecurityException.class);

    //positive test case
    IvParameterSpec ivParameterSpec = AlgorithmParameterUtils.generateIvParameterSpec(CSP_NAME,
        "SHA1PRNG", 16);
    SymmetricCipher symmetricCrypto0 = new SymmetricCipher(CSP_NAME, "AES/CBC/NoPadding", secretKey,
        ivParameterSpec);
    byte[] encryptedInput0 = symmetricCrypto0.encrypt(plainText.getBytes());
    byte[] decryptedInput0 = symmetricCrypto0.decrypt(encryptedInput0);
    assertThat(decryptedInput0).containsExactly(plainText.getBytes());
  }

  @Test
  void ctrNoPadding() throws GeneralSecurityException {
    String plainText = "Hello world ~!#@#$@!$@#%$%^%$*^&*(*))_(*&^%$@#@!~`122234536890-=";
    SecretKey secretKey = SecretKeyUtils.generateSecretKey("AES");

    //negative test case
    assertThatThrownBy(
        () -> new SymmetricCipher(CSP_NAME, "AES/CTR/NoPadding", secretKey)).isInstanceOf(
        GeneralSecurityException.class);

    //positive test case
    IvParameterSpec ivParameterSpec = AlgorithmParameterUtils.generateIvParameterSpec(CSP_NAME,
        "SHA1PRNG", 16);
    SymmetricCipher symmetricCrypto0 = new SymmetricCipher(CSP_NAME, "AES/CTR/NoPadding", secretKey,
        ivParameterSpec);
    byte[] encryptedInput0 = symmetricCrypto0.encrypt(plainText.getBytes());
    byte[] decryptedInput0 = symmetricCrypto0.decrypt(encryptedInput0);
    assertThat(decryptedInput0).containsExactly(plainText.getBytes());
  }

  @Test
  void cbcCTSPadding() throws GeneralSecurityException {
    String plainText = "Hello world ~!#@#$@!$@#%$%^%$*^&*(*))_(*&^%$@#@!~`122234536890-=";
    SecretKey secretKey = SecretKeyUtils.generateSecretKey("AES");

    //negative test case
    assertThatThrownBy(
        () -> new SymmetricCipher(CSP_NAME, "AES/CBC/CTSPadding", secretKey)).isInstanceOf(
        GeneralSecurityException.class);

    //positive test case
    IvParameterSpec ivParameterSpec = AlgorithmParameterUtils.generateIvParameterSpec(CSP_NAME,
        "SHA1PRNG", 16);
    SymmetricCipher symmetricCrypto0 = new SymmetricCipher(CSP_NAME, "AES/CBC/CTSPadding",
        secretKey,
        ivParameterSpec);
    byte[] encryptedInput0 = symmetricCrypto0.encrypt(plainText.getBytes());
    byte[] decryptedInput0 = symmetricCrypto0.decrypt(encryptedInput0);
    assertThat(decryptedInput0).containsExactly(plainText.getBytes());
  }

  @Test
  void chacha7539() throws GeneralSecurityException {
    String plainText = "Hello world ~!#@#$@!$@#%$%^%$*^&*(*))_(*&^%$@#@!~`122234536890-=";
    SecretKey secretKey = SecretKeyUtils.generateSecretKey("ChaCha7539");

    //negative test case
    assertThatThrownBy(() -> new SymmetricCipher(CSP_NAME, "ChaCha7539", secretKey))
        .isInstanceOf(GeneralSecurityException.class);

    //positive test case
    IvParameterSpec ivParameterSpec = AlgorithmParameterUtils.generateIvParameterSpec(CSP_NAME,
        "SHA1PRNG", 12);
    SymmetricCipher symmetricCrypto0 = new SymmetricCipher(CSP_NAME, "ChaCha7539",
        secretKey, ivParameterSpec);
    byte[] encryptedInput0 = symmetricCrypto0.encrypt(plainText.getBytes());
    byte[] decryptedInput0 = symmetricCrypto0.decrypt(encryptedInput0);
    assertThat(decryptedInput0).containsExactly(plainText.getBytes());
  }


  @Test
  void ccmNoPaddingWithADD() throws GeneralSecurityException {
    String plainText = "Hello world ~!#@#$@!$@#%$%^%$*^&*(*))_(*&^%$@#@!~`122234536890-=";
    byte[] aad = "Additional associated data".getBytes();
    SecretKey secretKey = SecretKeyUtils.generateSecretKey("AES");

    //negative test case
    assertThatThrownBy(
        () -> new SymmetricCipher(CSP_NAME, "AES/CCM/NoPadding", secretKey))
        .isInstanceOf(GeneralSecurityException.class);

    //positive test case
    IvParameterSpec ivParameterSpec = AlgorithmParameterUtils.generateIvParameterSpec(CSP_NAME,
        "SHA1PRNG", 12);
    AEADParameterSpec spec = new AEADParameterSpec(ivParameterSpec.getIV(), 128, aad);

    SymmetricCipher symmetricCrypto0 = new SymmetricCipher(CSP_NAME,
        "AES/CCM/NoPadding", secretKey, spec);
    byte[] encryptedInput0 = symmetricCrypto0.encrypt(plainText.getBytes());
    byte[] decryptedInput0 = symmetricCrypto0.decrypt(encryptedInput0);
    assertThat(decryptedInput0).containsExactly(plainText.getBytes());
  }

  @Test
  void eaxNoPadding() throws GeneralSecurityException {
    String plainText = "Hello world ~!#@#$@!$@#%$%^%$*^&*(*))_(*&^%$@#@!~`122234536890-=";
    byte[] aad = "Additional associated data".getBytes();
    SecretKey secretKey = SecretKeyUtils.generateSecretKey("AES");

    //negative test case
    assertThatThrownBy(
        () -> new SymmetricCipher(CSP.BC, "AES/EAX/NoPadding", secretKey))
        .isInstanceOf(GeneralSecurityException.class);

    //positive test case
    IvParameterSpec ivParameterSpec = AlgorithmParameterUtils.generateIvParameterSpec(CSP_NAME,
        "SHA1PRNG", 12);
    AEADParameterSpec spec = new AEADParameterSpec(ivParameterSpec.getIV(), 128, aad);

    SymmetricCipher symmetricCrypto0 = new SymmetricCipher(CSP_NAME,
        "AES/EAX/NoPadding", secretKey, spec);
    byte[] encryptedInput0 = symmetricCrypto0.encrypt(plainText.getBytes());
    byte[] decryptedInput0 = symmetricCrypto0.decrypt(encryptedInput0);
    assertThat(decryptedInput0).containsExactly(plainText.getBytes());
  }


  @Test
  void gcmNoPadding() throws GeneralSecurityException {
    String plainText = "Hello world ~!#@#$@!$@#%$%^%$*^&*(*))_(*&^%$@#@!~`122234536890-=";
    byte[] aad = "Additional associated data".getBytes();
    SecretKey secretKey = SecretKeyUtils.generateSecretKey("AES");

    //negative test case
    assertThatThrownBy(
        () -> new SymmetricCipher(CSP_NAME, "AES/GCM/NoPadding", secretKey))
        .isInstanceOf(GeneralSecurityException.class);

    //positive test case
    IvParameterSpec ivParameterSpec = AlgorithmParameterUtils.generateIvParameterSpec(CSP_NAME,
        "SHA1PRNG", 12);
    AEADParameterSpec spec = new AEADParameterSpec(ivParameterSpec.getIV(), 128, aad);

    SymmetricCipher symmetricCrypto0 = new SymmetricCipher(CSP_NAME, "AES/GCM/NoPadding",
        secretKey, spec);
    byte[] encryptedInput0 = symmetricCrypto0.encrypt(plainText.getBytes());
    byte[] decryptedInput0 = symmetricCrypto0.decrypt(encryptedInput0);
    assertThat(decryptedInput0).containsExactly(plainText.getBytes());


    //positive test case
    IvParameterSpec ivParameterSpec1 = AlgorithmParameterUtils.generateIvParameterSpec(CSP_NAME,
        "SHA1PRNG", 12);
    AEADParameterSpec spec1 = new AEADParameterSpec(ivParameterSpec1.getIV(), 128);

    SymmetricCipher symmetricCrypto1 = new SymmetricCipher(CSP_NAME,
        "AES/GCM/NoPadding", secretKey, spec1);
    byte[] encryptedInput1 = symmetricCrypto1.encrypt(plainText.getBytes());
    byte[] decryptedInput1 = symmetricCrypto1.decrypt(encryptedInput1);
    assertThat(decryptedInput1).containsExactly(plainText.getBytes());
  }


}