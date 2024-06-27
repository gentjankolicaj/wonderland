package io.wonderland.alice.crypto.mode;

import io.wonderland.alice.crypto.BlockCipher;
import io.wonderland.alice.crypto.CipherParameter;
import io.wonderland.alice.exception.RuntimeCipherException;
import lombok.RequiredArgsConstructor;

//todo: implement

@RequiredArgsConstructor
public final class ECBBlockCipher implements BlockCipher {

  private final BlockCipher blockCipher;


  @Override
  public void init(boolean encryption, CipherParameter params) throws IllegalArgumentException {

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
      throws RuntimeCipherException, IllegalStateException {
    return 0;
  }

  @Override
  public int getOutputSize(int inputLen) {
    return 0;
  }

  @Override
  public void reset() {
    this.blockCipher.reset();
  }


}
