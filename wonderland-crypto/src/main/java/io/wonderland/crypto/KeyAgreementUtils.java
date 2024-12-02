package io.wonderland.crypto;

import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.KeyAgreement;
import javax.crypto.SecretKey;
import org.bouncycastle.jcajce.spec.UserKeyingMaterialSpec;
import org.bouncycastle.util.Arrays;

public final class KeyAgreementUtils {


  private KeyAgreementUtils() {
  }

  /**
   * Generate an agreed secret byte value .
   *
   * @param provider  cryptographic service provider
   * @param algorithm key agreement algorithm
   * @param aPriv     Party A's private key.
   * @param bPub      Party B's public key.
   * @return bytes of the generated secret.
   */
  public static byte[] generateSecret(String provider, String algorithm,
      PrivateKey aPriv, PublicKey bPub) throws GeneralSecurityException {
    KeyAgreement agreement = KeyAgreement.getInstance(algorithm, provider);
    agreement.init(aPriv);
    agreement.doPhase(bPub, true);
    byte[] secretBuffer = agreement.generateSecret();
    return Arrays.copyOfRange(secretBuffer, 0, secretBuffer.length);
  }

  /**
   * Generate an agreed secret key value .
   *
   * @param provider           cryptographic service provider
   * @param algorithm          key agreement algorithm
   * @param secretKeyAlgorithm algorithm for secret key
   * @param aPriv              Party A's private key.
   * @param bPub,              Party B's public key.
   * @return the generated secret key .
   */
  public static SecretKey generateSecretKey(String provider, String algorithm,
      String secretKeyAlgorithm, PrivateKey aPriv, PublicKey bPub) throws GeneralSecurityException {
    KeyAgreement agreement = KeyAgreement.getInstance(algorithm, provider);
    agreement.init(aPriv);
    agreement.doPhase(bPub, true);
    return agreement.generateSecret(secretKeyAlgorithm);
  }

  /**
   * Generate an agreed secret key value .
   *
   * @param provider           cryptographic service provider
   * @param algorithm          key agreement algorithm
   * @param secretKeyAlgorithm algorithm for secret key
   * @param aPriv              Party A's private key.
   * @param bPub,              Party B's public key.
   * @param keyMaterial        key material
   * @return the generated secret key.
   */
  public static SecretKey generateSecretKey(String provider, String algorithm,
      String secretKeyAlgorithm, PrivateKey aPriv, PublicKey bPub, byte[] keyMaterial)
      throws GeneralSecurityException {
    KeyAgreement agreement = KeyAgreement.getInstance(algorithm, provider);
    agreement.init(aPriv, new UserKeyingMaterialSpec(keyMaterial));
    agreement.doPhase(bPub, true);
    return agreement.generateSecret(secretKeyAlgorithm);
  }

  /**
   * Generate an agreed secret key value using the Unified Diffie-Hellman model.
   *
   * @param provider               cryptographic service provider
   * @param algorithm              Key agreement algorithm
   * @param secretKeyAlgorithm     algorithm for secret key
   * @param aPriv                  Party A's private key.
   * @param bPub                   Party B's public key.
   * @param algorithmParameterSpec Algorithm parameter spec (Maybe it can be DHUParameterSpec |
   *                               MQVParameterSpec...)
   * @return the generated secret key.
   */
  public static SecretKey generateSecretKey(String provider, String algorithm,
      String secretKeyAlgorithm, PrivateKey aPriv, PublicKey bPub,
      AlgorithmParameterSpec algorithmParameterSpec) throws GeneralSecurityException {
    KeyAgreement agreement = KeyAgreement.getInstance(algorithm, provider);
    agreement.init(aPriv, algorithmParameterSpec);
    agreement.doPhase(bPub, true);
    return agreement.generateSecret(secretKeyAlgorithm);
  }

}