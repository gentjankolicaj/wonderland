package io.wonderland.alice.jca.symmetric;


import io.wonderland.alice.crypto.symmetric.VernamCrypt;

public final class Vernam extends StreamCipherSpi {

  public Vernam() {
    super(new VernamCrypt());
  }
}
