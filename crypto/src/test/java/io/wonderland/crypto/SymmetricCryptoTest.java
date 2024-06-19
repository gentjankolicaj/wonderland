package io.wonderland.crypto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.security.GeneralSecurityException;
import java.security.Security;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jcajce.spec.AEADParameterSpec;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.jupiter.api.Test;

@Slf4j
class SymmetricCryptoTest {

  static final String CSP = "BC";

  static {
    Security.addProvider(new BouncyCastleProvider());
  }


  @Test
  void ecbNoPadding() throws GeneralSecurityException {
    String plainText = "Hello world ~!#@#$@!$@#%$%^%$*^&*(*))_(*&^%$@#@!~`122234536890-=";
    SecretKey secretKey = KeyUtils.generateSecretKey("AES");

    //positive test case
    SymmetricCrypto symmetricCrypto0 = new SymmetricCrypto(CSP, "AES/ECB/NoPadding", secretKey);
    byte[] encryptedInput0 = symmetricCrypto0.encrypt(plainText.getBytes());
    byte[] decryptedInput0 = symmetricCrypto0.decrypt(encryptedInput0);
    assertThat(decryptedInput0).containsExactly(plainText.getBytes());
    log.debug("ecbNoPadding encryptedInput0 : {}", new String(encryptedInput0));
    log.debug("ecbNoPadding decryptedInput0 : {}", new String(decryptedInput0));

    //negative test case
    IvParameterSpec iv = AlgorithmParameterUtils.generateIvParameterSpec("SHA1PRNG", 16);
    assertThatThrownBy(
        () -> new SymmetricCrypto(CSP, "AES/ECB/NoPadding", secretKey, iv)).isInstanceOf(
        GeneralSecurityException.class);
  }


  @Test
  void cbcNoPadding() throws GeneralSecurityException {
    String plainText = "Hello world ~!#@#$@!$@#%$%^%$*^&*(*))_(*&^%$@#@!~`122234536890-=";
    SecretKey secretKey = KeyUtils.generateSecretKey("AES");

    //negative test case
    assertThatThrownBy(() -> new SymmetricCrypto(CSP, "AES/CBC/NoPadding", secretKey)).isInstanceOf(
        GeneralSecurityException.class);

    //positive test case
    IvParameterSpec ivParameterSpec = AlgorithmParameterUtils.generateIvParameterSpec("SHA1PRNG",
        16);
    SymmetricCrypto symmetricCrypto0 = new SymmetricCrypto(CSP, "AES/CBC/NoPadding", secretKey,
        ivParameterSpec);
    byte[] encryptedInput0 = symmetricCrypto0.encrypt(plainText.getBytes());
    byte[] decryptedInput0 = symmetricCrypto0.decrypt(encryptedInput0);
    assertThat(decryptedInput0).containsExactly(plainText.getBytes());
    log.debug("cbcNoPadding encryptedInput0 : {}", new String(encryptedInput0));
    log.debug("cbcNoPadding decryptedInput0 : {}", new String(decryptedInput0));
  }

  @Test
  void cbcPKCS5Padding() throws GeneralSecurityException {
    String plainText = "Hello world ~!#@#$@!$@#%$%^%$*^&*(*))_(*&^%$@#@!~`122234536890-=";
    SecretKey secretKey = KeyUtils.generateSecretKey("AES");

    //negative test case
    assertThatThrownBy(
        () -> new SymmetricCrypto(CSP, "AES/CBC/PKCS5Padding", secretKey)).isInstanceOf(
        GeneralSecurityException.class);

    //positive test case
    IvParameterSpec ivParameterSpec = AlgorithmParameterUtils.generateIvParameterSpec("SHA1PRNG",
        16);
    SymmetricCrypto symmetricCrypto0 = new SymmetricCrypto(CSP, "AES/CBC/PKCS5Padding", secretKey,
        ivParameterSpec);
    byte[] encryptedInput0 = symmetricCrypto0.encrypt(plainText.getBytes());
    byte[] decryptedInput0 = symmetricCrypto0.decrypt(encryptedInput0);
    assertThat(decryptedInput0).containsExactly(plainText.getBytes());
    log.debug("cbcPKCS5Padding encryptedInput0 : {}", new String(encryptedInput0));
    log.debug("cbcPKCS5Padding decryptedInput0 : {}", new String(decryptedInput0));
  }

  @Test
  void cbcCTSPadding() throws GeneralSecurityException {
    String plainText = "Hello world ~!#@#$@!$@#%$%^%$*^&*(*))_(*&^%$@#@!~`122234536890-=";
    SecretKey secretKey = KeyUtils.generateSecretKey("AES");

    //negative test case
    assertThatThrownBy(
        () -> new SymmetricCrypto(CSP, "AES/CBC/CTSPadding", secretKey)).isInstanceOf(
        GeneralSecurityException.class);

    //positive test case
    IvParameterSpec ivParameterSpec = AlgorithmParameterUtils.generateIvParameterSpec("SHA1PRNG",
        16);
    SymmetricCrypto symmetricCrypto0 = new SymmetricCrypto(CSP, "AES/CBC/CTSPadding", secretKey,
        ivParameterSpec);
    byte[] encryptedInput0 = symmetricCrypto0.encrypt(plainText.getBytes());
    byte[] decryptedInput0 = symmetricCrypto0.decrypt(encryptedInput0);
    assertThat(decryptedInput0).containsExactly(plainText.getBytes());
    log.debug("cbcCTSPadding encryptedInput0 : {}", new String(encryptedInput0));
    log.debug("cbcCTSPadding decryptedInput0 : {}", new String(decryptedInput0));
  }

  @Test
  void ctrNoPadding() throws GeneralSecurityException {
    String plainText = "Hello world ~!#@#$@!$@#%$%^%$*^&*(*))_(*&^%$@#@!~`122234536890-=";
    SecretKey secretKey = KeyUtils.generateSecretKey("AES");

    //negative test case
    assertThatThrownBy(() -> new SymmetricCrypto(CSP, "AES/CTR/NoPadding", secretKey)).isInstanceOf(
        GeneralSecurityException.class);

    //positive test case
    IvParameterSpec ivParameterSpec = AlgorithmParameterUtils.generateIvParameterSpec("SHA1PRNG",
        16);
    SymmetricCrypto symmetricCrypto0 = new SymmetricCrypto(CSP, "AES/CTR/NoPadding", secretKey,
        ivParameterSpec);
    byte[] encryptedInput0 = symmetricCrypto0.encrypt(plainText.getBytes());
    byte[] decryptedInput0 = symmetricCrypto0.decrypt(encryptedInput0);
    assertThat(decryptedInput0).containsExactly(plainText.getBytes());
    log.debug("ctrNoPadding encryptedInput0 : {}", new String(encryptedInput0));
    log.debug("ctrNoPadding decryptedInput0 : {}", new String(decryptedInput0));
  }

  @Test
  void chacha7539() throws GeneralSecurityException {
    String plainText = "Hello world ~!#@#$@!$@#%$%^%$*^&*(*))_(*&^%$@#@!~`122234536890-=";
    SecretKey secretKey = KeyUtils.generateSecretKey("ChaCha7539");

    //negative test case
    assertThatThrownBy(() -> new SymmetricCrypto(CSP, "ChaCha7539", secretKey)).isInstanceOf(
        GeneralSecurityException.class);

    //positive test case
    IvParameterSpec ivParameterSpec = AlgorithmParameterUtils.generateIvParameterSpec("SHA1PRNG",
        12);
    SymmetricCrypto symmetricCrypto0 = new SymmetricCrypto(CSP, "ChaCha7539", secretKey,
        ivParameterSpec);
    byte[] encryptedInput0 = symmetricCrypto0.encrypt(plainText.getBytes());
    byte[] decryptedInput0 = symmetricCrypto0.decrypt(encryptedInput0);
    assertThat(decryptedInput0).containsExactly(plainText.getBytes());
    log.debug("chacha7539 encryptedInput0 : {}", new String(encryptedInput0));
    log.debug("chacha7539 decryptedInput0 : {}", new String(decryptedInput0));
  }


  @Test
  void ccmNoPaddingWithADD() throws GeneralSecurityException {
    String plainText = "Hello world ~!#@#$@!$@#%$%^%$*^&*(*))_(*&^%$@#@!~`122234536890-=";
    byte[] aad = "Additional associated data".getBytes();
    SecretKey secretKey = KeyUtils.generateSecretKey("AES");

    //negative test case
    assertThatThrownBy(() -> new SymmetricCrypto(CSP, "AES/CCM/NoPadding", secretKey)).isInstanceOf(
        GeneralSecurityException.class);

    //positive test case
    IvParameterSpec ivParameterSpec = AlgorithmParameterUtils.generateIvParameterSpec("SHA1PRNG",
        12);
    AEADParameterSpec spec = new AEADParameterSpec(ivParameterSpec.getIV(), 128, aad);

    SymmetricCrypto symmetricCrypto0 = new SymmetricCrypto(CSP, "AES/CCM/NoPadding", secretKey,
        spec);
    byte[] encryptedInput0 = symmetricCrypto0.encrypt(plainText.getBytes());
    byte[] decryptedInput0 = symmetricCrypto0.decrypt(encryptedInput0);
    assertThat(decryptedInput0).containsExactly(plainText.getBytes());
    log.debug("ccmNoPaddingWithADD encryptedInput0 : {}", new String(encryptedInput0));
    log.debug("ccmNoPaddingWithADD decryptedInput0 : {}", new String(decryptedInput0));
  }

  @Test
  void eaxNoPadding() throws GeneralSecurityException {
    String plainText = "Hello world ~!#@#$@!$@#%$%^%$*^&*(*))_(*&^%$@#@!~`122234536890-=";
    byte[] aad = "Additional associated data".getBytes();
    SecretKey secretKey = KeyUtils.generateSecretKey("AES");

    //negative test case
    assertThatThrownBy(() -> new SymmetricCrypto(CSP, "AES/EAX/NoPadding", secretKey)).isInstanceOf(
        GeneralSecurityException.class);

    //positive test case
    IvParameterSpec ivParameterSpec = AlgorithmParameterUtils.generateIvParameterSpec("SHA1PRNG",
        12);
    AEADParameterSpec spec = new AEADParameterSpec(ivParameterSpec.getIV(), 128, aad);

    SymmetricCrypto symmetricCrypto0 = new SymmetricCrypto(CSP, "AES/EAX/NoPadding", secretKey,
        spec);
    byte[] encryptedInput0 = symmetricCrypto0.encrypt(plainText.getBytes());
    byte[] decryptedInput0 = symmetricCrypto0.decrypt(encryptedInput0);
    assertThat(decryptedInput0).containsExactly(plainText.getBytes());
    log.debug("eaxNoPadding encryptedInput0 : {}", new String(encryptedInput0));
    log.debug("eaxNoPadding decryptedInput0 : {}", new String(decryptedInput0));
  }


  @Test
  void gcmNoPadding() throws GeneralSecurityException {
    String plainText = "Hello world ~!#@#$@!$@#%$%^%$*^&*(*))_(*&^%$@#@!~`122234536890-=";
    byte[] aad = "Additional associated data".getBytes();
    SecretKey secretKey = KeyUtils.generateSecretKey("AES");

    //negative test case
    assertThatThrownBy(() -> new SymmetricCrypto(CSP, "AES/GCM/NoPadding", secretKey)).isInstanceOf(
        GeneralSecurityException.class);

    //positive test case
    IvParameterSpec ivParameterSpec = AlgorithmParameterUtils.generateIvParameterSpec("SHA1PRNG",
        12);
    AEADParameterSpec spec = new AEADParameterSpec(ivParameterSpec.getIV(), 128, aad);

    SymmetricCrypto symmetricCrypto0 = new SymmetricCrypto(CSP, "AES/GCM/NoPadding", secretKey,
        spec);
    byte[] encryptedInput0 = symmetricCrypto0.encrypt(plainText.getBytes());
    byte[] decryptedInput0 = symmetricCrypto0.decrypt(encryptedInput0);
    assertThat(decryptedInput0).containsExactly(plainText.getBytes());
    log.debug("gcmNoPadding encryptedInput0 : {}", new String(encryptedInput0));
    log.debug("gcmNoPadding decryptedInput0 : {}", new String(decryptedInput0));

    //positive test case
    IvParameterSpec ivParameterSpec1 = AlgorithmParameterUtils.generateIvParameterSpec("SHA1PRNG",
        12);
    AEADParameterSpec spec1 = new AEADParameterSpec(ivParameterSpec1.getIV(), 128);

    SymmetricCrypto symmetricCrypto1 = new SymmetricCrypto(CSP, "AES/GCM/NoPadding", secretKey,
        spec1);
    byte[] encryptedInput1 = symmetricCrypto1.encrypt(plainText.getBytes());
    byte[] decryptedInput1 = symmetricCrypto1.decrypt(encryptedInput1);
    assertThat(decryptedInput0).containsExactly(plainText.getBytes());
    log.debug("gcmNoPadding encryptedInput1 : {}", new String(encryptedInput1));
    log.debug("gcmNoPadding decryptedInput1 : {}", new String(decryptedInput1));
  }


}