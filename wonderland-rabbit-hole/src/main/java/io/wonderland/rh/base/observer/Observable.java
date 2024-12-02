package io.wonderland.rh.base.observer;

@SuppressWarnings("unused")
public interface Observable<I, N> extends Identifiable<I> {

  void notifyObservers();

  void notifyObserver(I id, N notification);

  void notifyObservers(N notification);

  void addObserver(Observer<I, N> observer);

  void removeObserver(Observer<I, N> observer);

  boolean getChanged();

  void setChanged(boolean flag);

}
