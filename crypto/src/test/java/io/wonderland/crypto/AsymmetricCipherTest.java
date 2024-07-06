package io.wonderland.crypto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.security.GeneralSecurityException;
import java.security.KeyException;
import java.security.KeyPair;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
class AsymmetricCipherTest extends AbstractTest {


  @Test
  void constructors() throws GeneralSecurityException {
    String algorithm = "RSA";
    KeyPair keyPair = KeyPairUtils.generateKeyPair(algorithm, 2048);

    AsymmetricCipher asymmetricCipher = new AsymmetricCipher(algorithm, keyPair);
    assertThat(asymmetricCipher.getProvider()).isEqualTo(CSP.INSTANCE_CONTEXT.getProvider());
    assertThat(asymmetricCipher.getTransformation()).isEqualTo(algorithm);
    assertThat(asymmetricCipher.getEncryptCipher()).isNotNull();
    assertThat(asymmetricCipher.getDecryptCipher()).isNotNull();

    AsymmetricCipher asymmetricCipher1 = new AsymmetricCipher(algorithm, keyPair.getPrivate(),
        keyPair.getPublic());
    assertThat(asymmetricCipher1.getProvider()).isEqualTo(CSP.INSTANCE_CONTEXT.getProvider());
    assertThat(asymmetricCipher1.getTransformation()).isEqualTo(algorithm);
    assertThat(asymmetricCipher1.getEncryptCipher()).isNotNull();
    assertThat(asymmetricCipher1.getDecryptCipher()).isNotNull();
  }


  @Test
  void invalidArguments() throws GeneralSecurityException {
    KeyPair keyPair = KeyPairUtils.generateKeyPair("RSA", 2048);

    //negative tests
    assertThatThrownBy(() -> new AsymmetricCipher(CSP_NAME, "RSA", null)).isInstanceOf(
        NullPointerException.class);
    assertThatThrownBy(
        () -> new AsymmetricCipher(CSP_NAME, "RSA", null, keyPair.getPrivate())).isInstanceOf(
        KeyException.class);

  }

  @Test
  void cipherWithKey() throws GeneralSecurityException {
    KeyPair keyPair = KeyPairUtils.generateKeyPair("RSA", 2048);

    String input0 = "Hello world ~!#@#$@!$@#%$%^%$*^&*(*))_(*&^%$@#@!~`122234536890-=";
    String input1 = "12344455";
    String input2 = "1234445523456";
    byte[] allInput = new byte[input0.length() + input1.length() + input2.length()];
    System.arraycopy(input0.getBytes(), 0, allInput, 0, input0.length());
    System.arraycopy(input1.getBytes(), 0, allInput, input0.length(), input1.length());
    System.arraycopy(input2.getBytes(), 0, allInput, input0.length() + input1.length(),
        input2.length());

    //positive test, encrypt with public-key & decrypt with private-key
    AsymmetricCipher asymmetricCipher0 = new AsymmetricCipher(CSP_NAME, "RSA", keyPair.getPublic(),
        keyPair.getPrivate());
    byte[] encrypted0 = asymmetricCipher0.encrypt(input0.getBytes());
    byte[] decrypted0 = asymmetricCipher0.decrypt(encrypted0);
    assertThat(decrypted0).containsExactly(input0.getBytes());

    //encrypt
    asymmetricCipher0.encryptUpdate(input0.getBytes());
    asymmetricCipher0.encryptUpdate(input1.getBytes());
    asymmetricCipher0.encryptUpdate(input2.getBytes());
    byte[] encrypted1 = asymmetricCipher0.encrypt();

    //decrypt
    asymmetricCipher0.decryptUpdate(encrypted1);
    byte[] decrypted1 = asymmetricCipher0.decrypt();
    assertThat(decrypted1).containsExactly(allInput);

    //=============================================================
    //second test, encrypt with private-key & decrypt with public-key
    AsymmetricCipher asymmetricCipher1 = new AsymmetricCipher(CSP_NAME, "RSA", keyPair.getPrivate(),
        keyPair.getPublic());
    byte[] encrypted2 = asymmetricCipher1.encrypt(input0.getBytes());
    byte[] decrypted2 = asymmetricCipher1.decrypt(encrypted2);
    assertThat(decrypted2).containsExactly(input0.getBytes());

    //encrypt
    asymmetricCipher1.encryptUpdate(input0.getBytes());
    asymmetricCipher1.encryptUpdate(input1.getBytes());
    asymmetricCipher1.encryptUpdate(input2.getBytes());
    byte[] encrypted3 = asymmetricCipher1.encrypt();

    //decrypt
    asymmetricCipher1.decryptUpdate(encrypted3);
    byte[] decrypted3 = asymmetricCipher1.decrypt();
    assertThat(decrypted3).containsExactly(allInput);
  }

  @Test
  void cipherWithKeyPair() throws GeneralSecurityException {
    KeyPair keyPair = KeyPairUtils.generateKeyPair("RSA", 2048);

    String input0 = "Hello world ~!#@#$@!$@#%$%^%$*^&*(*))_(*&^%$@#@!~`122234536890-=";
    String input1 = "12344455";
    String input2 = "1234445523456";
    byte[] allInput = new byte[input0.length() + input1.length() + input2.length()];
    System.arraycopy(input0.getBytes(), 0, allInput, 0, input0.length());
    System.arraycopy(input1.getBytes(), 0, allInput, input0.length(), input1.length());
    System.arraycopy(input2.getBytes(), 0, allInput, input0.length() + input1.length(),
        input2.length());

    //positive test, encrypt with public-key & decrypt with private-key
    AsymmetricCipher asymmetricCipher0 = new AsymmetricCipher(CSP_NAME, "RSA", keyPair);
    byte[] encrypted0 = asymmetricCipher0.encrypt(input0.getBytes());
    byte[] decrypted0 = asymmetricCipher0.decrypt(encrypted0);
    assertThat(decrypted0).containsExactly(input0.getBytes());

    //encrypt
    asymmetricCipher0.encryptUpdate(input0.getBytes());
    asymmetricCipher0.encryptUpdate(input1.getBytes());
    asymmetricCipher0.encryptUpdate(input2.getBytes());
    byte[] encrypted1 = asymmetricCipher0.encrypt();

    //decrypt
    asymmetricCipher0.decryptUpdate(encrypted1);
    byte[] decrypted1 = asymmetricCipher0.decrypt();
    assertThat(decrypted1).containsExactly(allInput);
  }

  @Test
  void cipherFields() throws GeneralSecurityException {
    KeyPair keyPair = KeyPairUtils.generateKeyPair("RSA", 2048);

    //positive test, encrypt with public-key & decrypt with private-key
    AsymmetricCipher asymmetricCipher = new AsymmetricCipher(CSP_NAME, "RSA", keyPair);
    assertThat(asymmetricCipher.getProvider()).isEqualTo(CSP_NAME);
    assertThat(asymmetricCipher.getTransformation()).isEqualTo("RSA");
    assertThat(asymmetricCipher.getEncryptCipher()).isNotNull();
    assertThat(asymmetricCipher.getDecryptCipher()).isNotNull();
  }


}