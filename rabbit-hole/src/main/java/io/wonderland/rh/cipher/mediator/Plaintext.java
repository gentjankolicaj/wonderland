package io.wonderland.rh.cipher.mediator;

import io.wonderland.rh.base.mediator.Component;
import io.wonderland.rh.base.mediator.Mediator;
import lombok.Setter;
import org.apache.commons.lang3.ArrayUtils;

@Setter
public class Plaintext implements Component<String> {

  public static final String KEY = "Plaintext";
  private Mediator<String> mediator;
  private byte[] value;

  @Override
  public String getKey() {
    return KEY;
  }

  @Override
  public byte[] getValue() {
    return value;
  }

  @Override
  public void clear() {
    this.value = ArrayUtils.EMPTY_BYTE_ARRAY;
  }
}
