package io.wonderland.crypto.merkle;

public abstract class LeafNode<T> implements Node<T> {

  private byte[] hash;
  private byte[] value;

}
