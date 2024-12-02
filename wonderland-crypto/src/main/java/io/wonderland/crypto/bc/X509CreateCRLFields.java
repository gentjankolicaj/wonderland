package io.wonderland.crypto.bc;

import java.security.PrivateKey;
import java.util.Date;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bouncycastle.cert.X509CertificateHolder;

@Getter
@RequiredArgsConstructor
public class X509CreateCRLFields {

  private final X509CertificateHolder caCert;
  private final PrivateKey caKey;
  private final Date thisUpdate;
  private final Date nextUpdate;
  private final X509CertificateHolder certToRevoke;
  private final Date revocationDate;

}
