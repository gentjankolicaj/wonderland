package io.wonderland.alice.crypto.cipher.symmetric;

import io.wonderland.alice.crypto.Algorithms;
import io.wonderland.alice.crypto.BlockCipher;
import io.wonderland.alice.crypto.CipherParameters;
import io.wonderland.alice.exception.DataLengthException;

public class DESCrypt implements BlockCipher {

  @Override
  public void init(boolean encryption, CipherParameters params) throws IllegalArgumentException {
    //todo: implement
  }

  @Override
  public String getAlgorithmName() {
    return Algorithms.DES.getName();
  }

  @Override
  public int getBlockSize() {
    return 0;
  }

  @Override
  public int processBlock(byte[] in, int inOff, byte[] out, int outOff)
      throws DataLengthException, IllegalStateException {
    return 0;
  }

  @Override
  public void reset() {
  }
}
