package io.wonderland.rq;


import com.fasterxml.jackson.annotation.JsonProperty;
import io.wonderland.base.ConfigurationException;
import io.wonderland.base.YamlConfigurations;
import io.wonderland.grpc.GrpcClientProperties;
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

  @JsonProperty(value = "grpcClient")
  private GrpcClientProperties grpcClient;

  public static ApplicationProperties load() throws ConfigurationException {
    return YamlConfigurations.load(ApplicationProperties.class, "/application.yml");
  }
}
