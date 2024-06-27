package io.wonderland.alice.crypto.padding;

import io.wonderland.alice.exception.GenericPaddingException;
import java.security.SecureRandom;

public final class BytePadding implements BlockPadding<Byte> {

  public static final String PADDING = "BytePadding";
  private final byte b;

  public BytePadding(byte b) {
    this.b = b;
  }

  @Override
  public void init(SecureRandom random) throws IllegalArgumentException {
    //do nothing since we are padding determined byte
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
      in[inOff] = b;
      inOff++;
    }
    return added;
  }

  @Override
  public int padCount(byte[] in) throws GenericPaddingException {
    int len = in.length;
    while (len > 0) {
      if (in[len - 1] != b) {
        break;
      }
      len--;
    }
    return in.length - len;
  }

  @Override
  public Byte getPad() {
    return b;
  }
}
