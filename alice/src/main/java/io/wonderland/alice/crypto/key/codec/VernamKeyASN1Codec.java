package io.wonderland.alice.crypto.key.codec;


import com.beanit.asn1bean.ber.ReverseByteArrayOutputStream;
import com.beanit.asn1bean.ber.types.BerInteger;
import com.beanit.asn1bean.ber.types.BerOctetString;
import io.wonderland.alice.asn1.vernam.VernamKeyASN1;
import io.wonderland.alice.codec.SecretKeyCodec;
import io.wonderland.alice.crypto.key.secretkey.VernamKey;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.function.Function;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;


@Slf4j
public final class VernamKeyASN1Codec implements SecretKeyCodec<VernamKey> {

  private static VernamKeyASN1Codec instance;

  private VernamKeyASN1Codec() {
  }

  public static VernamKeyASN1Codec getInstance() {
    if (instance == null) {
      instance = new VernamKeyASN1Codec();
    }
    return instance;
  }

  @Override
  public Function<VernamKey, byte[]> encoder() {
    return key -> {
      ReverseByteArrayOutputStream ber = new ReverseByteArrayOutputStream(100, true);
      VernamKeyASN1 keyASN1 = new VernamKeyASN1();
      keyASN1.setM(new BerInteger(key.getModulus()));
      keyASN1.setK(new BerOctetString(key.getKey()));
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
  public Function<byte[], VernamKey> decoder() {
    return array -> {
      ByteArrayInputStream bais = new ByteArrayInputStream(array);
      VernamKeyASN1 keyASN1 = new VernamKeyASN1();
      try {
        keyASN1.decode(bais, true);
        return new VernamKey(keyASN1.getM().intValue(), keyASN1.getK().value);
      } catch (IOException e) {
        log.error("", e);
        return null;
      }
    };
  }

}
