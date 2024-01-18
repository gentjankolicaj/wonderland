package io.wonderland.alice.jca.cipher;

import io.wonderland.alice.crypto.cipher.symmetric.AffineCrypt;

public final class Affine extends StreamCipherSpi {

  public Affine() {
    super(new AffineCrypt());
  }

}
