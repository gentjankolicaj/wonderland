package io.wonderland.alice.crypto.stream;


import io.wonderland.alice.exception.RuntimeCipherException;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;

public final class ResettableOutputStream extends ByteArrayOutputStream {

  public byte[] getBuffer() {
    return this.buf;
  }

  public void reset() {
    Arrays.fill(this.buf, (byte) 0);
    super.reset();
  }

  public ResettableOutputStream clone() {
    try {
      ResettableOutputStream var = (ResettableOutputStream) super.clone();
      var.buf = this.buf.clone();
      return var;
    } catch (CloneNotSupportedException e) {
      throw new RuntimeCipherException("Unexpected exception during cloning.", e);
    }
  }

}
