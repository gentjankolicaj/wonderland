package io.wonderland.alice.crypto.mode;

import io.wonderland.alice.crypto.BlockCipher;
import io.wonderland.alice.crypto.CipherParameters;
import io.wonderland.alice.exception.DataLengthException;

//todo: implement
public class OFBBlockCipher implements BlockCipher {

  @Override
  public void init(boolean encryption, CipherParameters params) throws IllegalArgumentException {

  }

  @Override
  public String getAlgorithmName() {
    return null;
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
