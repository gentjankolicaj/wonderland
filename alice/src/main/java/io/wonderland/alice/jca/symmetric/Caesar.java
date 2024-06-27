package io.wonderland.alice.jca.symmetric;

import io.wonderland.alice.crypto.symmetric.CaesarCrypt;


/**
 * Caesar cipher provider implementation
 */
public final class Caesar extends StreamCipherSpi {

  public Caesar() {
    super(new CaesarCrypt(), 32);
  }

}
