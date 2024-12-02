package io.wonderland.base;


import java.util.LinkedList;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;

public final class ByteArrayBuilder {

  public static final byte[] ZERO_BYTES = new byte[0];
  private final List<byte[]> bufferedArrays;

  public ByteArrayBuilder() {
    this(null);
  }

  public ByteArrayBuilder(byte[] initialBlock) {
    this.bufferedArrays = new LinkedList<>();
    if (ArrayUtils.isNotEmpty(initialBlock)) {
      this.bufferedArrays.add(initialBlock);
    }
  }

  public static byte[] toByteArray(List<byte[]> bufferedArrays) {
    if (CollectionUtils.isEmpty(bufferedArrays)) {
      return new byte[0];
    } else {
      int totalLength = bufferedArrays.stream().mapToInt(arr -> arr.length).sum();
      if (totalLength == 0) {
        return ZERO_BYTES;
      } else {

        //copy buffered blocks in final array
        byte[] finalBlock = new byte[totalLength];
        int offset = 0;

        for (byte[] block : bufferedArrays) {
          System.arraycopy(block, 0, finalBlock, offset, block.length);
          offset += block.length;
        }
        return finalBlock;
      }
    }
  }

  public ByteArrayBuilder append(int b8) {
    this.bufferedArrays.add(new byte[]{(byte) b8});
    return this;
  }

  public ByteArrayBuilder appendOptimal(int i) {
    this.bufferedArrays.add(IntUtils.getOptimalBytesBE(i));
    return this;
  }

  public ByteArrayBuilder append(char c) {
    this.bufferedArrays.add(CharUtils.getOptimalBytesBE(c));
    return this;
  }

  public ByteArrayBuilder appendOptimal(char c) {
    this.bufferedArrays.add(CharUtils.getOptimalBytesBE(c));
    return this;
  }

  public ByteArrayBuilder appendTwoBytes(int b16) {
    this.bufferedArrays.add(new byte[]{(byte) (b16 >> 8), (byte) b16});
    return this;
  }

  public ByteArrayBuilder appendThreeBytes(int b24) {
    this.bufferedArrays.add(new byte[]{(byte) (b24 >> 16), (byte) (b24 >> 8), (byte) b24});
    return this;
  }

  public ByteArrayBuilder appendFourBytes(int b32) {
    this.bufferedArrays.add(
        new byte[]{(byte) (b32 >> 24), (byte) (b32 >> 16), (byte) (b32 >> 8), (byte) b32,});
    return this;
  }

  public ByteArrayBuilder append(byte[] array) {
    if (ArrayUtils.isNotEmpty(array)) {
      this.bufferedArrays.add(array);
    }
    return this;
  }

  public byte[] toByteArray() {
    int totalLength = bufferedArrays.stream().mapToInt(arr -> arr.length).sum();
    if (totalLength == 0) {
      return ZERO_BYTES;
    } else {

      //copy buffered blocks in final array
      byte[] finalBlock = new byte[totalLength];
      int offset = 0;

      for (byte[] block : bufferedArrays) {
        System.arraycopy(block, 0, finalBlock, offset, block.length);
        offset += block.length;
      }
      return finalBlock;
    }
  }

}
