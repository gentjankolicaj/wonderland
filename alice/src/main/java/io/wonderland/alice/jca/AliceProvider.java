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
  private static final double VERSION = 0.1;
  private static final String INFO = "Alice cryptographic service provider.";
  private static AliceProvider instance;

  public AliceProvider() {
    super(NAME, VERSION, INFO);

    put("Cipher.Caesar", "io.wonderland.alice.jca.cipher.Caesar");
    put("Cipher.OTP", "io.wonderland.alice.jca.cipher.OTP");
    put("Cipher.Vigenere", "io.wonderland.alice.jca.cipher.Vigenere");
    put("Cipher.Vernam", "io.wonderland.alice.jca.cipher.Vernam");
    put("Cipher.Affine", "io.wonderland.alice.jca.cipher.Affine");
    put("Cipher.Railfence", "io.wonderland.alice.jca.cipher.Railfence");
    put("Cipher.Monoalphabet", "io.wonderland.alice.jca.cipher.Monoalphabet");
    put("Cipher.Permutation", "io.wonderland.alice.jca.cipher.Permutation");
    put("Cipher.Null", "io.wonderland.alice.jca.cipher.Null");
    put("Cipher.RSA", "io.wonderland.alice.jca.cipher.RSA");
    instance = this;
  }

  public static AliceProvider getInstance() {
    if (instance == null) {
      instance = new AliceProvider();
    }
    return instance;
  }

}
