package io.wonderland.base;


import java.util.LinkedList;
import org.apache.commons.lang3.ArrayUtils;

public final class ByteArrayBuilder {

  public static final byte[] ZERO_BYTES = new byte[0];
  private final LinkedList<byte[]> bufferedBlocks;

  public ByteArrayBuilder() {
    this(null);
  }

  public ByteArrayBuilder(byte[] initialBlock) {
    this.bufferedBlocks = new LinkedList<>();
    if (ArrayUtils.isNotEmpty(initialBlock)) {
      this.bufferedBlocks.add(initialBlock);
    }
  }

  public ByteArrayBuilder append(int b8) {
    this.bufferedBlocks.add(new byte[]{(byte) b8});
    return this;
  }

  public ByteArrayBuilder appendOptimal(int i) {
    this.bufferedBlocks.add(IntUtils.getOptimalBytesBE(i));
    return this;
  }


  public ByteArrayBuilder append(char c) {
    this.bufferedBlocks.add(CharUtils.getOptimalBytesBE(c));
    return this;
  }

  public ByteArrayBuilder appendOptimal(char c) {
    this.bufferedBlocks.add(CharUtils.getOptimalBytesBE(c));
    return this;
  }

  public ByteArrayBuilder appendTwoBytes(int b16) {
    this.bufferedBlocks.add(new byte[]{(byte) (b16 >> 8), (byte) b16});
    return this;
  }

  public ByteArrayBuilder appendThreeBytes(int b24) {
    this.bufferedBlocks.add(new byte[]{(byte) (b24 >> 16), (byte) (b24 >> 8), (byte) b24});
    return this;
  }

  public ByteArrayBuilder appendFourBytes(int b32) {
    this.bufferedBlocks.add(
        new byte[]{(byte) (b32 >> 24), (byte) (b32 >> 16), (byte) (b32 >> 8), (byte) b32,});
    return this;
  }

  public ByteArrayBuilder append(byte[] array) {
    if (ArrayUtils.isNotEmpty(array)) {
      this.bufferedBlocks.add(array);
    }
    return this;
  }

  public byte[] toByteArray() {
    int totalLength = bufferedBlocks.stream().mapToInt(arr -> arr.length).sum();
    if (totalLength == 0) {
      return ZERO_BYTES;
    } else {

      //copy buffered blocks in final array
      byte[] finalBlock = new byte[totalLength];
      int offset = 0;

      for (byte[] block : bufferedBlocks) {
        System.arraycopy(block, 0, finalBlock, offset, block.length);
        offset += block.length;
      }
      return finalBlock;
    }
  }

}
