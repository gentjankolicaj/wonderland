package io.wonderland.rh.digest;

import io.wonderland.alice.jca.AliceProvider;
import java.security.Security;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

class DigestTabTest {

  static {
    Security.addProvider(new BouncyCastleProvider());
    Security.addProvider(new AliceProvider());
  }


}