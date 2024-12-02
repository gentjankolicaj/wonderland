package io.wonderland.grpc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GrpcApplicationProperties {

  private String name;

  @JsonProperty(value = "grpcClient")
  private GrpcClientProperties client;

  @JsonProperty(value = "grpcServer")
  private GrpcServerProperties server;
}
