package io.wonderland.rh.base.mediator;


import java.util.Map;


public interface Mediator<K> {

  Map<K, Component<K>> getComponents();

}
