package io.wonderland.rh.cipher;

import io.wonderland.rh.cipher.key.AffineKeyPane;
import io.wonderland.rh.cipher.key.CaesarKeyPane;
import io.wonderland.rh.cipher.key.RailfenceKeyPane;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class CipherConstants {

  private CipherConstants() {
  }

  private static final Map<String, Class<?>> keyPaneMap = new ConcurrentHashMap<>();

  static {
    keyPaneMap.put("Caesar", CaesarKeyPane.class);
    keyPaneMap.put("Affine", AffineKeyPane.class);
    keyPaneMap.put("Railfence", RailfenceKeyPane.class);
  }

  public static Map<String, Class<?>> getKeyPaneMappings() {
    return keyPaneMap;
  }

}
