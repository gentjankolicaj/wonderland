package io.wonderland.rh.base.observer;


import java.util.Objects;
import java.util.Optional;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KeyObserver<N> extends AbstractObserver<N> {

  protected Optional<N> optionalKey = Optional.empty();
  protected boolean updated = true;

  @Override
  public void update(N notification) {
    if (Objects.nonNull(notification)) {
      this.updated = true;
      this.optionalKey = Optional.of(notification);
    }
  }

}
