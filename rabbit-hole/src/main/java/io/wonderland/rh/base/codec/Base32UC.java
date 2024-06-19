package io.wonderland.rh.base.codec;


import java.util.function.Function;
import org.apache.commons.codec.binary.Base32;

public class Base32UC implements CodecAlg<byte[], String, String, byte[]> {

  private static final Base32 BASE_32_UC = new Base32(false);
  private static final String NAME = "Base32 (UC)";

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
    return BASE_32_UC::encodeAsString;
  }

  @Override
  public Function<String, byte[]> decode() {
    return BASE_32_UC::decode;
  }

}
