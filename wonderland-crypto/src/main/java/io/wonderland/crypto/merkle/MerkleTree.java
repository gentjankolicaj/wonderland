package io.wonderland.crypto.merkle;

import lombok.Getter;

//todo:
@Getter
public abstract class MerkleTree<T> extends BranchNode<T> {

  private BranchNode<T> left;
  private BranchNode<T> right;

}
