package io.wonderland.rh.base.fx;

import io.wonderland.rh.base.codec.CodecAlg;
import java.util.function.Function;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class CodecDropdownItem {

  private final String key;
  private final CodecAlg<byte[], String, String, byte[]> codecAlg;
  private final Function<CodecAlg<byte[], String, String, byte[]>, Void> func;

  public CodecDropdownItem(CodecAlg<byte[], String, String, byte[]> codecAlg,
      Function<CodecAlg<byte[], String, String, byte[]>, Void> func) {
    this.key = codecAlg.getName();
    this.codecAlg = codecAlg;
    this.func = func;
  }
}