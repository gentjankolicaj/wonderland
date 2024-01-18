package io.wonderland.alice.charset;

import java.nio.charset.Charset;

public enum Charsets {


  ENGLISH_LOWERCASE {
    @Override
    public Charset getCharset() {
      return EnglishLowerCaseCharset.INSTANCE;
    }
  },


  ENGLISH {
    @Override
    public Charset getCharset() {
      return EnglishCharset.INSTANCE;
    }
  },

  ASCII {
    @Override
    public Charset getCharset() {
      return Charset.forName("ASCII");
    }

  },
  UTF_8 {
    @Override
    public Charset getCharset() {
      return Charset.forName("UTF-8");
    }

  },
  UTF_16 {
    @Override
    public Charset getCharset() {
      return Charset.forName("UTF-16");
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
