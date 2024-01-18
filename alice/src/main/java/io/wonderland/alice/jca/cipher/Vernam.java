package io.wonderland.alice.jca.cipher;


import io.wonderland.alice.crypto.cipher.symmetric.VernamCrypt;

public final class Vernam extends StreamCipherSpi {

  public Vernam() {
    super(new VernamCrypt());
  }
}
