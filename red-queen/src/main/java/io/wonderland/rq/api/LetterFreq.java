package io.wonderland.rq.api;

import io.wonderland.base.Api;
import io.wonderland.struct.Char;
import io.wonderland.struct.Language;
import java.util.Map;
import java.util.function.Function;

public class LetterFreq extends Api {


  public Function<Language, Map<? extends Char, Double>> monogram() {
    return null;
  }

  public Function<Language, Map<? extends Char, Double>> digram() {
    return null;
  }

  public Function<Language, Map<? extends Char, Double>> trigram() {
    return null;
  }


}
