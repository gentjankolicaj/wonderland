package io.wonderland.rh.base.dropdown;

import io.wonderland.rh.base.TypeObserver;
import java.util.Objects;
import java.util.function.BiConsumer;
import lombok.Getter;

@Getter
public class DropdownElement<K,T,R> {
  private final K key;
  private final BiConsumer<TypeObserver<T>,R> biConsumer;
  public DropdownElement(K key,BiConsumer<TypeObserver<T>,R> biConsumer){
    Objects.requireNonNull(key);
    this.key=key;
    this.biConsumer=biConsumer;
  }


  public void runConsumer(TypeObserver<T> observer,R r){
    biConsumer.accept(observer,r);
  }

}
