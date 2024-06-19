package io.wonderland.garden.redis;

import io.lettuce.core.codec.RedisCodec;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;
import io.wonderland.base.ByteBufferUtils;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;
import org.apache.commons.lang3.StringUtils;

public final class BAValueCodec implements RedisCodec<String, byte[]> {

  private static final BAValueCodec INSTANCE = new BAValueCodec();
  private static final Charset charset = Charset.defaultCharset();

  private BAValueCodec() {
  }

  public static BAValueCodec getInstance() {
    return INSTANCE;
  }

  @Override
  public String decodeKey(ByteBuffer bbKey) {
    return Unpooled.wrappedBuffer(bbKey).toString(charset);
  }

  @Override
  public byte[] decodeValue(ByteBuffer bbValue) {
    return ByteBufferUtils.getBytes(bbValue);
  }

  @Override
  public ByteBuffer encodeKey(String key) {
    return allocateBuffer(key);
  }

  @Override
  public ByteBuffer encodeValue(byte[] value) {
    return ByteBuffer.wrap(value);
  }


  private ByteBuffer allocateBuffer(String key) {
    if (StringUtils.isEmpty(key)) {
      return ByteBuffer.wrap(ByteBufferUtils.EMPTY_ARRAY);
    }
    ByteBuffer buffer = ByteBuffer.allocate(
        (int) (CharsetUtil.encoder(charset).maxBytesPerChar() * key.length()));

    ByteBuf byteBuffer = Unpooled.wrappedBuffer(buffer);
    byteBuffer.clear();
    encode(key, byteBuffer);
    buffer.limit(byteBuffer.writerIndex());
    return buffer;
  }


  private void encode(String payload, ByteBuf target) {
    CharsetEncoder encoder = CharsetUtil.encoder(charset);
    int length = (int) ((double) payload.length() * encoder.maxBytesPerChar());
    target.ensureWritable(length);
    try {
      final ByteBuffer dstBuf = target.nioBuffer(0, length);
      final int pos = dstBuf.position();
      CoderResult cr = encoder.encode(CharBuffer.wrap(payload), dstBuf, true);
      if (!cr.isUnderflow()) {
        cr.throwException();
      }
      cr = encoder.flush(dstBuf);
      if (!cr.isUnderflow()) {
        cr.throwException();
      }
      target.writerIndex(target.writerIndex() + dstBuf.position() - pos);
    } catch (CharacterCodingException x) {
      throw new IllegalStateException(x);
    }
  }

}
