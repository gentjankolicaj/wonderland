package io.wonderland.alice.crypto.key.codec;


import com.beanit.asn1bean.ber.ReverseByteArrayOutputStream;
import io.wonderland.alice.asn1.caesar.CaesarKeyASN1;
import io.wonderland.alice.codec.SecretKeyCodec;
import io.wonderland.alice.crypto.key.secretkey.CaesarKey;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.function.Function;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;


@Slf4j
public final class CaesarKeyASN1Codec implements SecretKeyCodec<CaesarKey> {

  private static CaesarKeyASN1Codec instance;

  private CaesarKeyASN1Codec() {
  }

  public static CaesarKeyASN1Codec getInstance() {
    if (instance == null) {
      instance = new CaesarKeyASN1Codec();
    }
    return instance;
  }


  @Override
  public Function<CaesarKey, byte[]> encoder() {
    return key -> {
      ReverseByteArrayOutputStream ber = new ReverseByteArrayOutputStream(100, true);
      CaesarKeyASN1 keyASN1 = new CaesarKeyASN1(key.getShift());
      try {
        keyASN1.encode(ber, true);
        return ber.getArray();
      } catch (IOException e) {
        log.error("", e);
        return ArrayUtils.EMPTY_BYTE_ARRAY;
      }
    };
  }

  @Override
  public Function<byte[], CaesarKey> decoder() {
    return array -> {
      ByteArrayInputStream bais = new ByteArrayInputStream(array);
      CaesarKeyASN1 keyASN1 = new CaesarKeyASN1();
      try {
        keyASN1.decode(bais, true);
        return new CaesarKey(keyASN1.intValue());
      } catch (IOException e) {
        log.error("", e);
        return null;
      }
    };
  }

}
