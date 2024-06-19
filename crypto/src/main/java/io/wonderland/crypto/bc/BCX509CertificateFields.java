package io.wonderland.crypto.bc;

import java.math.BigInteger;
import java.util.Date;
import lombok.Value;
import org.bouncycastle.asn1.x500.X500Name;

@Value
public class BCX509CertificateFields {

  private final BigInteger serial;
  private final X500Name subject;
  private final X500Name issuer;
  private final Date notBefore;
  private final Date notAfter;

}
