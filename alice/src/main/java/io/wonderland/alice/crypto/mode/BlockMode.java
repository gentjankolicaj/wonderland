package io.wonderland.alice.crypto.mode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@RequiredArgsConstructor
@Getter
public enum BlockMode {

  ECB("ECB"), CBC("CBC"), CTR("CTR");

  private final String value;


  public static BlockMode parse(String modeName) {
    if (StringUtils.isEmpty(modeName)) {
      throw new IllegalArgumentException("Unknown block mode: " + modeName);
    }
    for (BlockMode mode : values()) {
      if (mode.name().equalsIgnoreCase(modeName)) {
        return mode;
      }
    }
    throw new IllegalArgumentException("Unknown block mode: " + modeName);
  }

}
