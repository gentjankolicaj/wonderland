package io.wonderland.struct;

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
      throw new IllegalArgumentException("Language not found: " + name);
    }

    for (Language language : values()) {
      if (language.getName().equals(name)) {
        return language;
      }
    }
    throw new IllegalArgumentException("Language not found: " + name);
  }

  public static Language ofNullable(String code) {
    if (StringUtils.isEmpty(code)) {
      throw new IllegalArgumentException("Language code not found: " + code);
    }

    for (Language language : values()) {
      if (language.getCode().equalsIgnoreCase(code)) {
        return language;
      }
    }
    return null;
  }
}
