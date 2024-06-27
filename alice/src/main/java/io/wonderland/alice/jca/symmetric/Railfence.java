package io.wonderland.alice.jca.symmetric;

import io.wonderland.alice.crypto.symmetric.RailfenceCrypt;

public final class Railfence extends StreamCipherSpi {

  public Railfence() {
    super(new RailfenceCrypt());
  }

}
