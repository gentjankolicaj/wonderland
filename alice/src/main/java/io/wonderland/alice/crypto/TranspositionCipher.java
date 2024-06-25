package io.wonderland.alice.crypto;

import io.wonderland.alice.exception.CipherException;


public interface TranspositionCipher extends ICipher {

  void transpose(byte[] in, byte[] out) throws CipherException;

}
