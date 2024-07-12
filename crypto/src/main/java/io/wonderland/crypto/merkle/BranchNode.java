package io.wonderland.crypto.merkle;

import lombok.Getter;

@Getter
public abstract class BranchNode<T> implements Node<T> {

  private Node<T> left;
  private Node<T> right;
}
