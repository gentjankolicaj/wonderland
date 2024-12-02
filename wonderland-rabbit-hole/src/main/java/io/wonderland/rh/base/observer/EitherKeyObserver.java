package io.wonderland.rh.base.observer;


import io.atlassian.fugue.Either;
import java.security.KeyPair;
import javax.crypto.SecretKey;


public class EitherKeyObserver extends KeyObserver<Either<SecretKey, KeyPair>> {


}
