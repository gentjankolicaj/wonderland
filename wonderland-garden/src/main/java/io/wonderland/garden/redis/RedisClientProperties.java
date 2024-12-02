package io.wonderland.garden.redis;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class RedisClientProperties {

  private String host;
  private int port;
  private String username;
  private String password;
  private int db;
}