package io.wonderland.grpc;

import io.atlassian.fugue.Pair;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.lang3.StringUtils;

public final class ManagedChannels {

  private static final int MAX_INBOUND_MSG_SIZE = 10 * 1024 * 1024;
  private static final Map<Integer, ManagedChannel> CHANNELS = new ConcurrentHashMap<>();
  private static int counter;

  private ManagedChannels() {
  }


  public static Map<Integer, ManagedChannel> getChannels() {
    return CHANNELS;
  }

  public static ManagedChannel getChannel(Integer key) {
    return CHANNELS.get(key);
  }

  public static synchronized Pair<Integer, ManagedChannel> createChannel(
      GrpcClientProperties properties) {
    String host = StringUtils.isEmpty(properties.getHost()) ? GrpcConstants.DEFAULT_HOST
        : properties.getHost();
    int port = properties.getPort() == -1 ? GrpcConstants.DEFAULT_PORT : properties.getPort();
    ManagedChannel managedChannel = ManagedChannelBuilder.forAddress(host, port)
        .maxInboundMessageSize(MAX_INBOUND_MSG_SIZE)
        .usePlaintext().build();
    CHANNELS.put(counter++, managedChannel);
    return new Pair<>(counter, managedChannel);
  }

  public static synchronized Pair<Integer, ManagedChannel> createChannel(Map<String, Object> map) {
    String host =
        StringUtils.isEmpty((String) map.get(GrpcConstants.HOST_FIELD)) ? GrpcConstants.DEFAULT_HOST
            : (String) map.get(GrpcConstants.HOST_FIELD);
    int port = (Integer) map.get(GrpcConstants.PORT_FIELD) == -1 ? GrpcConstants.DEFAULT_PORT
        : (Integer) map.get(GrpcConstants.PORT_FIELD);
    ManagedChannel managedChannel = ManagedChannelBuilder.forAddress(host, port)
        .maxInboundMessageSize(MAX_INBOUND_MSG_SIZE)
        .usePlaintext().build();
    CHANNELS.put(counter++, managedChannel);
    return new Pair<>(counter, managedChannel);
  }


}
