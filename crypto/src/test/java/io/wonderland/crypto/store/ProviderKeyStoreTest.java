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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProviderKeyStoreTest extends AbstractTest {

  private ProviderKeyStore bcKeyStore;

  @BeforeEach
  void before() {
    bcKeyStore = new ProviderKeyStore(CSP_NAME);
  }


  @Test
  void getPrivateKey() throws GeneralSecurityException, IOException {
    Path pkcs12KeystorePath = Path.of(
        Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("bc.p12"))
            .getPath());
    PrivateKey privateKey = bcKeyStore.getPrivateKey(pkcs12KeystorePath, KeyStoreType.PKCS12,
        "p4ssword", "bc");
    assertThat(privateKey).isNotNull();
    assertThat(privateKey.getEncoded()).isNotNull();
    assertThat(privateKey.getFormat()).isEqualTo("PKCS#8");
  }

  @Test
  void getPublicKey() throws GeneralSecurityException, IOException {
    Path pkcs12KeystorePath = Path.of(
        Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("bc.p12"))
            .getPath());

    PublicKey publicKey = bcKeyStore.getPublicKey(pkcs12KeystorePath, KeyStoreType.PKCS12,
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

    KeyPair keyPair = bcKeyStore.getKeyPair(pkcs12KeystorePath, KeyStoreType.PKCS12, "p4ssword",
        "bc");
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
    Certificate cert = bcKeyStore.getCertificate(pkcs12KeystorePath, KeyStoreType.PKCS12,
        "p4ssword", "bc");
    assertThat(cert).isNotNull();

    //negative test
    Certificate noneCert = bcKeyStore.getCertificate(pkcs12KeystorePath, KeyStoreType.PKCS12,
        "p4ssword", "");
    assertThat(noneCert).isNull();
  }

  @Test
  void loadKeystore() throws GeneralSecurityException, IOException {
    Path pkcs12KeystorePath = Path.of(
        Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("bc.p12"))
            .getPath());

    //test pkcs12 keystore
    assertThatThrownBy(() -> bcKeyStore.loadKeyStore(pkcs12KeystorePath, KeyStoreType.PKCS12, "1"))
        .isInstanceOf(IOException.class);
    assertThat(
        bcKeyStore.loadKeyStore(pkcs12KeystorePath, KeyStoreType.PKCS12, "p4ssword")).isNotNull();

  }


}