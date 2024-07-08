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
   * @param keyAgreementAlgorithm Key agreement algorithm
   * @param aPrivateKey           Party A's private key.
   * @param bPublicKey            Party B's public key.
   * @return bytes of the generated secret.
   */
  public static byte[] generateSecret(String keyAgreementAlgorithm, PrivateKey aPrivateKey,
      PublicKey bPublicKey) throws GeneralSecurityException {
    KeyAgreement agreement = KeyAgreement.getInstance(keyAgreementAlgorithm, CSP.BC);
    agreement.init(aPrivateKey);
    agreement.doPhase(bPublicKey, true);
    byte[] secretBuffer = agreement.generateSecret();
    return Arrays.copyOfRange(secretBuffer, 0, secretBuffer.length);
  }

  /**
   * Generate an agreed secret key value .
   *
   * @param keyAgreementAlgorithm Key agreement algorithm
   * @param secretKeyAlgorithm    algorithm for secret key
   * @param aPrivateKey           Party A's private key.
   * @param bPublicKey,           Party B's public key.
   * @return the generated secret key .
   */
  public static SecretKey generateSecretKey(String keyAgreementAlgorithm, String secretKeyAlgorithm,
      PrivateKey aPrivateKey, PublicKey bPublicKey) throws GeneralSecurityException {
    KeyAgreement agreement = KeyAgreement.getInstance(keyAgreementAlgorithm, CSP.BC);
    agreement.init(aPrivateKey);
    agreement.doPhase(bPublicKey, true);
    return agreement.generateSecret(secretKeyAlgorithm);
  }

  /**
   * Generate an agreed secret key value .
   *
   * @param keyAgreementAlgorithm Key agreement algorithm
   * @param secretKeyAlgorithm    algorithm for secret key
   * @param aPrivateKey           Party A's private key.
   * @param bPublicKey,           Party B's public key.
   * @param keyMaterial           key material
   * @return the generated secret key.
   */
  public static SecretKey generateSecretKey(String keyAgreementAlgorithm, String secretKeyAlgorithm,
      PrivateKey aPrivateKey, PublicKey bPublicKey, byte[] keyMaterial)
      throws GeneralSecurityException {
    KeyAgreement agreement = KeyAgreement.getInstance(keyAgreementAlgorithm, CSP.BC);
    agreement.init(aPrivateKey, new UserKeyingMaterialSpec(keyMaterial));
    agreement.doPhase(bPublicKey, true);
    return agreement.generateSecret(secretKeyAlgorithm);
  }

  /**
   * Generate an agreed secret key value using the Unified Diffie-Hellman model.
   *
   * @param keyAgreementAlgorithm  Key agreement algorithm
   * @param secretKeyAlgorithm     algorithm for secret key
   * @param aPrivateKey            Party A's private key.
   * @param bPublicKey             Party B's public key.
   * @param algorithmParameterSpec Algorithm parameter spec (Maybe it can be DHUParameterSpec |
   *                               MQVParameterSpec...)
   * @return the generated secret key.
   */
  public static SecretKey generateSecretKey(String keyAgreementAlgorithm, String secretKeyAlgorithm,
      PrivateKey aPrivateKey, PublicKey bPublicKey, AlgorithmParameterSpec algorithmParameterSpec)
      throws GeneralSecurityException {
    KeyAgreement agreement = KeyAgreement.getInstance(keyAgreementAlgorithm, CSP.BC);
    agreement.init(aPrivateKey, algorithmParameterSpec);
    agreement.doPhase(bPublicKey, true);
    return agreement.generateSecret(secretKeyAlgorithm);
  }
}