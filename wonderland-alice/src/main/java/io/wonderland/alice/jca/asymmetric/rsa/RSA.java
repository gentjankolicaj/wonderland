package io.wonderland.alice.jca.asymmetric.rsa;


import io.wonderland.alice.crypto.asymmetric.rsa.RSACrypt;

/**
 * RSA cipher provider implementation
 */
public class RSA extends RSACipherSpi {

  //changed to public because it was failing instantiation with reflection
  public RSA() {
    super(new RSACrypt());
  }

}
