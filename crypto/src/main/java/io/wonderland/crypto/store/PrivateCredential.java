package io.wonderland.crypto.store;

import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * wrapper class for a private key and its corresponding public key certificate.
 * <p>
 * The regular Java API also has javax.security.auth.x500.X500PrivateCredential. There is also a
 * slightly more general class in the BC PKIX API - org.bouncycastle.pkix.PKIXIdentity.
 * </p>
 */

@RequiredArgsConstructor
@Getter
public class PrivateCredential {

  private final X509Certificate x509Certificate;
  private final PrivateKey privateKey;

}