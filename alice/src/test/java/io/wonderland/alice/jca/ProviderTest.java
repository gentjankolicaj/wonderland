package io.wonderland.alice.jca;

import java.security.Security;

public class ProviderTest {

  static {
    Security.addProvider(new AliceProvider());
  }

}
