package io.wonderland.crypto.bc;

import java.security.PrivateKey;
import java.util.Date;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bouncycastle.cert.X509CertificateHolder;

@Getter
@RequiredArgsConstructor
public class X509EmptyCRLFields {

  private final X509CertificateHolder caCert;
  private final PrivateKey caKey;
  private final Date update;
  private final Date nextUpdate;

}
