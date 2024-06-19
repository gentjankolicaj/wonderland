package io.wonderland.alice.crypto.padding;

import io.wonderland.alice.exception.PaddingException;
import java.security.SecureRandom;

public final class OnePadding implements BlockPadding<Byte> {

  public static final String PADDING = "OnePadding";
  private static final int B = 1;

  @Override
  public void init(SecureRandom random) throws IllegalArgumentException {
    //do nothing since we are padding determined int
  }

  @Override
  public String getPaddingName() {
    return PADDING;
  }

  @Override
  public int addPadding(byte[] in, int inOff) {
    int len = in.length;
    int added = len - inOff;
    while (inOff < len) {
      in[inOff] = B;
      inOff++;
    }
    return added;
  }

  @Override
  public int padCount(byte[] in) throws PaddingException {
    int counter = in.length;
    while (counter > 0) {
      if (in[counter - 1] != B) {
        break;
      }
      counter--;
    }
    return in.length - counter;
  }

  @Override
  public Byte getPad() {
    return B;
  }

}
