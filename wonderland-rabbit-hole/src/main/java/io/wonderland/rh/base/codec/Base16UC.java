package io.wonderland.rh.base.codec;


import java.util.function.Function;
import org.apache.commons.codec.binary.Base16;

public class Base16UC implements CodecAlg<byte[], String, String, byte[]> {

  private static final Base16 BASE_16_UC = new Base16(false);
  private static final String NAME = "Base16 (UC)";

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public int getRadix() {
    return 16;
  }

  @Override
  public Function<byte[], String> encode() {
    return BASE_16_UC::encodeAsString;
  }

  @Override
  public Function<String, byte[]> decode() {
    return BASE_16_UC::decode;
  }

}
