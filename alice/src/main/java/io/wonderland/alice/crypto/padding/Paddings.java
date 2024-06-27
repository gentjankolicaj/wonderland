package io.wonderland.alice.crypto.padding;

import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Paddings {

  NO_PADDING(() -> new NoPadding()), ZERO_PADDING(() -> new ZeroPadding()), ONE_PADDING(
      () -> new OnePadding());

  private final Supplier<Padding> supplier;

  public Padding create() {
    return this.supplier.get();
  }

}
