package io.wonderland.alice.charset;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public enum Charsets {


  ASCII {
    @Override
    public Charset getCharset() {
      return StandardCharsets.US_ASCII;
    }

  },
  UTF_8 {
    @Override
    public Charset getCharset() {
      return StandardCharsets.UTF_8;
    }

  },
  UTF_16 {
    @Override
    public Charset getCharset() {
      return StandardCharsets.UTF_16;
    }

  },
  UTF_32 {
    @Override
    public Charset getCharset() {
      return Charset.forName("UTF-32");
    }

  };

  public abstract Charset getCharset();

}
