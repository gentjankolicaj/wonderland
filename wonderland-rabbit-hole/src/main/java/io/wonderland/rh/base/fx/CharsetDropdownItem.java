package io.wonderland.rh.base.fx;

import java.util.function.Function;
import lombok.Getter;


@Getter
public class CharsetDropdownItem<T, R> {

  protected final Function<T, R> func;
  private final String key;
  private final T input;

  public CharsetDropdownItem(String key, T input, Function<T, R> func) {
    this.func = func;
    this.key = key;
    this.input = input;
  }

}
