package io.wonderland.rh.base.mediator;


public interface Component<K> {

  K getKey();

  <T> T getValue();

  void clear();

  void setMediator(Mediator<K> mediator);

}
