package io.wonderland.rh.key;

import io.wonderland.rh.key.secret.AffineKeyPane;
import io.wonderland.rh.key.secret.CaesarKeyPane;
import io.wonderland.rh.key.secret.RailfenceKeyPane;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class KeyConstants {

  private static final Map<String, Class<?>> keyPaneMap = new ConcurrentHashMap<>();

  static {
    keyPaneMap.put("Caesar", CaesarKeyPane.class);
    keyPaneMap.put("Affine", AffineKeyPane.class);
    keyPaneMap.put("Railfence", RailfenceKeyPane.class);
  }

  private KeyConstants() {
  }

  public static Map<String, Class<?>> getKeyInputPaneMap() {
    return keyPaneMap;
  }

}
