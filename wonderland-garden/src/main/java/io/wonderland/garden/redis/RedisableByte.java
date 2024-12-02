package io.wonderland.garden.redis;

import java.io.IOException;
import org.apache.commons.lang3.SerializationUtils;

public interface RedisableByte<I> extends Redisable<I, byte[], byte[]> {


  default byte[] getKey() {
    return (this.getClass().getSimpleName().toLowerCase() + SEPARATOR + getId()).getBytes(
        getCharset());
  }

  default byte[] getValue() throws IOException {
    return SerializationUtils.serialize(this);
  }

}
