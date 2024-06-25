package io.wonderland.alice.crypto.cipher.symmetric;

import io.wonderland.alice.crypto.Algorithms;
import io.wonderland.alice.crypto.CipherParameters;
import io.wonderland.alice.crypto.StreamCipher;
import io.wonderland.alice.crypto.key.NullSecretKey;
import io.wonderland.alice.exception.DataLengthException;

/**
 * Null cipher implementation.
 * <br><a
 * href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/javax/crypto/NullCipher.html">NullCipher
 * oracle</a>
 */
public final class NullCrypt implements StreamCipher {

  @Override
  public void init(boolean encryption, CipherParameters params) throws IllegalArgumentException {
    //do nothing because in null cipher because plaintext=ciphertext
  }

  @Override
  public String getAlgorithmName() {
    return Algorithms.NULL.getName();
  }

  @Override
  public String[] getKeyTypeNames() {
    return new String[]{NullSecretKey.class.getName()};
  }

  @Override
  public byte processByte(byte in) {
    return in;
  }

  @Override
  public int processBytes(byte[] in, int inOff, int len, byte[] out, int outOff)
      throws DataLengthException {
    int processed = in.length - inOff;
    for (int i = 0; i + inOff < in.length; i++) {
      out[i + outOff] = in[i + outOff];
    }
    return processed;
  }

  @Override
  public void reset() {
    //do nothing in null cipher
  }

}
