package io.wonderland.crypto.bc;

import java.security.KeyPair;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bouncycastle.cert.X509CertificateHolder;

@RequiredArgsConstructor
@Getter
public class BCX509CertificateKeyPair {

  private final KeyPair keyPair;
  private final X509CertificateHolder cert;

}
