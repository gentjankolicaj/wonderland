package io.wonderland.struct;

import io.wonderland.base.CharUtils;
import io.wonderland.base.IntUtils;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.Objects;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public final class Dichar implements Char<byte[][]>, Serializable {

  private static final String A_MESSAGE = "a arg must not be null.";
  private static final String B_MESSAGE = "b arg must not be null.";

  private final byte[] a;
  private final byte[] b;

  public Dichar(Monochar a, Monochar b) {
    this.a = Objects.requireNonNull(a, A_MESSAGE).getValue();
    this.b = Objects.requireNonNull(b, B_MESSAGE).getValue();
  }

  public Dichar(byte a, byte b) {
    this.a = new byte[]{a};
    this.b = new byte[]{b};
  }

  public Dichar(char a, char b) {
    this.a = CharUtils.getOptimalBytesBE(a);
    this.b = CharUtils.getOptimalBytesBE(b);
  }

  public Dichar(int a, int b) {
    this.a = IntUtils.getOptimalBytesBE(a);
    this.b = IntUtils.getOptimalBytesBE(b);
  }

  public Dichar(byte[] a, byte[] b) {
    this.a = Objects.requireNonNull(a, A_MESSAGE);
    this.b = Objects.requireNonNull(b, B_MESSAGE);
  }

  public Dichar(String a, String b) {
    this.a = a.getBytes();
    this.b = b.getBytes();
  }

  public byte[][] getValue() {
    byte[][] arr = {a, b};
    return arr;
  }

  public String toString() {
    return new String(a) + new String(b);
  }

  public String toString(Charset charset) {
    return new String(a, charset) + new String(b, charset);
  }

}
