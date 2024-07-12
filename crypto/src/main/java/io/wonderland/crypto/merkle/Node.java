package io.wonderland.crypto.merkle;

public interface Node<T> {

  byte[] getValue();

  byte[] getHash();


}
