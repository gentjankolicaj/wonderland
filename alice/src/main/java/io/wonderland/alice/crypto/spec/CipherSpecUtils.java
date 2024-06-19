package io.wonderland.alice.crypto.spec;

import java.security.spec.AlgorithmParameterSpec;

public final class CipherSpecUtils {

  private CipherSpecUtils() {

  }

  public static Class<AlgorithmParameterSpec>[] availableAlgParamSpecs() {
    return new Class[0];
  }
}
