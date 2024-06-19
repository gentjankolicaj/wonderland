package io.wonderland.alice.crypto.key.codec;

import io.wonderland.alice.codec.KeyPairCodec;
import io.wonderland.alice.codec.PrivateKeyCodec;
import io.wonderland.alice.codec.PublicKeyCodec;
import io.wonderland.alice.crypto.key.keypair.AliceRSAPrivateKey;
import io.wonderland.alice.crypto.key.keypair.AliceRSAPublicKey;
import java.util.function.Function;


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


  private static class PublicKeyCodecImpl implements PublicKeyCodec<AliceRSAPublicKey> {

    @Override
    public Function<AliceRSAPublicKey, byte[]> encoder() {
      return null;
    }

    @Override
    public Function<byte[], AliceRSAPublicKey> decoder() {
      return null;
    }
  }


  private static class PrivateKeyCodecImpl implements PrivateKeyCodec<AliceRSAPrivateKey> {


    @Override
    public Function<AliceRSAPrivateKey, byte[]> encoder() {
      return null;
    }

    @Override
    public Function<byte[], AliceRSAPrivateKey> decoder() {
      return null;
    }
  }

}
