package io.wonderland.rh.base;

public abstract class AbstractObserver<N> implements Observer<Integer,N> {
  private int id;

  @Override
  public Integer getId() {
    return id;
  }

  @Override
  public void setId(Integer integer) {
  this.id=id;
  }


}
