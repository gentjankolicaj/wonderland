package io.wonderland.rh.keygen;


import io.wonderland.rh.base.AbstractObservable;
import io.wonderland.rh.base.Observer;

public class KeygenObservable extends AbstractObservable<Object> {

  public void addAllObservers(Observer<Integer,Object>...observers){
    for(Observer<Integer,Object> observer:observers){
      addObserver(observer);
    }
  }

}
