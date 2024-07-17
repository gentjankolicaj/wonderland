package io.wonderland.crypto.bc;

import java.math.BigInteger;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.cert.X509CertificateHolder;

/**
 * Wrapper class for X509 intermediate certificate fields
 */
@RequiredArgsConstructor
@Getter
public class X509InterCertFields {

  /**
   * @param trustCert    certificate carrying the public key that will later be used to verify this
   * certificate's signature.
   */
  private final X509CertificateHolder trustCert;

  /**
   * @param trustKey     private key used to generate the signature in the certificate.
   */
  private final PrivateKey trustKey;
  private final BigInteger serial;
  private final Date notBefore;
  private final Date notAfter;
  private final X500Name subject;

  /**
   * @param certKey       public key to be installed in the certificate.
   */
  private final PublicKey certKey;

  private final int followingCACerts;

}
