package io.wonderland.crypto;

import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.Certificate;
import java.security.spec.AlgorithmParameterSpec;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SignatureUtils {

  private SignatureUtils() {
  }


  /**
   * Create a signature object for signing input with algorithm and private key.
   *
   * @param provider   cryptographic service provider
   * @param algorithm  signing algorithm
   * @param privateKey signing key
   * @return signed input
   * @throws GeneralSecurityException wrapper exception
   */
  public static byte[] createSignature(String provider, String algorithm, PrivateKey privateKey)
      throws GeneralSecurityException {
    Signature signature = Signature.getInstance(algorithm, provider);
    signature.initSign(privateKey);
    return signature.sign();
  }


  /**
   * Signing input with algorithm and private key.
   *
   * @param provider   cryptographic service provider
   * @param algorithm  signing algorithm
   * @param privateKey signing key
   * @param input      input
   * @return signed input
   * @throws GeneralSecurityException wrapper exception
   */
  public static byte[] sign(String provider, String algorithm, PrivateKey privateKey, byte[] input)
      throws GeneralSecurityException {
    Signature signature = Signature.getInstance(algorithm, provider);
    signature.initSign(privateKey);
    signature.update(input);
    return signature.sign();
  }

  /**
   * Signing input with algorithm and private key.
   *
   * @param provider               cryptographic service provider
   * @param algorithm              signing algorithm
   * @param privateKey             signing key
   * @param algorithmParameterSpec algorithm param specs
   * @param input                  input
   * @return signed input
   * @throws GeneralSecurityException wrapper exception
   */
  public static byte[] sign(String provider, String algorithm, PrivateKey privateKey,
      AlgorithmParameterSpec algorithmParameterSpec,
      byte[] input) throws GeneralSecurityException {
    Signature signature = Signature.getInstance(algorithm, provider);
    signature.setParameter(algorithmParameterSpec);
    signature.initSign(privateKey);
    signature.update(input);
    return signature.sign();
  }

  /**
   * Verify signed input by algorithm using public key, input
   *
   * @param provider    cryptographic service provider
   * @param algorithm   signing algorithm
   * @param publicKey   corresponding public key of a private key used for signing
   * @param input       unsigned input
   * @param signedInput signed input
   * @return verification result (true|false)
   * @throws GeneralSecurityException wrapper exception
   */
  public static boolean verifySign(String provider, String algorithm, PublicKey publicKey,
      byte[] input,
      byte[] signedInput)
      throws GeneralSecurityException {
    Signature signature = Signature.getInstance(algorithm, provider);
    signature.initVerify(publicKey);
    signature.update(input);
    return signature.verify(signedInput);
  }

  /**
   * Verify signed input by algorithm using certificate, input
   *
   * @param provider    cryptographic service provider
   * @param algorithm   signing algorithm
   * @param certificate certificate form private key used to for signing
   * @param input       unsigned input
   * @param signedInput signed input
   * @return verification result (true|false)
   * @throws GeneralSecurityException wrapper exception
   */
  public static boolean verifySign(String provider, String algorithm, Certificate certificate,
      byte[] input,
      byte[] signedInput)
      throws GeneralSecurityException {
    Signature signature = Signature.getInstance(algorithm, provider);
    signature.initVerify(certificate);
    signature.update(input);
    return signature.verify(signedInput);
  }

  /**
   * Verify signed input by algorithm using public key, input
   *
   * @param provider               cryptographic service provider
   * @param algorithm              signing algorithm
   * @param publicKey              corresponding public key of a private key used for signing
   * @param algorithmParameterSpec algorithm param specs
   * @param input                  unsigned input
   * @param signedInput            signed input
   * @return verification result (true|false)
   * @throws GeneralSecurityException wrapper exception
   */
  public static boolean verifySign(String provider, String algorithm, PublicKey publicKey,
      AlgorithmParameterSpec algorithmParameterSpec,
      byte[] input, byte[] signedInput) throws GeneralSecurityException {
    Signature signature = Signature.getInstance(algorithm, provider);
    signature.setParameter(algorithmParameterSpec);
    signature.initVerify(publicKey);
    signature.update(input);
    return signature.verify(signedInput);
  }


}
