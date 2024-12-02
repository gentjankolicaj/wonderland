package io.wonderland.rh.base.codec;


import java.util.function.Function;
import org.apache.commons.codec.binary.Base32;

public class Base32LC implements CodecAlg<byte[], String, String, byte[]> {

  private static final Base32 BASE_32_LC = new Base32(true);
  private static final String NAME = "Base32 (LC)";

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public int getRadix() {
    return 32;
  }

  @Override
  public Function<byte[], String> encode() {
    return BASE_32_LC::encodeAsString;
  }

  @Override
  public Function<String, byte[]> decode() {
    return BASE_32_LC::decode;
  }

}
