package io.wonderland.crypto;

import java.security.Security;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class AbstractTest {

  static final String CSP_NAME = "BC";

  static {
    Security.addProvider(new BouncyCastleProvider());
  }

}
