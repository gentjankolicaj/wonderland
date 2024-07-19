package io.wonderland.crypto.store;

import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertStore;
import java.security.cert.CertStoreParameters;

public final class CertStoreUtils {


  private CertStoreUtils() {
  }

  /**
   * @param provider cryptographic service provider
   * @param type     cert store type
   * @param params   cert store params
   * @return
   */
  public static CertStore getInstance(String provider, String type, CertStoreParameters params)
      throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException {
    CertStore certStore = CertStore.getInstance(type, params, provider);
    return certStore;
  }


}
