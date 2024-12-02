package io.wonderland.alice.jca.symmetric;

import io.wonderland.alice.crypto.symmetric.NullCrypt;

public final class Null extends StreamCipherSpi {

  public Null() {
    super(new NullCrypt());
  }

}
