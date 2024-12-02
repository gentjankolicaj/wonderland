package io.wonderland.crypto.store;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * For more on java 11 security specs : <a
 * href="https://docs.oracle.com/en/java/javase/11/docs/specs/security/standard-names.html#keystore-types">Java
 * 11 security specs</a>
 */
@Getter
@RequiredArgsConstructor
public enum KeyStoreType {

  JCEKS(0, "JCEKS"), JKS(1, "JKS"),
  DKS(2, "DKS"),
  PKCS11(3, "PKCS11"), PKCS12(4, "PKCS12"),
  BOUNCY_CASTLE_BKS(5, "BKS"), BOUNCY_CASTLE_UBER(6, "UBER"),
  BOUNCY_CASTLE_FIPS(7, "FIPS"), BOUNCY_CASTLE_FKS(8, "BCFKS");

  private final int index;
  private final String name;

}
