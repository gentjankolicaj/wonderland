package io.wonderland.garden.dao;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.codec.StringCodec;
import io.wonderland.base.Dao;
import io.wonderland.garden.domain.LetterFreq;
import java.util.List;
import java.util.Optional;

public final class LetterFreqDao implements Dao<LetterFreq, String> {

  private final StatefulRedisConnection<String, String> redisConnection;

  public LetterFreqDao(RedisClient redisClient) {
    this.redisConnection = redisClient.connect(new StringCodec());
  }


  @Override
  public Optional<LetterFreq> get(String id) {
    RedisCommands<String, String> commands = redisConnection.sync();
    return Optional.of(new LetterFreq(id, commands.smembers(id)));
  }

  @Override
  public List<LetterFreq> getAll() {
    RedisCommands<String, String> commands = redisConnection.sync();
    LetterFreq letterFreq = new LetterFreq(commands.smembers(LetterFreq.DEFAULT_KEY));
    return List.of(letterFreq);
  }

  @Override
  public void save(LetterFreq letterFreq) {
    RedisCommands<String, String> commands = redisConnection.sync();
    commands.sadd(letterFreq.getKey(), letterFreq.getLangCodes().toArray(new String[0]));
  }

  @Override
  public void saveAll(LetterFreq... args) {
    throw new UnsupportedOperationException("Method not implemented");
  }

  @Override
  public void update(LetterFreq letterFreq) {
    RedisCommands<String, String> commands = redisConnection.sync();
    commands.sadd(letterFreq.getKey(), letterFreq.getLangCodes().toArray(new String[0]));
  }

  @Override
  public void delete(LetterFreq letterFreq) {
    RedisCommands<String, String> commands = redisConnection.sync();
    commands.srem(letterFreq.getKey(), letterFreq.getLangCodes().toArray(new String[0]));
  }
}
