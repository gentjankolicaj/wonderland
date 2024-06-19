package io.wonderland.garden.domain;

import java.util.Map;
import lombok.Value;

@Value
public class Grapheme {

  String key;
  Map<String, byte[]> freq;

}
