package io.wonderland.rh.base.observer;

import java.util.Objects;
import java.util.Optional;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@SuppressWarnings("all")
public class TypeObserver<T> extends AbstractObserver<T> {

  private boolean updated;
  private Optional<T> value = Optional.empty();

  @Override
  public void update(T t) {
    if (Objects.nonNull(t)) {
      updated = true;
      value = Optional.of(t);
    }
  }

}
