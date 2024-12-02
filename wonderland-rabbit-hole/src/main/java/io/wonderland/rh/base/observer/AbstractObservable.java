package io.wonderland.rh.base.observer;


import java.util.ArrayList;
import java.util.List;

public abstract class AbstractObservable<N> implements Observable<Integer, N> {

  private final List<Observer<Integer, N>> observers = new ArrayList<>();
  private int id;
  private boolean changed;

  @Override
  public Integer getId() {
    return id;
  }

  @Override
  public void setId(Integer id) {
    this.id = id;
  }

  @Override
  public void notifyObservers() {
    notifyObservers(null);
  }

  @Override
  public void notifyObservers(N notification) {
    for (Observer obs : observers) {
      obs.update(notification);
    }
  }

  @Override
  public void notifyObserver(Integer id, N notification) {
    for (Observer obs : observers) {
      if (obs.getId().equals(id)) {
        obs.update(notification);
        break;
      }
    }
  }

  @Override
  public void addObserver(Observer<Integer, N> observer) {
    this.observers.add(observer);
  }

  @Override
  public void removeObserver(Observer<Integer, N> observer) {
    this.observers.remove(observer);
  }

  @Override
  public boolean getChanged() {
    return this.changed;
  }

  @Override
  public void setChanged(boolean changed) {
    this.changed = changed;
  }


}
