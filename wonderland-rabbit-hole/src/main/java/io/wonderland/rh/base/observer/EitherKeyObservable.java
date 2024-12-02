package io.wonderland.rh.base.observer;


import io.atlassian.fugue.Either;
import java.security.KeyPair;
import javax.crypto.SecretKey;

public class EitherKeyObservable extends AbstractObservable<Either<SecretKey, KeyPair>> {

  public void addAllObservers(EitherKeyObserver... observers) {
    for (EitherKeyObserver observer : observers) {
      addObserver(observer);
    }
  }

}
