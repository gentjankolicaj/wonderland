package io.wonderland.alice.jca.symmetric.permutation;

import io.wonderland.alice.crypto.symmetric.PermutationCrypt;

/**
 * Permutation cipher implementation.
 */
public final class Permutation extends PermutationCipherSpi {

  public Permutation() {
    super(new PermutationCrypt());
  }

}
