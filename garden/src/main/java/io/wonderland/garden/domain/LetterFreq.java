package io.wonderland.garden.domain;

import java.util.Set;
import java.util.TreeSet;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.collections4.CollectionUtils;

@Getter
@AllArgsConstructor
public class LetterFreq {

  public static final String DEFAULT_KEY = "letter_freq";
  private String key;
  private Set<String> langCodes;

  public LetterFreq(Set<String> langCodes) {
    this.key = DEFAULT_KEY;
    this.langCodes = langCodes;
  }


  public void setLangCode(String code) {
    if (CollectionUtils.isEmpty(langCodes)) {
      this.langCodes = new TreeSet<>();
    }
    this.langCodes.add(code);
  }

}
