package io.wonderland.alice.crypto.key.codec;


import com.beanit.asn1bean.ber.ReverseByteArrayOutputStream;
import com.beanit.asn1bean.ber.types.BerInteger;
import io.wonderland.alice.asn1.otp.OTPKeyASN1;
import io.wonderland.alice.asn1.otp.OTPKeyASN1.K;
import io.wonderland.alice.codec.SecretKeyCodec;
import io.wonderland.alice.crypto.key.secretkey.OTPKey;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;


@Slf4j
public final class OTPKeyASN1Codec implements SecretKeyCodec<OTPKey> {

  private static OTPKeyASN1Codec instance;

  private OTPKeyASN1Codec() {
  }

  public static OTPKeyASN1Codec getInstance() {
    if (instance == null) {
      instance = new OTPKeyASN1Codec();
    }
    return instance;
  }

  private static List<Integer> getInts(List<BerInteger> berIntegerList) {
    return berIntegerList.stream().map(BerInteger::intValue).collect(Collectors.toList());
  }

  private static K getK(List<Integer> integerList) {
    K k = new K();
    k.getBerInteger()
        .addAll(integerList.stream().map(BerInteger::new).collect(Collectors.toList()));
    return k;
  }

  @Override
  public Function<OTPKey, byte[]> encoder() {
    return key -> {
      ReverseByteArrayOutputStream os = new ReverseByteArrayOutputStream(100, true);
      OTPKeyASN1 keyASN1 = new OTPKeyASN1();
      keyASN1.setM(new BerInteger(key.getModulus()));
      keyASN1.setK(getK(key.getCodeKeys()));
      try {
        keyASN1.encode(os, true);
        return os.getArray();
      } catch (IOException e) {
        log.error("", e);
        return ArrayUtils.EMPTY_BYTE_ARRAY;
      }
    };
  }

  @Override
  public Function<byte[], OTPKey> decoder() {
    return encoded -> {
      ByteArrayInputStream bais = new ByteArrayInputStream(encoded);
      OTPKeyASN1 keyASN1 = new OTPKeyASN1();
      try {
        keyASN1.decode(bais, true);
        return new OTPKey(keyASN1.getM().intValue(), getInts(keyASN1.getK().getBerInteger()));
      } catch (IOException e) {
        log.error("", e);
        return null;
      }
    };
  }

}
