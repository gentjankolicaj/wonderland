package io.wonderland.crypto.bc;

import java.math.BigInteger;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bouncycastle.asn1.x500.X500Name;

/**
 * Wrapper class for X509 self-signed certificate fields
 */
@RequiredArgsConstructor
@Getter
public class X509RootCertFields {

  private final PrivateKey privateKey;
  private final PublicKey publicKey;
  private final BigInteger serial;
  private final Date notBefore;
  private final Date notAfter;
  private final X500Name subject;
}
