package io.wonderland.base;

public abstract class AbstractService<T, I> {

  protected Dao<T, I> dao;

  protected AbstractService(Dao<T, I> dao) {
    this.dao = dao;
  }
}
