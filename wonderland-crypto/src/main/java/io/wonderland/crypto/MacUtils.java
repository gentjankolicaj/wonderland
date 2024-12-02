package io.wonderland.crypto;

import java.security.GeneralSecurityException;
import java.security.Key;
import java.util.Objects;
import javax.crypto.Mac;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

public final class MacUtils {

  private MacUtils() {
  }


  /**
   * Return a MAC computed over input using the passed in MAC algorithm name.
   *
   * @param provider  cryptographic service provider
   * @param algorithm the name of the MAC algorithm.
   * @param key       an appropriate secret key for the MAC algorithm.
   * @param input     the input for the MAC function.
   * @return the computed MAC.
   */
  public static byte[] mac(String provider, String algorithm, Key key, byte[] input)
      throws GeneralSecurityException {
    if (StringUtils.isEmpty(algorithm)) {
      throw new IllegalArgumentException(ExceptionMessages.ALGORITHM_CAN_T_BE_EMPTY);
    }
    if (Objects.isNull(key)) {
      throw new IllegalArgumentException(ExceptionMessages.KEY_NOT_VALID);
    }

    if (ArrayUtils.isEmpty(input)) {
      throw new IllegalArgumentException(ExceptionMessages.INPUT_NOT_VALID);
    }
    Mac mac = Mac.getInstance(algorithm, provider);
    mac.init(key);
    mac.update(input);
    return mac.doFinal();
  }


  /**
   * Return a new MAC object instantiated.
   *
   * @param provider  cryptographic service provider
   * @param algorithm the name of the MAC algorithm.
   * @param key       an appropriate secret key for the MAC algorithm.
   * @return MAC instance.
   */
  public static Mac createMac(String provider, String algorithm, Key key)
      throws GeneralSecurityException {
    if (StringUtils.isEmpty(algorithm)) {
      throw new IllegalArgumentException(ExceptionMessages.ALGORITHM_CAN_T_BE_EMPTY);
    }
    if (Objects.isNull(key)) {
      throw new IllegalArgumentException(ExceptionMessages.KEY_NOT_VALID);
    }

    Mac mac = Mac.getInstance(algorithm, provider);
    mac.init(key);
    return mac;
  }


}