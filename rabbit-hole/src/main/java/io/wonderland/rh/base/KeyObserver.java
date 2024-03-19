package io.wonderland.rh.base;


import java.security.Key;
import java.security.KeyPair;
import java.util.Objects;
import java.util.Optional;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KeyObserver<T> extends AbstractObserver<T> {

  private boolean isPublicKeySelected;
  private Optional<T> optionalKey=Optional.empty();
  private boolean updated=true;

  @Override
  public void update(T notification) {
    if(Objects.nonNull(notification)){
      this.updated=true;
      if (notification instanceof Key || notification instanceof KeyPair) {
        this.optionalKey=Optional.of(notification);
      }
    }
  }

}
