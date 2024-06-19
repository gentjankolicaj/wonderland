package io.wonderland.alice.crypto.mode;

import io.wonderland.alice.crypto.BlockCipher;
import io.wonderland.alice.crypto.CipherParameters;
import io.wonderland.alice.exception.DataLengthException;

public final class ECBBlockCipher implements BlockCipher {

  private final BlockCipher blockCipher;
  private final int bitBlockSize;

  public ECBBlockCipher(BlockCipher blockCipher, int bitBlockSize) {
    this.blockCipher = blockCipher;
    this.bitBlockSize = bitBlockSize;
  }

  @Override
  public void init(boolean encryption, CipherParameters params) throws IllegalArgumentException {

  }

  @Override
  public String getAlgorithmName() {
    return this.blockCipher.getAlgorithmName() + "/ECB";
  }

  @Override
  public int getBlockSize() {
    return blockCipher.getBlockSize();
  }

  @Override
  public int processBlock(byte[] in, int inOff, byte[] out, int outOff)
      throws DataLengthException, IllegalStateException {
    return 0;
  }

  @Override
  public void reset() {
    this.blockCipher.reset();
  }
}
