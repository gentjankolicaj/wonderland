package io.wonderland.alice.crypto.key.codec;

import com.beanit.asn1bean.ber.ReverseByteArrayOutputStream;
import com.beanit.asn1bean.ber.types.BerInteger;
import io.wonderland.alice.asn1.rsa.RSAPrivateKeyASN1;
import io.wonderland.alice.asn1.rsa.RSAPublicKeyASN1;
import io.wonderland.alice.codec.KeyPairCodec;
import io.wonderland.alice.codec.PrivateKeyCodec;
import io.wonderland.alice.codec.PublicKeyCodec;
import io.wonderland.alice.crypto.key.keypair.AliceRSAPrivateKey;
import io.wonderland.alice.crypto.key.keypair.AliceRSAPublicKey;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.function.Function;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;


public final class RSAKeyPairASN1Codec implements
    KeyPairCodec<AliceRSAPublicKey, AliceRSAPrivateKey> {

  private static RSAKeyPairASN1Codec instance;

  private RSAKeyPairASN1Codec() {
  }

  public static RSAKeyPairASN1Codec getInstance() {
    if (instance == null) {
      instance = new RSAKeyPairASN1Codec();
    }
    return instance;
  }

  @Override
  public PublicKeyCodec<AliceRSAPublicKey> publicKeyCodec() {
    return new PublicKeyCodecImpl();
  }

  @Override
  public PrivateKeyCodec<AliceRSAPrivateKey> privateKeyCodec() {
    return new PrivateKeyCodecImpl();
  }


  @Slf4j
  private static class PublicKeyCodecImpl implements PublicKeyCodec<AliceRSAPublicKey> {

    @Override
    public Function<AliceRSAPublicKey, byte[]> encoder() {
      return key -> {
        ReverseByteArrayOutputStream os = new ReverseByteArrayOutputStream(100, true);
        RSAPublicKeyASN1 keyASN1 = new RSAPublicKeyASN1();
        keyASN1.setPublicExponent(new BerInteger(key.getPublicExponent()));
        keyASN1.setModulus(new BerInteger(key.getModulus()));
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
    public Function<byte[], AliceRSAPublicKey> decoder() {
      return encoded -> {
        ByteArrayInputStream bais = new ByteArrayInputStream(encoded);
        RSAPublicKeyASN1 keyASN1 = new RSAPublicKeyASN1();
        try {
          keyASN1.decode(bais, true);
          return new AliceRSAPublicKey(keyASN1.getPublicExponent().value,
              keyASN1.getModulus().value);
        } catch (IOException e) {
          log.error("", e);
          return null;
        }
      };
    }
  }

  @Slf4j
  private static class PrivateKeyCodecImpl implements PrivateKeyCodec<AliceRSAPrivateKey> {


    @Override
    public Function<AliceRSAPrivateKey, byte[]> encoder() {
      return key -> {
        ReverseByteArrayOutputStream os = new ReverseByteArrayOutputStream(100, true);
        RSAPrivateKeyASN1 keyASN1 = new RSAPrivateKeyASN1();
        keyASN1.setModulus(new BerInteger(key.getModulus()));
        keyASN1.setPublicExponent(new BerInteger(key.getPublicExponent()));
        keyASN1.setPrivateExponent(new BerInteger(key.getPrivateExponent()));
        keyASN1.setPrimeP(new BerInteger(key.getPrimeP()));
        keyASN1.setPrimeQ(new BerInteger(key.getPrimeQ()));
        keyASN1.setPrimeExponentP(new BerInteger(key.getPrimeExponentP()));
        keyASN1.setPrimeExponentQ(new BerInteger(key.getPrimeExponentQ()));
        keyASN1.setCoefficient(new BerInteger(key.getCrtCoefficient()));
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
    public Function<byte[], AliceRSAPrivateKey> decoder() {
      return encoded -> {
        ByteArrayInputStream bais = new ByteArrayInputStream(encoded);
        RSAPrivateKeyASN1 keyASN1 = new RSAPrivateKeyASN1();
        try {
          keyASN1.decode(bais, true);
          return new AliceRSAPrivateKey(keyASN1.getModulus().value, keyASN1.getPrimeP().value,
              keyASN1.getPrimeQ().value,
              keyASN1.getPublicExponent().value, keyASN1.getPrivateExponent().value,
              keyASN1.getPrimeExponentP().value
              , keyASN1.getPrimeExponentQ().value, keyASN1.getCoefficient().value);
        } catch (IOException e) {
          log.error("", e);
          return null;
        }
      };
    }
  }

}
