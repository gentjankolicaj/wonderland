package io.wonderland.alice.jca.cipher;

import io.wonderland.alice.crypto.cipher.symmetric.MonoalphabetCrypt;

public final class Monoalphabet extends StreamCipherSpi {

  public Monoalphabet() {
    super(new MonoalphabetCrypt());
  }
}
