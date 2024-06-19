package io.wonderland.alice.jca.cipher;


import io.wonderland.alice.crypto.cipher.symmetric.PermutationCrypt;

public final class Permutation {

  private Permutation() {
  }

  public static class ECB extends BlockCipherSpi {

    public ECB() {
      super(new PermutationCrypt());
    }
  }

}
