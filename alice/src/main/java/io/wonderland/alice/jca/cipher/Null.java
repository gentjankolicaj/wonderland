package io.wonderland.alice.jca.cipher;

import io.wonderland.alice.crypto.cipher.symmetric.NullCrypt;

public final class Null extends StreamCipherSpi {

  public Null() {
    super(new NullCrypt());
  }

}
