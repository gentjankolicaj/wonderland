package io.wonderland.crypto.store;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.wonderland.crypto.AbstractTest;
import io.wonderland.crypto.CSP;
import io.wonderland.crypto.PBKDFUtils;
import java.io.IOException;
import java.nio.file.Path;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.Objects;
import org.bouncycastle.crypto.util.PBKDFConfig;
import org.bouncycastle.jcajce.BCFKSLoadStoreParameter;
import org.junit.jupiter.api.Test;

//@SuppressWarnings("all")
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

  @Test
  void loadKeystoreWithBCFKS()
      throws KeyStoreException, NoSuchProviderException, CertificateException, IOException, NoSuchAlgorithmException {
    PBKDFConfig config = PBKDFUtils.createPbkdfConfigWithScrypt(1024, 8, 1, 20);

    KeyStore store = KeyStoreUtils.getInstance(CSP_NAME, KeyStoreType.BOUNCY_CASTLE_FKS);

    //initialize empty keystore to use scrypt
    store.load(new BCFKSLoadStoreParameter.Builder().withStorePBKDFConfig(config).build());

    //do something on store
    //store.setEntry()....
    //FileOutputStream fOut = new FileOutputStream("scrypt.fks");
    //store.store(fOut,"storePassword");
    //fOut.close();
    assertThat(config).isNotNull();
    assertThat(store).isNotNull();
  }

  @Test
  void loadKeystoreWithJKS()
      throws KeyStoreException, NoSuchProviderException, CertificateException, IOException, NoSuchAlgorithmException {

    KeyStore store = KeyStoreUtils.getInstance(CSP.SUN, KeyStoreType.JKS);

    //initialize empty keystore
    store.load(null, null);

    //do something on store
    //store.setEntry()....
    //FileOutputStream fOut = new FileOutputStream("keystore.jks");
    //store.store(fOut,"storePassword");
    //fOut.close();
    assertThat(store).isNotNull();
  }

  @Test
  void loadKeystoreWithPKCS12()
      throws KeyStoreException, NoSuchProviderException, CertificateException, IOException, NoSuchAlgorithmException {

    KeyStore store = KeyStoreUtils.getInstance(CSP_NAME, KeyStoreType.PKCS12);

    //initialize empty keystore
    store.load(null, null);

    //do something on store
    //store.setEntry()....
    //FileOutputStream fOut = new FileOutputStream("keystore.jks");
    //store.store(fOut,"storePassword");
    //fOut.close();
    assertThat(store).isNotNull();
  }


}