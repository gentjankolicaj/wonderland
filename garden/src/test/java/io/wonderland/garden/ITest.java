package io.wonderland.garden;

import static org.assertj.core.api.Assertions.assertThat;

import io.wonderland.base.Application;
import io.wonderland.garden.redis.RedisClientProperties;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
@Slf4j
public class ITest {

  @Container
  static GenericContainer<?> redis = new GenericContainer<>(
      DockerImageName.parse("redis:7.2.5-alpine"))
      .withExposedPorts(6379)
      .waitingFor(Wait.forLogMessage(".*Ready to accept connections.*\\n", 1));


  static Application<ApplicationProperties> gardenApplication = new GardenApplication() {

    /**
     * Override properties because of using test container runtime values
     * @return ApplicationProperties
     */
    @Override
    public ApplicationProperties getConfiguration() {
      try {
        ApplicationProperties properties = ApplicationProperties.loadProps();
        properties.setRedisClient(
            new RedisClientProperties(redis.getHost(), redis.getFirstMappedPort(), null, null, 0));
        return properties;
      } catch (Exception e) {
        log.error("", e);
        return null;
      }
    }
  };

  @BeforeAll
  public static void onStartup() throws InterruptedException {
    //start test container
    redis.start();

    //start application async because grpc-server blocks
    CompletableFuture.runAsync(() -> gardenApplication.start(new String[]{}));
    log.info("OnStartup executed,application.start() invoked.");

    //artificial delay to wait for redis & grpc server startups
    Thread.sleep(6000);
  }

  @AfterAll
  public static void onShutdown() {
    //stop test container
    gardenApplication.stop();
    log.info("OnShutdown executed, application.stop() invoked.");
    redis.stop();
  }

  @Test
  void checkProps() throws IOException {
    assertThat(gardenApplication.getConfiguration()).isNotNull();
  }

}
