package io.wonderland.garden.resource;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class LangResourceParam {

  private static final long LETTER_SET_SIZE = 4_500_000_000L; //total characters frequency evaluated.
  private final String filePath;

}
