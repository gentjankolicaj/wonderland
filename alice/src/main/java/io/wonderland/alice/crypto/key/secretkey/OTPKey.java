package io.wonderland.alice.crypto.key.secretkey;

import io.wonderland.alice.charset.CharsetsUtils;
import io.wonderland.alice.crypto.Algorithms;
import io.wonderland.alice.crypto.key.codec.OTPKeyASN1Codec;
import io.wonderland.alice.exception.ExceptionMessages;
import io.wonderland.base.IntUtils;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.crypto.SecretKey;
import lombok.Getter;
import lombok.ToString;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;


@Getter
@ToString
public final class OTPKey implements SecretKey {

  private static final Function<OTPKey, byte[]> ENCODER = OTPKeyASN1Codec.getInstance().encoder();

  private final int modulus;
  private final List<Integer> codeKeys;

  public OTPKey(int modulus, int... codeKeys) {
    if (modulus <= 0) {
      throw new IllegalArgumentException(ExceptionMessages.KEY_MODULUS_NOT_VALID);
    }
    if (ArrayUtils.isEmpty(codeKeys)) {
      throw new IllegalArgumentException(ExceptionMessages.KEY_ARGS_NOT_VALID);
    }
    this.codeKeys = Arrays.stream(codeKeys).boxed().collect(Collectors.toList());
    this.modulus = modulus;
  }

  public OTPKey(Integer modulus, List<Integer> codeKeys) {
    if (CollectionUtils.isEmpty(codeKeys)) {
      throw new IllegalArgumentException(ExceptionMessages.KEY_ARGS_NOT_VALID);
    }
    this.codeKeys = codeKeys;
    this.modulus = modulus;
  }

  public OTPKey(String modulus, String... codeKeys) {
    if (StringUtils.isEmpty(modulus) || ArrayUtils.isEmpty(codeKeys)) {
      throw new IllegalArgumentException(ExceptionMessages.KEY_ARGS_NOT_VALID);
    }
    this.modulus = Integer.parseInt(modulus);
    this.codeKeys = IntUtils.parseIntList(codeKeys);
  }

  public OTPKey(byte[] arg) {
    if (ArrayUtils.isEmpty(arg)) {
      throw new IllegalArgumentException(ExceptionMessages.KEY_NOT_VALID);
    }
    this.modulus = CharsetsUtils.getDefaultAlphabetSize();
    this.codeKeys = IntUtils.getIntListBE(arg);
  }

  @Override
  public String getAlgorithm() {
    return Algorithms.OTP.getName();
  }

  @Override
  public String getFormat() {
    return Algorithms.OTP.getKeyFormat().getValue();
  }

  @Override
  public byte[] getEncoded() {
    return ENCODER.apply(this);
  }
}
