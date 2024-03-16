package io.wonderland.rh.base;


import java.util.ArrayList;
import java.util.List;

public abstract class AbstractObservable<N> implements Observable<Integer,N> {
  private int id;
  private boolean changed;
  private final List<Observer<Integer,N>> observers=new ArrayList<>();

  @Override
  public Integer getId() {
    return id;
  }

  @Override
  public void setId(Integer id) {
   this.id= id;
  }

  @Override
  public void notifyObservers() {
    notifyObservers(null);
  }

  @Override
  public void notifyObservers(N notification) {
   for(Observer obs:observers){
       obs.update(notification);
     }
  }


  @Override
  public void notifyObserver(Integer id,N notification) {
    for(Observer obs:observers){
      if(obs.getId().equals(id)){
        obs.update(notification);
        break;
      }
    }
  }

  @Override
  public void addObserver(Observer<Integer,N> observer) {
   this.observers.add(observer);
  }

  @Override
  public void removeObserver(Observer<Integer,N> observer) {
   this.observers.remove(observer);
  }

  @Override
  public void setChanged(boolean changed) {
   this.changed=changed;
  }

  @Override
  public boolean getChanged() {
    return this.changed;
  }


}
