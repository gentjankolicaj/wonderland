package io.wonderland.alice.codec;

import java.security.PrivateKey;
import java.security.PublicKey;

public interface KeyPairCodec<PK extends PublicKey, SK extends PrivateKey> {

  PublicKeyCodec<PK> publicKeyCodec();

  PrivateKeyCodec<SK> privateKeyCodec();

}
