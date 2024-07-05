package io.wonderland.crypto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.security.GeneralSecurityException;
import java.security.KeyException;
import java.security.KeyPair;
import java.security.Security;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.jupiter.api.Test;

@Slf4j
class AsymmetricCryptoTest {

  static final String CSP = "BC";

  static {
    Security.addProvider(new BouncyCastleProvider());
  }

  @Test
  void invalidArguments() throws GeneralSecurityException {
    KeyPair keyPair = KeyUtils.generateKeyPair("RSA", 2048);

    //negative tests
    assertThatThrownBy(() -> new AsymmetricCrypto(CSP, "RSA", null)).isInstanceOf(
        NullPointerException.class);
    assertThatThrownBy(
        () -> new AsymmetricCrypto(CSP, "RSA", null, keyPair.getPrivate())).isInstanceOf(
        KeyException.class);

  }

  @Test
  void cipherWithKey() throws GeneralSecurityException {
    KeyPair keyPair = KeyUtils.generateKeyPair("RSA", 2048);

    String input0 = "Hello world ~!#@#$@!$@#%$%^%$*^&*(*))_(*&^%$@#@!~`122234536890-=";
    String input1 = "12344455";
    String input2 = "1234445523456";
    byte[] allInput = new byte[input0.length() + input1.length() + input2.length()];
    System.arraycopy(input0.getBytes(), 0, allInput, 0, input0.length());
    System.arraycopy(input1.getBytes(), 0, allInput, input0.length(), input1.length());
    System.arraycopy(input2.getBytes(), 0, allInput, input0.length() + input1.length(),
        input2.length());

    //positive test, encrypt with public-key & decrypt with private-key
    AsymmetricCrypto asymmetricCrypto0 = new AsymmetricCrypto(CSP, "RSA", keyPair.getPublic(),
        keyPair.getPrivate());
    byte[] encrypted0 = asymmetricCrypto0.encrypt(input0.getBytes());
    byte[] decrypted0 = asymmetricCrypto0.decrypt(encrypted0);
    assertThat(decrypted0).containsExactly(input0.getBytes());

    //encrypt
    asymmetricCrypto0.encryptUpdate(input0.getBytes());
    asymmetricCrypto0.encryptUpdate(input1.getBytes());
    asymmetricCrypto0.encryptUpdate(input2.getBytes());
    byte[] encrypted1 = asymmetricCrypto0.encrypt();

    //decrypt
    asymmetricCrypto0.decryptUpdate(encrypted1);
    byte[] decrypted1 = asymmetricCrypto0.decrypt();
    assertThat(decrypted1).containsExactly(allInput);

    //=============================================================
    //second test, encrypt with private-key & decrypt with public-key
    AsymmetricCrypto asymmetricCrypto1 = new AsymmetricCrypto(CSP, "RSA", keyPair.getPrivate(),
        keyPair.getPublic());
    byte[] encrypted2 = asymmetricCrypto1.encrypt(input0.getBytes());
    byte[] decrypted2 = asymmetricCrypto1.decrypt(encrypted2);
    assertThat(decrypted2).containsExactly(input0.getBytes());

    //encrypt
    asymmetricCrypto1.encryptUpdate(input0.getBytes());
    asymmetricCrypto1.encryptUpdate(input1.getBytes());
    asymmetricCrypto1.encryptUpdate(input2.getBytes());
    byte[] encrypted3 = asymmetricCrypto1.encrypt();

    //decrypt
    asymmetricCrypto1.decryptUpdate(encrypted3);
    byte[] decrypted3 = asymmetricCrypto1.decrypt();
    assertThat(decrypted3).containsExactly(allInput);
  }

  @Test
  void cipherWithKeyPair() throws GeneralSecurityException {
    KeyPair keyPair = KeyUtils.generateKeyPair("RSA", 2048);

    String input0 = "Hello world ~!#@#$@!$@#%$%^%$*^&*(*))_(*&^%$@#@!~`122234536890-=";
    String input1 = "12344455";
    String input2 = "1234445523456";
    byte[] allInput = new byte[input0.length() + input1.length() + input2.length()];
    System.arraycopy(input0.getBytes(), 0, allInput, 0, input0.length());
    System.arraycopy(input1.getBytes(), 0, allInput, input0.length(), input1.length());
    System.arraycopy(input2.getBytes(), 0, allInput, input0.length() + input1.length(),
        input2.length());

    //positive test, encrypt with public-key & decrypt with private-key
    AsymmetricCrypto asymmetricCrypto0 = new AsymmetricCrypto(CSP, "RSA", keyPair);
    byte[] encrypted0 = asymmetricCrypto0.encrypt(input0.getBytes());
    byte[] decrypted0 = asymmetricCrypto0.decrypt(encrypted0);
    assertThat(decrypted0).containsExactly(input0.getBytes());

    //encrypt
    asymmetricCrypto0.encryptUpdate(input0.getBytes());
    asymmetricCrypto0.encryptUpdate(input1.getBytes());
    asymmetricCrypto0.encryptUpdate(input2.getBytes());
    byte[] encrypted1 = asymmetricCrypto0.encrypt();

    //decrypt
    asymmetricCrypto0.decryptUpdate(encrypted1);
    byte[] decrypted1 = asymmetricCrypto0.decrypt();
    assertThat(decrypted1).containsExactly(allInput);
  }

  @Test
  void cipherFields() throws GeneralSecurityException {
    KeyPair keyPair = KeyUtils.generateKeyPair("RSA", 2048);

    //positive test, encrypt with public-key & decrypt with private-key
    AsymmetricCrypto asymmetricCrypto = new AsymmetricCrypto(CSP, "RSA", keyPair);
    assertThat(asymmetricCrypto.getProvider()).isEqualTo(CSP);
    assertThat(asymmetricCrypto.getTransformation()).isEqualTo("RSA");
    assertThat(asymmetricCrypto.getEncryptCipher()).isNotNull();
    assertThat(asymmetricCrypto.getDecryptCipher()).isNotNull();
  }


}