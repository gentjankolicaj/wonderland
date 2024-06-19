package io.wonderland.alice.jca.cipher;

import io.wonderland.alice.crypto.cipher.symmetric.CaesarCrypt;


/**
 * Caesar cipher provider implementation
 */
public final class Caesar extends StreamCipherSpi {

  public Caesar() {
    super(new CaesarCrypt(), 32);
  }

}
