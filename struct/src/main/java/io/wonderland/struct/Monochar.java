package io.wonderland.struct;

import io.wonderland.base.CharUtils;
import io.wonderland.base.IntUtils;
import java.io.Serializable;
import java.nio.charset.Charset;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public final class Monochar implements Char<byte[]>, Serializable {

  private final byte[] value;

  public Monochar(byte b) {
    this.value = new byte[]{b};
  }

  public Monochar(char c) {
    this.value = CharUtils.getOptimalBytesBE(c);
  }

  public Monochar(int i) {
    this.value = IntUtils.getOptimalBytesBE(i);
  }

  public Monochar(byte... value) {
    this.value = value;
  }

  public Monochar(String value) {
    this.value = value.getBytes();
  }

  public String toString() {
    return new String(value);
  }

  public String toString(Charset charset) {
    return new String(value, charset);
  }

}
