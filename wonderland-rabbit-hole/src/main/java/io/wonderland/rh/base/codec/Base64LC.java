package io.wonderland.rh.base.codec;

import java.util.Base64;
import java.util.function.Function;

public class Base64LC implements CodecAlg<byte[], String, String, byte[]> {

  public static final String NAME = "Base64 (LC)";

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
    return arg -> Base64.getEncoder().encodeToString(arg).toLowerCase();
  }

  @Override
  public Function<String, byte[]> decode() {
    return arg -> Base64.getDecoder().decode(arg);
  }

}
