package io.wonderland.crypto.store;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.wonderland.crypto.AbstractTest;
import java.io.IOException;
import java.nio.file.Path;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.util.Objects;
import org.junit.jupiter.api.Test;

class KeyStoreUtilsTest extends AbstractTest {


  @Test
  void getPrivateKey() throws GeneralSecurityException, IOException {
    Path pkcs12KeystorePath = Path.of(
        Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("bc.p12"))
            .getPath());
    PrivateKey privateKey = KeyStoreUtils.getPrivateKey(CSP_NAME, pkcs12KeystorePath,
        KeyStoreType.PKCS12, "p4ssword", "bc");
    assertThat(privateKey).isNotNull();
    assertThat(privateKey.getEncoded()).isNotNull();
    assertThat(privateKey.getFormat()).isEqualTo("PKCS#8");
  }

  @Test
  void getPublicKey() throws GeneralSecurityException, IOException {
    Path pkcs12KeystorePath = Path.of(
        Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("bc.p12"))
            .getPath());

    PublicKey publicKey = KeyStoreUtils.getPublicKey(CSP_NAME, pkcs12KeystorePath,
        KeyStoreType.PKCS12,
        "p4ssword", "bc");
    assertThat(publicKey).isNotNull();
    assertThat(publicKey.getEncoded()).isNotNull();
    assertThat(publicKey.getFormat()).isEqualTo("X.509");
  }

  @Test
  void getKeyPair() throws GeneralSecurityException, IOException {
    Path pkcs12KeystorePath = Path.of(
        Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("bc.p12"))
            .getPath());

    KeyPair keyPair = KeyStoreUtils.getKeyPair(CSP_NAME, pkcs12KeystorePath, KeyStoreType.PKCS12,
        "p4ssword", "bc");
    assertThat(keyPair).isNotNull();
    assertThat(keyPair.getPrivate()).isNotNull();
    assertThat(keyPair.getPrivate().getEncoded()).isNotNull();
    assertThat(keyPair.getPrivate().getFormat()).isEqualTo("PKCS#8");
    assertThat(keyPair.getPublic()).isNotNull();
    assertThat(keyPair.getPublic().getEncoded()).isNotNull();
    assertThat(keyPair.getPublic().getFormat()).isEqualTo("X.509");
  }

  @Test
  void getCertificate() throws GeneralSecurityException, IOException {
    Path pkcs12KeystorePath = Path.of(
        Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("bc.p12"))
            .getPath());

    //positive test
    Certificate cert = KeyStoreUtils.getCertificate(CSP_NAME, pkcs12KeystorePath,
        KeyStoreType.PKCS12,
        "p4ssword", "bc");
    assertThat(cert).isNotNull();

    //negative test
    Certificate noneCert = KeyStoreUtils.getCertificate(CSP_NAME, pkcs12KeystorePath,
        KeyStoreType.PKCS12, "p4ssword", "");
    assertThat(noneCert).isNull();
  }

  @Test
  void loadKeystore() throws GeneralSecurityException, IOException {
    Path pkcs12KeystorePath = Path.of(
        Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("bc.p12"))
            .getPath());

    //test pkcs12 keystore
    assertThatThrownBy(
        () -> KeyStoreUtils.loadKeyStore(CSP_NAME, pkcs12KeystorePath, KeyStoreType.PKCS12, "1"))
        .isInstanceOf(IOException.class);
    assertThat(KeyStoreUtils.loadKeyStore(CSP_NAME, pkcs12KeystorePath, KeyStoreType.PKCS12,
        "p4ssword")).isNotNull();

  }


}