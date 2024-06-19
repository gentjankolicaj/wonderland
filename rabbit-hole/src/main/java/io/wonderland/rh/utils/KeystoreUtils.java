package io.wonderland.rh.utils;

import io.wonderland.rh.export.KeystoreBox.KeystoreParams;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.KeyStore.PasswordProtection;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.operator.OperatorCreationException;

@Slf4j
public class KeystoreUtils {

  public static final String DEFAULT_KEYSTORE_NAME = "keystore";
  public static final String KEY_PREFIX = "-HH-mm-ss-nn";

  private KeystoreUtils() {
  }


  public static void exportSecretKey(SecretKey secretKey, KeystoreParams keystoreParams)
      throws KeyStoreException, CertificateException, IOException, NoSuchAlgorithmException {
    KeyStore ks = KeyStore.getInstance(keystoreParams.getKsType());
    ks.load(null, keystoreParams.getKsPwd().toCharArray());
    ks.setEntry(keystoreParams.getKeyAlias(), new KeyStore.SecretKeyEntry(secretKey),
        new PasswordProtection(keystoreParams.getKeyPwd().toCharArray()));
    try (FileOutputStream fos = new FileOutputStream(
        getKeystorePathname(keystoreParams.getKsType(), keystoreParams.getKsPath()))) {
      ks.store(fos, keystoreParams.getKsPwd().toCharArray());
    }

  }


  public static void exportKeyPair(KeyPair keyPair, KeystoreParams keystoreParams)
      throws KeyStoreException, CertificateException, IOException, NoSuchAlgorithmException, OperatorCreationException {
    KeyStore ks = KeyStore.getInstance(keystoreParams.getKsType());
    ks.load(null, keystoreParams.getKsPwd().toCharArray());
    X509Certificate[] selfSignedCerts = new X509Certificate[]{
        X509CertUtils.getSelfSignedCert(keyPair)};
    ks.setKeyEntry(keystoreParams.getKeyAlias(), keyPair.getPrivate(),
        keystoreParams.getKeyPwd().toCharArray(),
        selfSignedCerts);
    try (FileOutputStream fos = new FileOutputStream(
        getKeystorePathname(keystoreParams.getKsType(), keystoreParams.getKsPath()))) {
      ks.store(fos, keystoreParams.getKsPwd().toCharArray());
    }
  }

  public static String getKeystorePathname(String keystoreType, String keystorePath) {
    String localTime = LocalTime.now().format(DateTimeFormatter.ofPattern(KEY_PREFIX));
    return keystorePath + File.separatorChar + DEFAULT_KEYSTORE_NAME + localTime + "."
        + keystoreType;
  }
}
