package io.wonderland.crypto;

import io.wonderland.crypto.CSPContext.CSPContextImpl;

public final class CSP {

  /**
   * Names of Cryptographic Service Providers to be passed as arguments into methods of engine
   * classes.
   */
  public static final String BC = "BC";
  public static final String SUN = "SUN";
  public static final String SunJCE = "SunJCE";

  public static final CSPContext INSTANCE_CONTEXT = new CSPContextImpl(BC);

  private CSP() {
  }

}
