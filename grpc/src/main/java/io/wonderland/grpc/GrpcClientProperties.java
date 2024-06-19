package io.wonderland.grpc;

import com.fasterxml.jackson.annotation.JsonRootName;
import java.util.LinkedHashMap;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

@Getter
@Setter
@JsonRootName(value = "grpcClient")
public class GrpcClientProperties extends LinkedHashMap<String, Object> {

  public static GrpcClientProperties of(String host, int port) {
    GrpcClientProperties prop = new GrpcClientProperties();
    prop.put(GrpcConstants.HOST_FIELD, host);
    prop.put(GrpcConstants.PORT_FIELD, port);
    return prop;
  }

  public int getPort() {
    Object ref = this.get(GrpcConstants.PORT_FIELD);
    if (Objects.isNull(ref)) {
      return -1;
    } else {
      return (Integer) ref;
    }
  }

  public String getHost() {
    Object ref = this.get(GrpcConstants.HOST_FIELD);
    if (Objects.isNull(ref)) {
      return StringUtils.EMPTY;
    } else {
      return (String) ref;
    }
  }


}
