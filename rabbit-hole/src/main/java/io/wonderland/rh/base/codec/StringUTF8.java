package io.wonderland.rh.base.codec;


import java.nio.charset.StandardCharsets;
import java.util.function.Function;

public class StringUTF8 implements CodecAlg<byte[], String, String, byte[]> {

  public static final String NAME = "StringUTF-8";

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public int getRadix() {
    return -1;
  }

  @Override
  public Function<byte[], String> encode() {
    return arg -> new String(arg, StandardCharsets.UTF_8);
  }

  @Override
  public Function<String, byte[]> decode() {
    return arg -> arg.getBytes(StandardCharsets.UTF_8);
  }

}
