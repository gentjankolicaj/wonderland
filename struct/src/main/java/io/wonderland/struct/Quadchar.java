package io.wonderland.struct;

import io.wonderland.base.CharUtils;
import io.wonderland.base.IntUtils;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.Objects;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public final class Quadchar implements Char<byte[][]>, Serializable {

  private static final String A_MESSAGE = "a arg must not be null.";
  private static final String B_MESSAGE = "b arg must not be null.";
  private static final String C_MESSAGE = "c arg must not be null.";
  private static final String D_MESSAGE = "d arg must not be null.";

  private final byte[] a;
  private final byte[] b;
  private final byte[] c;
  private final byte[] d;

  public Quadchar(Monochar a, Monochar b, Monochar c, Monochar d) {
    this.a = Objects.requireNonNull(a, A_MESSAGE).getValue();
    this.b = Objects.requireNonNull(b, B_MESSAGE).getValue();
    this.c = Objects.requireNonNull(c, C_MESSAGE).getValue();
    this.d = Objects.requireNonNull(d, D_MESSAGE).getValue();
  }

  public Quadchar(byte a, byte b, byte c, byte d) {
    this.a = new byte[]{a};
    this.b = new byte[]{b};
    this.c = new byte[]{c};
    this.d = new byte[]{d};
  }

  public Quadchar(char a, char b, char c, char d) {
    this.a = CharUtils.getOptimalBytesBE(a);
    this.b = CharUtils.getOptimalBytesBE(b);
    this.c = CharUtils.getOptimalBytesBE(c);
    this.d = CharUtils.getOptimalBytesBE(d);
  }

  public Quadchar(int a, int b, int c, int d) {
    this.a = IntUtils.getOptimalBytesBE(a);
    this.b = IntUtils.getOptimalBytesBE(b);
    this.c = IntUtils.getOptimalBytesBE(c);
    this.d = IntUtils.getOptimalBytesBE(d);
  }

  public Quadchar(byte[] a, byte[] b, byte[] c, byte[] d) {
    this.a = Objects.requireNonNull(a, A_MESSAGE);
    this.b = Objects.requireNonNull(b, B_MESSAGE);
    this.c = Objects.requireNonNull(c, C_MESSAGE);
    this.d = Objects.requireNonNull(d, D_MESSAGE);
  }

  public Quadchar(String a, String b, String c, String d) {
    this.a = a.getBytes();
    this.b = b.getBytes();
    this.c = c.getBytes();
    this.d = d.getBytes();
  }


  @Override
  public byte[][] getValue() {
    byte[][] arr = {a, b, c, d};
    return arr;
  }


  public String toString() {
    return new String(a) + new String(b) + new String(c) + new String(d);
  }

  public String toString(Charset charset) {
    return new String(a, charset) + new String(b, charset)
        + new String(c, charset) + new String(d, charset);
  }

}
