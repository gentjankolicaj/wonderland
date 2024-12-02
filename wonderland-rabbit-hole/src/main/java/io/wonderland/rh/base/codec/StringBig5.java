package io.wonderland.rh.base.codec;


import java.nio.charset.Charset;
import java.util.function.Function;

public class StringBig5 implements CodecAlg<byte[], String, String, byte[]> {

  public static final String NAME = "StringBig5";
  private static final Charset charset = Charset.forName("Big5");

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
    return arg -> new String(arg, charset);
  }

  @Override
  public Function<String, byte[]> decode() {
    return arg -> arg.getBytes(charset);
  }

}
