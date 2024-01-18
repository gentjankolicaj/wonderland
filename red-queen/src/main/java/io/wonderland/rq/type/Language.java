package io.wonderland.rq.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@RequiredArgsConstructor
@Getter
public enum Language {
  AL("al", "Albanian"), EN("en", "English"), DE("de", "Deutsch");

  private final String code;
  private final String name;


  public static Language findByName(String name) {
    if (StringUtils.isEmpty(name)) {
      throw new IllegalArgumentException("Language not found with " + name);
    }

    for (Language language : values()) {
      if (language.getName().equals(name)) {
        return language;
      }
    }
    throw new IllegalArgumentException("Language not found with " + name);
  }
}
