package io.wonderland.rh.base;

public interface Observable<ID,NOTIFICATION> extends Identifiable<ID> {

  void notifyObservers();

  void notifyObserver(ID id,NOTIFICATION notification);
  void notifyObservers(NOTIFICATION notification);

  void addObserver(Observer<ID,NOTIFICATION> observer);
  void removeObserver(Observer<ID,NOTIFICATION> observer);

  void setChanged(boolean flag);
  boolean getChanged();

}
