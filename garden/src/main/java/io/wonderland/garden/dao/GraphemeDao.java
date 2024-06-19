package io.wonderland.garden.dao;

import io.lettuce.core.LettuceFutures;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisFuture;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.api.sync.RedisCommands;
import io.wonderland.base.Dao;
import io.wonderland.garden.domain.Grapheme;
import io.wonderland.garden.redis.BAValueCodec;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.ArrayUtils;

public final class GraphemeDao implements Dao<Grapheme, String> {

  private final StatefulRedisConnection<String, byte[]> redisConnection;

  public GraphemeDao(RedisClient redisClient) {
    this.redisConnection = redisClient.connect(BAValueCodec.getInstance());
  }


  @Override
  public Optional<Grapheme> get(String id) {
    RedisCommands<String, byte[]> commands = redisConnection.sync();
    return Optional.of(new Grapheme(id, commands.hgetall(id)));
  }

  @Override
  public List<Grapheme> getAll() {
    throw new UnsupportedOperationException("Can't get all graphemes without keys");
  }

  @Override
  public void save(Grapheme grapheme) {
    RedisCommands<String, byte[]> commands = redisConnection.sync();
    commands.hset(grapheme.getKey(), grapheme.getFreq());
  }

  public void saveAll(Grapheme... graphemes) {
    if (ArrayUtils.isNotEmpty(graphemes)) {
      RedisAsyncCommands<String, byte[]> commands = redisConnection.async();
      List<RedisFuture<Long>> futures = new ArrayList<>();
      for (Grapheme grapheme : graphemes) {
        futures.add(commands.hset(grapheme.getKey(), grapheme.getFreq()));
      }
      redisConnection.flushCommands();
      LettuceFutures.awaitAll(5, TimeUnit.SECONDS,
          futures.toArray(new RedisFuture[futures.size()]));
    }
  }

  @Override
  public void update(Grapheme grapheme) {
    RedisCommands<String, byte[]> commands = redisConnection.sync();
    commands.hset(grapheme.getKey(), grapheme.getFreq());
  }

  @Override
  public void delete(Grapheme grapheme) {
    RedisCommands<String, byte[]> commands = redisConnection.sync();
    commands.hdel(grapheme.getKey());
  }
}
