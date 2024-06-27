package io.wonderland.alice.crypto.mode;

import io.wonderland.alice.crypto.BlockCipher;
import io.wonderland.alice.crypto.CipherParameter;
import io.wonderland.alice.exception.RuntimeCipherException;
import lombok.RequiredArgsConstructor;

//todo: implement
@RequiredArgsConstructor
public class CTRBlockCipher implements BlockCipher {

  private final BlockCipher cipher;

  @Override
  public void init(boolean encryption, CipherParameter param) throws IllegalArgumentException {

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
      throws RuntimeCipherException, IllegalStateException {
    return 0;
  }


  @Override
  public int getOutputSize(int inputLen) {
    return 0;
  }

  @Override
  public void reset() {

  }


}
