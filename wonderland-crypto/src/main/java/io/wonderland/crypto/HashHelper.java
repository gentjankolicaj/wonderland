package io.wonderland.crypto;

import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Getter
@Slf4j
public class HashHelper {

  private final String provider;
  private final String algorithm;
  private final MessageDigest md;

  public HashHelper(String algorithm) throws GeneralSecurityException {
    this(CSP.INSTANCE_CONTEXT.getProvider(), algorithm);
  }

  public HashHelper(String provider, String algorithm) throws GeneralSecurityException {
    if (StringUtils.isEmpty(algorithm)) {
      throw new IllegalArgumentException(ExceptionMessages.ALGORITHM_CAN_T_BE_EMPTY);
    }
    this.algorithm = algorithm;

    if (StringUtils.isNotEmpty(provider)) {
      this.provider = provider;
    } else {
      this.provider = CSP.INSTANCE_CONTEXT.getProvider();
    }
    this.md = HashUtils.createDigest(provider, algorithm);

  }

  public void update(byte[] input) {
    md.update(input);
  }

  public byte[] digest(byte[] input) {
    return md.digest(input);
  }

  public byte[] digest() {
    return md.digest();
  }

}
