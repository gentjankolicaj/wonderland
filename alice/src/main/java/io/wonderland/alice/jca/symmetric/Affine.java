package io.wonderland.alice.jca.symmetric;

import io.wonderland.alice.crypto.symmetric.AffineCrypt;

public final class Affine extends StreamCipherSpi {

  public Affine() {
    super(new AffineCrypt());
  }

}
