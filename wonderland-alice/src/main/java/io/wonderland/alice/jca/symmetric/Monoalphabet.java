package io.wonderland.alice.jca.symmetric;

import io.wonderland.alice.crypto.symmetric.MonoalphabetCrypt;

public final class Monoalphabet extends StreamCipherSpi {

  public Monoalphabet() {
    super(new MonoalphabetCrypt());
  }
}
