package io.wonderland.alice.jca.cipher;

import io.wonderland.alice.crypto.cipher.symmetric.RailfenceCrypt;

public final class Railfence extends StreamCipherSpi {

  public Railfence() {
    super(new RailfenceCrypt());
  }

}
