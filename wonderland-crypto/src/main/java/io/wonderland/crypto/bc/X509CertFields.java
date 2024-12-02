package io.wonderland.crypto.bc;

import java.math.BigInteger;
import java.util.Date;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bouncycastle.asn1.x500.X500Name;

/**
 * Wrapper class for X509 certificate fields
 */
@RequiredArgsConstructor
@Getter
public class X509CertFields {

  private final BigInteger serial;
  private final X500Name issuer;
  private final Date notBefore;
  private final Date notAfter;
  private final X500Name subject;
}
