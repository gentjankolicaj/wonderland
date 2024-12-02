package io.wonderland.rh.base.codec;


import io.wonderland.rh.utils.CodecUtils;
import java.util.function.Function;

public class Base10Blank implements CodecAlg<byte[], String, String, byte[]> {

  public static final String NAME = "Base10";
  private static final char SEPARATOR = ' ';

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public int getRadix() {
    return 10;
  }

  @Override
  public Function<byte[], String> encode() {
    return arg -> CodecUtils.encodeBase10(arg, SEPARATOR);
  }

  @Override
  public Function<String, byte[]> decode() {
    return arg -> CodecUtils.decodeBase10(arg, SEPARATOR);
  }

}
