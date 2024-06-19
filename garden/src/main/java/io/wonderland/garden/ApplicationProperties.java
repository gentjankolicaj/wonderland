package io.wonderland.garden;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.wonderland.base.ConfigurationException;
import io.wonderland.base.YamlConfigurations;
import io.wonderland.garden.redis.RedisClientProperties;
import io.wonderland.grpc.GrpcServerProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ApplicationProperties {

  private String name;

  @JsonProperty(value = "grpcServer")
  private GrpcServerProperties grpcServer;

  @JsonProperty(value = "redisClient")
  private RedisClientProperties redisClient;


  public static ApplicationProperties loadProps() throws ConfigurationException {
    return YamlConfigurations.load(ApplicationProperties.class, "/application.yml");
  }

}
