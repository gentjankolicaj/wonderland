package io.wonderland.garden.redis;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.RedisURI.Builder;
import org.apache.commons.lang3.StringUtils;

public final class RedisHelper {

  private RedisHelper() {
  }


  public static RedisClient setupClient(RedisClientProperties redisClientProperties) {
    Builder builder = RedisURI.Builder.redis(redisClientProperties.getHost())
        .withPort(redisClientProperties.getPort());
    if (StringUtils.isNotEmpty(redisClientProperties.getUsername())) {
      builder.withAuthentication(redisClientProperties.getUsername(),
          redisClientProperties.getPassword());
    } else if (StringUtils.isNotEmpty(redisClientProperties.getPassword())) {
      builder.withPassword(redisClientProperties.getPassword().toCharArray());
    }
    if (redisClientProperties.getDb() != 0) {
      builder.withDatabase(redisClientProperties.getDb());
    } else {
      builder.withDatabase(0);
    }
    return RedisClient.create(builder.build());
  }

}
