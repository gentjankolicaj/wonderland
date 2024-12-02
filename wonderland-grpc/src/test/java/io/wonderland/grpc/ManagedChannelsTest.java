package io.wonderland.grpc;


import static org.assertj.core.api.Assertions.assertThat;

import io.atlassian.fugue.Pair;
import io.grpc.ManagedChannel;
import io.wonderland.base.YamlUtils;
import java.io.IOException;
import org.junit.jupiter.api.Test;

class ManagedChannelsTest {

  @Test
  void createChannel() throws IOException {
    GrpcClientProperties properties = YamlUtils.read(GrpcClientProperties.class, "grpc_client.yml");
    Pair<Integer, ManagedChannel> pair = ManagedChannels.createChannel(properties);
    assertThat(pair.left()).isEqualTo(1);
    assertThat(pair.right()).isNotNull();
    assertThat(ManagedChannels.getChannels()).hasSize(1);
  }
}