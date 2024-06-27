package io.wonderland.alice.crypto.spec;

import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PBEParameterSpec;

public final class SpecUtils {

  private SpecUtils() {

  }

  public static Class<AlgorithmParameterSpec>[] availableAlgParamSpecs() {
    return new Class[0];
  }

  public static Class<AlgorithmParameterSpec>[] rsaAlgParamSpecs() {
    return new Class[]{OAEPParameterSpec.class};
  }

  public static Class<AlgorithmParameterSpec>[] blockCipherAlgParamSpecs() {
    return new Class[]{IvParameterSpec.class, PBEParameterSpec.class};
  }
}
