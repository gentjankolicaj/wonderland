package io.wonderland.crypto.bc;

import java.math.BigInteger;
import java.security.PrivateKey;
import java.util.Date;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bouncycastle.cert.X509CertificateHolder;

/**
 * Wrapper class for X509 attribute certificate fields
 */
@RequiredArgsConstructor
@Getter
public class X509AttrCertFields {

  private final X509CertificateHolder issuerCert;
  private final PrivateKey issuerKey;
  private final BigInteger serial;
  private final Date notBefore;
  private final Date notAfter;
  private final X509CertificateHolder holderCert;
  private final String holderRoleUri;


}
