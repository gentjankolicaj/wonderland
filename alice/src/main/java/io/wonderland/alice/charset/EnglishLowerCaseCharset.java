package io.wonderland.alice.charset;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;

/**
 * Uppercase & lowercase charset
 */
public class EnglishLowerCaseCharset extends Charset {

  public static final EnglishLowerCaseCharset INSTANCE = new EnglishLowerCaseCharset();
  private static final String canonicalName = "ENGLISH_UPPERCASE";
  private static final String[] aliases = {};

  protected EnglishLowerCaseCharset() {
    super(canonicalName, aliases);
  }

  @Override
  public boolean contains(Charset charset) {
    return false;
  }

  @Override
  public CharsetDecoder newDecoder() {
    return null;
  }

  @Override
  public CharsetEncoder newEncoder() {
    return null;
  }


  private static final class Encoder extends CharsetEncoder {

    protected Encoder(Charset cs, float averageBytesPerChar, float maxBytesPerChar, byte[] replacement) {
      super(cs, averageBytesPerChar, maxBytesPerChar, replacement);
    }

    @Override
    protected CoderResult encodeLoop(CharBuffer charBuffer, ByteBuffer byteBuffer) {
      return null;
    }
  }

  private static final class Decoder extends CharsetEncoder {

    protected Decoder(Charset cs, float averageBytesPerChar, float maxBytesPerChar, byte[] replacement) {
      super(cs, averageBytesPerChar, maxBytesPerChar, replacement);
    }

    @Override
    protected CoderResult encodeLoop(CharBuffer charBuffer, ByteBuffer byteBuffer) {
      return null;
    }
  }


}
