package io.wonderland.crypto.bc;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Wrapper class for sign algorithm fields
 */
@RequiredArgsConstructor
@Getter
public class SignFields {

  //csp implementation to be used for cert signing
  private final String provider;

  // the signature algorithm to sign the certificate with.
  private final String algorithm;

}
