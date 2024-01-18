package io.wonderland.rq.cryptanalysis;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class FrequencyResource {

  private final String filePath;
  private final long charsetSize;

}
