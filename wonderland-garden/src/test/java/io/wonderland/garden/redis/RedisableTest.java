package io.wonderland.garden.redis;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.charset.Charset;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.SerializationUtils;
import org.junit.jupiter.api.Test;

class RedisableTest {


  @Test
  void redisable() throws IOException {
    Redisable<Long, byte[], byte[]> user = new User(10L, "John", "Doe", 3.14);
    assertThat(user.getKey()).isEqualTo(
        (user.getClass().getSimpleName().toLowerCase() + ":" + 10).getBytes(
            Charset.defaultCharset()));
    assertThatCode(user::getValue).doesNotThrowAnyException();
    assertThat(user.getValue()).isEqualTo(SerializationUtils.serialize(user));
  }

  @Test
  void redisableByte() throws IOException {
    RedisableByte<Long> userB = new UserB(10L, "John", "Doe", 3.14);
    assertThat(userB.getKey()).isEqualTo(
        (userB.getClass().getSimpleName().toLowerCase() + ":" + 10).getBytes(
            Charset.defaultCharset()));
    assertThatCode(userB::getValue).doesNotThrowAnyException();
    assertThat(userB.getValue()).isEqualTo(SerializationUtils.serialize(userB));

  }

  @Test
  void redisableString() throws IOException {
    RedisableString<Long> userS = new UserS(10L, "John", "Doe", 3.14);
    assertThat(userS.getKey()).isEqualTo(userS.getClass().getSimpleName().toLowerCase() + ":" + 10);
    assertThatCode(userS::getValue).doesNotThrowAnyException();
    assertThat(userS.getValue()).isEqualTo(
        new String(SerializationUtils.serialize(userS), userS.getCharset()));
  }


  @Getter
  @RequiredArgsConstructor
  static class User implements Redisable<Long, byte[], byte[]> {

    private final Long id;
    private final String name;
    private final String surname;
    private final double age;

    public byte[] getKey() {
      return (this.getClass().getSimpleName().toLowerCase() + SEPARATOR + getId()).getBytes(
          getCharset());
    }

    public byte[] getValue() throws IOException {
      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      ObjectOutputStream oos = new ObjectOutputStream(bos);
      oos.writeObject(this);
      oos.flush();
      return bos.toByteArray();
    }

  }

  @Getter
  @RequiredArgsConstructor
  static class UserB implements RedisableByte<Long> {

    private final Long id;
    private final String name;
    private final String surname;
    private final double age;

  }

  @Getter
  @RequiredArgsConstructor
  static class UserS implements RedisableString<Long> {

    private final Long id;
    private final String name;
    private final String surname;
    private final double age;

  }


}