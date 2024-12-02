package io.wonderland.rh.api;

import io.grpc.ManagedChannel;
import io.wonderland.garden.api.GardenClient;
import io.wonderland.grpc.ManagedChannels;
import io.wonderland.rh.analysis.service.BackgroundServices;
import io.wonderland.rh.base.fx.ExceptionDialog;
import java.util.Map;
import java.util.Optional;
import lombok.Getter;

public final class Clients {

  @Getter
  private static Optional<GardenClient> gardenClient = Optional.empty();

  private Clients() {
  }


  public static Optional<GardenClient> createGardenClient(Map<String, Object> params) {
    try {
      ManagedChannel managedChannel = ManagedChannels.createChannel(params).right();
      GardenClient ref = new GardenClient(managedChannel);

      //just sent ping to test connection
      ref.ping();
      gardenClient = Optional.of(ref);
      BackgroundServices.setGardenClient(ref);
      return gardenClient;
    } catch (Exception e) {
      ExceptionDialog ed = new ExceptionDialog(e);
      ed.showAndWait();
      return Optional.empty();
    }
  }

}
