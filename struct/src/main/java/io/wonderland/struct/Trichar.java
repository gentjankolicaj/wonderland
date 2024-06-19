package io.wonderland.struct;

import io.wonderland.base.CharUtils;
import io.wonderland.base.IntUtils;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.Objects;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public final class Trichar implements Char<byte[][]>, Serializable {

  private static final String A_MESSAGE = "a arg must not be null.";
  private static final String B_MESSAGE = "b arg must not be null.";
  private static final String C_MESSAGE = "c arg must not be null.";

  private final byte[] a;
  private final byte[] b;
  private final byte[] c;

  public Trichar(Monochar a, Monochar b, Monochar c) {
    this.a = Objects.requireNonNull(a, A_MESSAGE).getValue();
    this.b = Objects.requireNonNull(b, B_MESSAGE).getValue();
    this.c = Objects.requireNonNull(c, C_MESSAGE).getValue();
  }

  public Trichar(byte a, byte b, byte c) {
    this.a = new byte[]{a};
    this.b = new byte[]{b};
    this.c = new byte[]{c};
  }

  public Trichar(char a, char b, char c) {
    this.a = CharUtils.getOptimalBytesBE(a);
    this.b = CharUtils.getOptimalBytesBE(b);
    this.c = CharUtils.getOptimalBytesBE(c);
  }

  public Trichar(int a, int b, int c) {
    this.a = IntUtils.getOptimalBytesBE(a);
    this.b = IntUtils.getOptimalBytesBE(b);
    this.c = IntUtils.getOptimalBytesBE(c);
  }

  public Trichar(byte[] a, byte[] b, byte[] c) {
    this.a = Objects.requireNonNull(a, A_MESSAGE);
    this.b = Objects.requireNonNull(b, B_MESSAGE);
    this.c = Objects.requireNonNull(c, C_MESSAGE);
  }

  public Trichar(String a, String b, String c) {
    this.a = a.getBytes();
    this.b = b.getBytes();
    this.c = c.getBytes();
  }

  @Override
  public byte[][] getValue() {
    return new byte[][]{a, b, c};
  }

  public String toString() {
    return new String(a) + new String(b) + new String(c);
  }

  public String toString(Charset charset) {
    return new String(a, charset) + new String(b, charset)
        + new String(c, charset);
  }


}
