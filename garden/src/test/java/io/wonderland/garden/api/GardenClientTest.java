package io.wonderland.garden.api;


import static org.assertj.core.api.Assertions.assertThat;

import io.grpc.ManagedChannel;
import io.wonderland.garden.ITest;
import io.wonderland.garden.domain.Grapheme;
import io.wonderland.grpc.GrpcClientProperties;
import io.wonderland.grpc.ManagedChannels;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GardenClientTest extends ITest {

  @BeforeEach
  void openChannel() {
    if (ManagedChannels.getChannel(0) == null) {
      GrpcClientProperties props = GrpcClientProperties.of("localhost", 9001);
      ManagedChannels.createChannel(props);
    }
  }

  @Test
  void getLetterFreqLangCodes() {
    ManagedChannel managedChannel = ManagedChannels.getChannel(0);
    GardenClient gardenClient = new GardenClient(managedChannel);
    assertThat(gardenClient.getLetterFreqLangCodes()).isNotEmpty().hasSizeGreaterThan(1);
  }

  @Test
  void getGrapheme() {
    ManagedChannel managedChannel = ManagedChannels.getChannel(0);
    GardenClient gardenClient = new GardenClient(managedChannel);
    Grapheme grapheme = gardenClient.getGrapheme("en");
    assertThat(grapheme).isNotNull();
    assertThat(grapheme.getKey()).isEqualTo("en");
    assertThat(grapheme.getFreq()).hasSizeGreaterThan(3);
  }


}