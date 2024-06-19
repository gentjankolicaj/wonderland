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
  void cipherWithKeys() throws GeneralSecurityException {
    KeyPair keyPair = KeyUtils.generateKeyPair("RSA", 2048);

    String input = "Hello world ~!#@#$@!$@#%$%^%$*^&*(*))_(*&^%$@#@!~`122234536890-=";

    //positive test, encrypt with public-key & decrypt with private-key
    AsymmetricCrypto asymmetricCrypto0 = new AsymmetricCrypto(CSP, "RSA", keyPair.getPublic(),
        keyPair.getPrivate());
    byte[] encrypted0 = asymmetricCrypto0.encrypt(input.getBytes());
    byte[] decrypted0 = asymmetricCrypto0.decrypt(encrypted0);
    assertThat(decrypted0).containsExactly(input.getBytes());
    log.debug("encrypted0 : {}", new String(encrypted0));
    log.debug("decrypted0 : {}", new String(decrypted0));

    //positive test, encrypt with private-key & decrypt with public-key
    AsymmetricCrypto asymmetricCrypto1 = new AsymmetricCrypto(CSP, "RSA", keyPair.getPrivate(),
        keyPair.getPublic());
    byte[] encrypted1 = asymmetricCrypto1.encrypt(input.getBytes());
    byte[] decrypted1 = asymmetricCrypto1.decrypt(encrypted1);
    assertThat(decrypted1).containsExactly(input.getBytes());
    log.debug("encrypted1 : {}", new String(encrypted1));
    log.debug("decrypted1 : {}", new String(decrypted1));

  }

  @Test
  void cipherWithKeyPair() throws GeneralSecurityException {
    KeyPair keyPair = KeyUtils.generateKeyPair("RSA", 2048);

    String input = "Hello world ~!#@#$@!$@#%$%^%$*^&*(*))_(*&^%$@#@!~`122234536890-=";

    //positive test with key-pair, encrypt with private-key & decrypt with public-key
    AsymmetricCrypto asymmetricCrypto2 = new AsymmetricCrypto(CSP, "RSA", keyPair);
    byte[] encrypted2 = asymmetricCrypto2.encrypt(input.getBytes());
    byte[] decrypted2 = asymmetricCrypto2.decrypt(encrypted2);
    assertThat(decrypted2).containsExactly(input.getBytes());
    log.debug("encrypted2 : {}", new String(encrypted2));
    log.debug("decrypted2 : {}", new String(decrypted2));
  }


}