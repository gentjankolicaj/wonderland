package io.wonderland.struct;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum GramType {

  MONOGRAM("monogram"), DIGRAM("digram"), TRIGRAM("trigram");

  private final String value;

}
