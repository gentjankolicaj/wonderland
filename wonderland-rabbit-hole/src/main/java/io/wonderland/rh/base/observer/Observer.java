package io.wonderland.rh.base.observer;

public interface Observer<I, N> extends Identifiable<I> {

  void update(N notification);

}
