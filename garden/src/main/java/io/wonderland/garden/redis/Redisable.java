package io.wonderland.garden.redis;


import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.Charset;

public interface Redisable<I, K, V> extends Serializable {

  char SEPARATOR = ':';
  Charset CHARSET = Charset.defaultCharset();

  I getId();

  K getKey();

  V getValue() throws IOException;

  default Charset getCharset() {
    return CHARSET;
  }


}
