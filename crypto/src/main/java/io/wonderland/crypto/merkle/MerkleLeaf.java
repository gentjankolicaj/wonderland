package io.wonderland.crypto.merkle;

public class MerkleLeaf<T> extends MerkleNode<T> {

  @Override
  public byte[] getHash() {
    return new byte[0];
  }


}
