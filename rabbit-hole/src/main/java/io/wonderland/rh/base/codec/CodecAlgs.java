package io.wonderland.rh.base.codec;

import io.wonderland.rh.utils.CodecUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public final class CodecAlgs {

  private static final char COMMA_SEPARATOR = ',';
  private static final char BLANK_SEPARATOR = ' ';

  private static final List<CodecAlg<byte[], String, String, byte[]>> RUNTIME_CODEC_ALGS = createCodecAlgs();


  private CodecAlgs() {
  }

  private static List<CodecAlg<byte[], String, String, byte[]>> createCodecAlgs() {
    List<CodecAlg<byte[], String, String, byte[]>> list = new ArrayList<>();
    for (int i = Character.MIN_RADIX; i <= Character.MAX_RADIX; i++) {
      list.add(createCodecAlg(i, COMMA_SEPARATOR));
      list.add(createCodecAlg(i, BLANK_SEPARATOR));
    }
    return list;
  }

  /**
   * @param radix     min_value=>Character.MIN_RADIX & max_value<=Character.MAX_RADIX
   * @param separator integer separator
   * @return implementation instance of CodecAlg
   */
  private static CodecAlg<byte[], String, String, byte[]> createCodecAlg(int radix,
      char separator) {
    return new CodecAlg<>() {
      @Override
      public String getName() {
        return "Base" + radix + " '" + separator + "'";
      }

      @Override
      public int getRadix() {
        return radix;
      }

      @Override
      public Function<byte[], String> encode() {
        return data -> CodecUtils.encodeBaseRadix(radix, data, separator);
      }

      @Override
      public Function<String, byte[]> decode() {
        return data -> CodecUtils.decodeBaseRadix(radix, data, separator);
      }
    };
  }

  public static List<CodecAlg<byte[], String, String, byte[]>> getRuntimeCodecAlgs() {
    return RUNTIME_CODEC_ALGS;
  }

  public static List<CodecAlg<byte[], String, String, byte[]>> getImplementedCodecAlgs() {
    return List.of(new StringASCII(), new StringUTF8(), new StringUTF16(), new StringBig5(),
        new StringUTF16LE(),
        new Base16LC(), new Base16UC(),
        new Base32LC(), new Base32UC(), new Base64LC(), new Base64UC());
  }

}
