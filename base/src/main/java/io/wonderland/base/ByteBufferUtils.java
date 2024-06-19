package io.wonderland.base;

import java.nio.ByteBuffer;
import java.util.Optional;

public final class ByteBufferUtils {

  public static final byte[] EMPTY_ARRAY = new byte[0];

  private ByteBufferUtils() {
  }

  public static byte[] getBytes(ByteBuffer byteBuffer) {
    int remainingBytes = byteBuffer.remaining();
    if (remainingBytes == 0) {
      return EMPTY_ARRAY;
    }
    byte[] arr = new byte[remainingBytes];
    byteBuffer.get(arr);
    return arr;
  }

  public static Optional<byte[]> getOptBytes(ByteBuffer byteBuffer) {
    int remainingBytes = byteBuffer.remaining();
    if (remainingBytes == 0) {
      return Optional.empty();
    }
    byte[] arr = new byte[remainingBytes];
    byteBuffer.get(arr);
    return Optional.of(arr);
  }


}
