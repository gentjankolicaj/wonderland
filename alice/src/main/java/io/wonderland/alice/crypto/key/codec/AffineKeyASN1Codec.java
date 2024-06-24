package io.wonderland.alice.crypto.key.codec;


import com.beanit.asn1bean.ber.ReverseByteArrayOutputStream;
import com.beanit.asn1bean.ber.types.BerOctetString;
import io.wonderland.alice.asn1.affine.AffineKeyASN1;
import io.wonderland.alice.codec.SecretKeyCodec;
import io.wonderland.alice.crypto.key.secretkey.AffineKey;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.function.Function;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;


@Slf4j
public final class AffineKeyASN1Codec implements SecretKeyCodec<AffineKey> {

  private static AffineKeyASN1Codec instance;

  private AffineKeyASN1Codec() {
  }

  public static AffineKeyASN1Codec getInstance() {
    if (instance == null) {
      instance = new AffineKeyASN1Codec();
    }
    return instance;
  }


  @Override
  public Function<AffineKey, byte[]> encoder() {
    return affineKey -> {
      ReverseByteArrayOutputStream os = new ReverseByteArrayOutputStream(100, true);
      AffineKeyASN1 affineKeyASN1 = new AffineKeyASN1();
      affineKeyASN1.setA(new BerOctetString(affineKey.getA().toByteArray()));
      affineKeyASN1.setB(new BerOctetString(affineKey.getB().toByteArray()));
      affineKeyASN1.setM(new BerOctetString(affineKey.getM().toByteArray()));
      try {
        affineKeyASN1.encode(os, true);
        return os.getArray();
      } catch (IOException e) {
        log.error("", e);
        return ArrayUtils.EMPTY_BYTE_ARRAY;
      }
    };
  }

  @Override
  public Function<byte[], AffineKey> decoder() {
    return encoded -> {
      ByteArrayInputStream bais = new ByteArrayInputStream(encoded);
      AffineKeyASN1 affineKeyASN1 = new AffineKeyASN1();
      try {
        affineKeyASN1.decode(bais, true);
        return new AffineKey(new BigInteger(affineKeyASN1.getA().value),
            new BigInteger(affineKeyASN1.getB().value), new BigInteger(affineKeyASN1.getM().value));
      } catch (IOException e) {
        log.error("", e);
        return null;
      }
    };
  }

}
