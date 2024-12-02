package io.wonderland.alice.crypto.key.codec;


import com.beanit.asn1bean.ber.ReverseByteArrayOutputStream;
import io.wonderland.alice.asn1.railfence.RailfenceKeyASN1;
import io.wonderland.alice.codec.SecretKeyCodec;
import io.wonderland.alice.crypto.key.secretkey.RailfenceKey;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.function.Function;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;


@Slf4j
public final class RailfenceKeyASN1Codec implements SecretKeyCodec<RailfenceKey> {

  private static RailfenceKeyASN1Codec instance;

  private RailfenceKeyASN1Codec() {
  }

  public static RailfenceKeyASN1Codec getInstance() {
    if (instance == null) {
      instance = new RailfenceKeyASN1Codec();
    }
    return instance;
  }


  @Override
  public Function<RailfenceKey, byte[]> encoder() {
    return key -> {
      ReverseByteArrayOutputStream os = new ReverseByteArrayOutputStream(100, true);
      RailfenceKeyASN1 keyASN1 = new RailfenceKeyASN1(key.getRails());
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
  public Function<byte[], RailfenceKey> decoder() {
    return encoded -> {
      ByteArrayInputStream bais = new ByteArrayInputStream(encoded);
      RailfenceKeyASN1 keyASN1 = new RailfenceKeyASN1();
      try {
        keyASN1.decode(bais, true);
        return new RailfenceKey(keyASN1.intValue());
      } catch (IOException e) {
        log.error("", e);
        return null;
      }
    };
  }

}
