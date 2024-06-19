package io.wonderland.rh;


import com.fasterxml.jackson.annotation.JsonProperty;
import io.wonderland.base.YamlUtils;
import io.wonderland.grpc.GrpcClientProperties;
import java.io.IOException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ApplicationProperties {

  private String name;

  @JsonProperty(value = "grpcClient")
  private GrpcClientProperties grpcClient;


  public static ApplicationProperties fromClasspath() throws IOException {
    return YamlUtils.readClasspath(ApplicationProperties.class,
        "/application.yml");
  }

}
