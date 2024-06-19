package io.wonderland.base;

public interface Application<T> {


  void start(String[] args) throws ApplicationException;

  void stop() throws ApplicationException;

  void shutdownHooks();

  T getConfiguration();

}
