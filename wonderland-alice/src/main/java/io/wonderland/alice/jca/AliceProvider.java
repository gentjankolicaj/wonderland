package io.wonderland.alice.jca;


import java.security.Provider;

/**
 * Alice cryptographic security provider implementation.
 * <br>For more <a
 * href="https://docs.oracle.com/en/java/javase/11/security/howtoimplaprovider.html#GUID-AB9C2460-0CF2-48BA-B9FE-7059071344CE">JCA
 * implementing a provider</a>
 */
public final class AliceProvider extends Provider {

  private static final String NAME = "Alice";
  private static final String VERSION = "0.1";
  private static final String INFO = "Alice cryptographic service provider.";
  private static AliceProvider instance;

  public AliceProvider() {
    super(NAME, VERSION, INFO);
    put("Cipher.Caesar", "io.wonderland.alice.jca.symmetric.Caesar");
    put("Cipher.OTP", "io.wonderland.alice.jca.symmetric.OTP");
    put("Cipher.Vigenere", "io.wonderland.alice.jca.symmetric.Vigenere");
    put("Cipher.Vernam", "io.wonderland.alice.jca.symmetric.Vernam");
    put("Cipher.Affine", "io.wonderland.alice.jca.symmetric.Affine");
    put("Cipher.Railfence", "io.wonderland.alice.jca.symmetric.Railfence");
    put("Cipher.Monoalphabet", "io.wonderland.alice.jca.symmetric.Monoalphabet");
    put("Cipher.Permutation", "io.wonderland.alice.jca.symmetric.permutation.Permutation");
    put("Cipher.Null", "io.wonderland.alice.jca.symmetric.Null");
    put("Cipher.RSA", "io.wonderland.alice.jca.asymmetric.rsa.RSA");
    instance = this;
  }

  public static AliceProvider getInstance() {
    if (instance == null) {
      instance = new AliceProvider();
    }
    return instance;
  }

}
