package io.wonderland.base;

import java.util.concurrent.Callable;

public interface FunctionArg<T, R> {

  Callable<R> arg(T t);

}
