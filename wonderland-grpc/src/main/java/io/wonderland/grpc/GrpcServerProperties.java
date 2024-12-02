package io.wonderland.grpc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonRootName;
import java.util.LinkedHashMap;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonRootName(value = "grpcServer")
@JsonIgnoreProperties(ignoreUnknown = true)
public class GrpcServerProperties extends LinkedHashMap<String, Object> {

  public int getPort() {
    Object ref = this.get(GrpcConstants.PORT_FIELD);
    if (Objects.isNull(ref)) {
      return -1;
    } else {
      return (Integer) ref;
    }
  }

}
