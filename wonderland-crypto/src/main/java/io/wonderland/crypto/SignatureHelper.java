package io.wonderland.crypto;

import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.Certificate;
import java.security.spec.AlgorithmParameterSpec;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public final class SignatureHelper {

  private final String provider;
  private final String algorithm;
  private final Signature signature;

  public SignatureHelper(String algorithm)
      throws NoSuchAlgorithmException, NoSuchProviderException {
    this(CSP.INSTANCE_CONTEXT.getProvider(), algorithm);
  }

  public SignatureHelper(String provider, String algorithm)
      throws NoSuchAlgorithmException, NoSuchProviderException {
    this.provider = provider;
    this.algorithm = algorithm;
    this.signature = Signature.getInstance(algorithm, provider);
  }


  /**
   * Create a signature object for signing input with algorithm and private key.
   *
   * @param privateKey signing key
   * @return signed input
   * @throws GeneralSecurityException wrapper exception
   */
  public byte[] createSignature(PrivateKey privateKey)
      throws GeneralSecurityException {
    Signature signature = Signature.getInstance(algorithm, provider);
    signature.initSign(privateKey);
    return signature.sign();
  }


  /**
   * Signing input with algorithm and private key.
   *
   * @param privateKey signing key
   * @param input      input
   * @return signed input
   * @throws GeneralSecurityException wrapper exception
   */
  public byte[] sign(PrivateKey privateKey, byte[] input)
      throws GeneralSecurityException {
    signature.initSign(privateKey);
    signature.update(input);
    return signature.sign();
  }

  /**
   * Signing input with algorithm and private key.
   *
   * @param privateKey             signing key
   * @param algorithmParameterSpec algorithm param specs
   * @param input                  input
   * @return signed input
   * @throws GeneralSecurityException wrapper exception
   */
  public byte[] sign(PrivateKey privateKey, AlgorithmParameterSpec algorithmParameterSpec,
      byte[] input) throws GeneralSecurityException {
    signature.setParameter(algorithmParameterSpec);
    signature.initSign(privateKey);
    signature.update(input);
    return signature.sign();
  }

  /**
   * Verify signed input by algorithm using public key, input
   *
   * @param publicKey   corresponding public key of a private key used for signing
   * @param input       unsigned input
   * @param signedInput signed input
   * @return verification result (true|false)
   * @throws GeneralSecurityException wrapper exception
   */
  public boolean verifySign(PublicKey publicKey, byte[] input, byte[] signedInput)
      throws GeneralSecurityException {
    signature.initVerify(publicKey);
    signature.update(input);
    return signature.verify(signedInput);
  }

  /**
   * Verify signed input by algorithm using certificate, input
   *
   * @param certificate certificate form private key used to for signing
   * @param input       unsigned input
   * @param signedInput signed input
   * @return verification result (true|false)
   * @throws GeneralSecurityException wrapper exception
   */
  public boolean verifySign(Certificate certificate, byte[] input, byte[] signedInput)
      throws GeneralSecurityException {
    signature.initVerify(certificate);
    signature.update(input);
    return signature.verify(signedInput);
  }

  /**
   * Verify signed input by algorithm using public key, input
   *
   * @param publicKey              corresponding public key of a private key used for signing
   * @param algorithmParameterSpec algorithm param specs
   * @param input                  unsigned input
   * @param signedInput            signed input
   * @return verification result (true|false)
   * @throws GeneralSecurityException wrapper exception
   */
  public boolean verifySign(PublicKey publicKey,
      AlgorithmParameterSpec algorithmParameterSpec, byte[] input, byte[] signedInput)
      throws GeneralSecurityException {
    signature.setParameter(algorithmParameterSpec);
    signature.initVerify(publicKey);
    signature.update(input);
    return signature.verify(signedInput);
  }


}
