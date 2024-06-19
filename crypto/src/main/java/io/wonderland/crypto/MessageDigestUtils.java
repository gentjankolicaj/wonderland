package io.wonderland.crypto;

import static io.wonderland.crypto.ExceptionMessages.INPUT_NOT_VALID;
import static io.wonderland.crypto.ExceptionMessages.KEY_NOT_VALID;

import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.MessageDigest;
import java.util.Objects;
import javax.crypto.Mac;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.operator.DefaultDigestAlgorithmIdentifierFinder;
import org.bouncycastle.operator.DigestAlgorithmIdentifierFinder;
import org.bouncycastle.operator.DigestCalculator;
import org.bouncycastle.operator.DigestCalculatorProvider;
import org.bouncycastle.operator.OperatorException;
import org.bouncycastle.operator.jcajce.JcaDigestCalculatorProviderBuilder;

@Slf4j
public class MessageDigestUtils {


  private MessageDigestUtils() {
  }

  public static MessageDigest createDigest(String provider, String algorithm)
      throws GeneralSecurityException {
    if (StringUtils.isEmpty(algorithm)) {
      throw new IllegalArgumentException(ExceptionMessages.ALGORITHM_CAN_T_BE_EMPTY);
    }
    return MessageDigest.getInstance(algorithm, provider);
  }

  public static byte[] computeDigest(String provider, String algorithm, byte[] input)
      throws GeneralSecurityException {
    if (StringUtils.isEmpty(algorithm)) {
      throw new IllegalArgumentException(ExceptionMessages.ALGORITHM_CAN_T_BE_EMPTY);
    }

    if (ArrayUtils.isEmpty(input)) {
      throw new IllegalArgumentException(INPUT_NOT_VALID);
    }
    MessageDigest messageDigest = MessageDigest.getInstance(algorithm, provider);
    return messageDigest.digest(input);
  }

  public static DigestCalculator createDigestCalculator(String provider, String algorithm)
      throws OperatorException {
    if (StringUtils.isEmpty(algorithm)) {
      throw new IllegalArgumentException(ExceptionMessages.ALGORITHM_CAN_T_BE_EMPTY);
    }
    DigestAlgorithmIdentifierFinder algorithmIdentifierFinder = new DefaultDigestAlgorithmIdentifierFinder();
    DigestCalculatorProvider digestCalculatorProvider = new JcaDigestCalculatorProviderBuilder().setProvider(
        provider).build();
    return digestCalculatorProvider.get(algorithmIdentifierFinder.find(algorithm));

  }

  /**
   * Return a MAC computed over input using the passed in MAC algorithm name.
   *
   * @param algorithm the name of the MAC algorithm.
   * @param key       an appropriate secret key for the MAC algorithm.
   * @param input     the input for the MAC function.
   * @return the computed MAC.
   */
  public static byte[] computeMac(String provider, String algorithm, Key key, byte[] input)
      throws GeneralSecurityException {
    if (StringUtils.isEmpty(algorithm)) {
      throw new IllegalArgumentException(ExceptionMessages.ALGORITHM_CAN_T_BE_EMPTY);
    }
    if (Objects.isNull(key)) {
      throw new IllegalArgumentException(KEY_NOT_VALID);
    }

    if (ArrayUtils.isEmpty(input)) {
      throw new IllegalArgumentException(INPUT_NOT_VALID);
    }
    Mac mac = Mac.getInstance(algorithm, provider);
    mac.init(key);
    mac.update(input);
    return mac.doFinal();
  }


}
