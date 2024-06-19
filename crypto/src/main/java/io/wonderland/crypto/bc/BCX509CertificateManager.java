package io.wonderland.crypto.bc;

import io.wonderland.crypto.Constants;
import java.security.Security;
import java.security.cert.CertificateFactory;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

@Slf4j
public class BCX509CertificateManager {

  private static final Map<String, X509CertificateHolder> certMap = new ConcurrentHashMap<>();
  private static CertificateFactory x509CertificateFactory;

  static {
    Security.addProvider(new BouncyCastleProvider());
    init();
  }


  private static void init() {
    try {
      x509CertificateFactory = CertificateFactory.getInstance("X.509", Constants.BC_CSP);
    } catch (Exception e) {
      log.error("Error on BC init().", e);
      System.exit(1);
    }
  }

}
