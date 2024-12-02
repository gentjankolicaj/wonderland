package io.wonderland.garden;


import io.lettuce.core.RedisClient;
import io.wonderland.base.Application;
import io.wonderland.base.ApplicationException;
import io.wonderland.garden.dao.GraphemeDao;
import io.wonderland.garden.dao.LetterFreqDao;
import io.wonderland.garden.grpc.LetterFreqService;
import io.wonderland.garden.redis.RedisHelper;
import io.wonderland.garden.resource.GardenFilesResource;
import io.wonderland.grpc.GrpcServer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GardenApplication implements Application<ApplicationProperties> {

  private RedisClient redisClient;
  private GrpcServer grpcServer;


  public static void main(String[] args) {
    GardenApplication gardenApplication = new GardenApplication();
    try {
      gardenApplication.start(args);
    } catch (ApplicationException e) {
      log.error("", e);
    }
  }


  @Override
  public void start(String[] args) throws ApplicationException {
    try {
      ApplicationProperties props = getConfiguration();
      log.info("Application properties: {}", props);
      this.redisClient = RedisHelper.setupClient(props.getRedisClient());
      LetterFreqDao letterFreqDao = new LetterFreqDao(redisClient);
      GraphemeDao graphemeDao = new GraphemeDao(redisClient);
      GardenFilesResource.loadInRedis(new LetterFreqDao(redisClient), new GraphemeDao(redisClient));
      LetterFreqService letterFreqService = new LetterFreqService(letterFreqDao, graphemeDao);
      this.grpcServer = GrpcServer.setupServer(props.getGrpcServer(), letterFreqService);

      //set shutdown hooks
      shutdownHooks();

      //Await termination on the main thread since the grpc library uses daemon threads.
      this.grpcServer.blockUntilShutdown();
    } catch (InterruptedException e) {
      log.error("", e);
      Thread.currentThread().interrupt();
    }
  }

  @Override
  public void stop() throws ApplicationException {
    if (this.redisClient != null) {
      this.redisClient.shutdown();
    }
    if (this.grpcServer != null && this.grpcServer.isActive()) {
      try {
        this.grpcServer.stop();
      } catch (InterruptedException e) {
        log.error(e.getMessage(), e);
        Thread.currentThread().interrupt();
      }
    }
  }

  @Override
  public void shutdownHooks() {
    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      if (this.redisClient != null) {
        redisClient.shutdown();
      }
    }));

    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      if (this.grpcServer != null && this.grpcServer.isActive()) {
        try {
          this.grpcServer.stop();
        } catch (InterruptedException e) {
          log.error(e.getMessage(), e);
          Thread.currentThread().interrupt();
        }
      }
    }));
  }

  @Override
  public ApplicationProperties getConfiguration() {
    try {
      return ApplicationProperties.loadProps();
    } catch (Exception e) {
      log.error("", e);
      return null;
    }
  }
}
