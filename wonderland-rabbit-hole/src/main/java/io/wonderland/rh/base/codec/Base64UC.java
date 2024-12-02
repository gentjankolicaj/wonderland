package io.wonderland.rh.base.codec;

import java.util.Base64;
import java.util.function.Function;

public class Base64UC implements CodecAlg<byte[], String, String, byte[]> {

  static final String NAME = "Base64 (UC)";

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public int getRadix() {
    return 64;
  }

  @Override
  public Function<byte[], String> encode() {
    return arg -> Base64.getEncoder().encodeToString(arg).toUpperCase();
  }

  @Override
  public Function<String, byte[]> decode() {
    return arg -> Base64.getDecoder().decode(arg);
  }

}
