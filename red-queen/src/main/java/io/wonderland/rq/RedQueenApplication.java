package io.wonderland.rq;

import io.wonderland.base.Application;
import io.wonderland.base.ApplicationException;
import io.wonderland.base.ConfigurationException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RedQueenApplication implements Application<ApplicationProperties> {


  public static void main(String[] args) {
    Application<ApplicationProperties> application = new RedQueenApplication();
    try {
      application.start(args);
    } catch (ApplicationException ae) {
      log.error("", ae);
      try {
        application.stop();
      } catch (Exception e) {
        log.error("", e);
      }
    }
  }

  @Override
  public void start(String[] args) throws ApplicationException {
    //to implement analysis server
  }

  @Override
  public void stop() throws ApplicationException {
    //to implement analysis server
  }

  @Override
  public void shutdownHooks() {
    //to implement analysis server
  }

  @Override
  public ApplicationProperties getConfiguration() {
    try {
      return ApplicationProperties.load();
    } catch (ConfigurationException e) {
      log.error("", e);
      return null;
    }
  }
}
