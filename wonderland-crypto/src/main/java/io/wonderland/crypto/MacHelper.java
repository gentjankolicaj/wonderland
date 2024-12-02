package io.wonderland.crypto;

import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Objects;
import javax.crypto.Mac;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public final class MacHelper {

  private final String provider;
  private final String algorithm;
  private final Mac mac;

  public MacHelper(String algorithm) throws GeneralSecurityException {
    this(CSP.INSTANCE_CONTEXT.getProvider(), algorithm);
  }

  public MacHelper(String provider, String algorithm) throws GeneralSecurityException {
    this.provider = provider;
    this.algorithm = algorithm;
    this.mac = Mac.getInstance(algorithm, provider);
  }


  /**
   * Init MAC of type algorithm and of provider.
   *
   * @param key key for the MAC algorithm.
   */
  public void init(Key key) throws GeneralSecurityException {
    if (Objects.isNull(key)) {
      throw new IllegalArgumentException(ExceptionMessages.KEY_NOT_VALID);
    }
    mac.init(key);
  }

  /**
   * Init MAC of type algorithm and of provider.
   *
   * @param key key for the MAC algorithm.
   */
  public void init(Key key, AlgorithmParameterSpec paramSpec) throws GeneralSecurityException {
    if (Objects.isNull(key)) {
      throw new IllegalArgumentException(ExceptionMessages.KEY_NOT_VALID);
    }

    mac.init(key, paramSpec);
  }

  /**
   * Update message to be mac-ed
   *
   * @param input message
   */
  public void update(byte[] input) {
    mac.update(input);
  }

  /**
   * Compute MAC , existing buffered message + input
   *
   * @param input message
   * @return computed mac
   */
  public byte[] mac(byte[] input) {
    return mac.doFinal(input);
  }

  /**
   * Compute mac from message buffer.
   *
   * @return computed mac
   */
  public byte[] mac() {
    return mac.doFinal();
  }


  /**
   * Return a new MAC object instantiated.
   *
   * @param key an appropriate key for the MAC algorithm.
   * @return MAC instance.
   */
  public Mac createMac(Key key) throws GeneralSecurityException {
    if (Objects.isNull(key)) {
      throw new IllegalArgumentException(ExceptionMessages.KEY_NOT_VALID);
    }

    Mac mac = Mac.getInstance(algorithm, provider);
    mac.init(key);
    return mac;
  }

}