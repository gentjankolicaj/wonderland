package io.wonderland.rh.base.codec;

import java.util.function.Function;

/**
 * CodecAlg an algorithm that encodes or decodes a data stream or signal.CodecAlg is comprised 2
 * functions encode & decode.
 *
 * @param <T1> Encode func parameter type
 * @param <R1> Encode func return type
 * @param <T2> Decode func param type
 * @param <R2> Decode func return type
 */
public interface CodecAlg<T1, R1, T2, R2> {

  Function<T1, R1> encode();

  Function<T2, R2> decode();

  /**
   * Parsing radix for encode/decode or modulo.
   *
   * @return default is 2 (because of minimal base 2, example a mod 2)
   */
  default int getRadix() {
    return 2;
  }

  default String getName() {
    return CodecAlg.class.getSimpleName();
  }

}
