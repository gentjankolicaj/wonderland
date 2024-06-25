package io.wonderland.alice.jca.cipher;


import io.wonderland.alice.crypto.cipher.asymmetric.rsa.RSACrypt;
import io.wonderland.alice.crypto.padding.RSAPadding;

/**
 * RSA cipher provider implementation
 */
public class RSA extends AsymmetricCipherSpi {

  //changed to public because it was failing instantiation with reflection
  public RSA() {
    super(new RSACrypt(new RSAPadding()), null, new RSAPadding());
  }

}
