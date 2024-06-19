package io.wonderland.crypto.merkle;

import lombok.Getter;

@Getter
public class MerkleTree<T> extends MerkleNode<T> {

  private MerkleNode<T> left;
  private MerkleNode<T> right;

  @Override
  public byte[] getHash() {
    return new byte[0];
  }
}
