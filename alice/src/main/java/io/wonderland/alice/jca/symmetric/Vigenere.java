package io.wonderland.alice.jca.symmetric;


import io.wonderland.alice.crypto.symmetric.VigenereCrypt;

public final class Vigenere extends StreamCipherSpi {

  public Vigenere() {
    super(new VigenereCrypt());
  }

}
