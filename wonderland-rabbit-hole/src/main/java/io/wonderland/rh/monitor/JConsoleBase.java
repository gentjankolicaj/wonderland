package io.wonderland.rh.monitor;

import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class JConsoleBase {

  private static final String CMD = "jconsole";

  private JConsoleBase() {
  }

  public static void start(long delay, long pid) {
    CompletableFuture.runAsync(() -> new JConsoleRunnable(delay, pid).run());
  }

  @RequiredArgsConstructor
  static class JConsoleRunnable implements Runnable {

    private final long delay;
    private final long pid;

    @Override
    public void run() {
      try {
        Thread.sleep(delay);

        // Create a ProcessBuilder instance
        ProcessBuilder processBuilder = new ProcessBuilder(CMD, "" + pid);

        // Start the process
        processBuilder.start();
      } catch (Exception e) {
        log.error("", e);
      }
    }
  }

}
