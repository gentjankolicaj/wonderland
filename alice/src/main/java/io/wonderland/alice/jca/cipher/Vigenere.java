package io.wonderland.alice.jca.cipher;


import io.wonderland.alice.crypto.cipher.symmetric.VigenereCrypt;

public final class Vigenere extends StreamCipherSpi {

  public Vigenere() {
    super(new VigenereCrypt());
  }

}
