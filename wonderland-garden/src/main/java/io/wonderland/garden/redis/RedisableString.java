package io.wonderland.garden.redis;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public interface RedisableString<I> extends Redisable<I, String, String> {

  default String getKey() {
    return this.getClass().getSimpleName().toLowerCase() + SEPARATOR + getId();
  }

  default String getValue() throws IOException {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    ObjectOutputStream oos = new ObjectOutputStream(bos);
    oos.writeObject(this);
    oos.flush();
    return bos.toString(getCharset());
  }

}
