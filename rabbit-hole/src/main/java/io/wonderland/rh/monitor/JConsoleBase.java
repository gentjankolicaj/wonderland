package io.wonderland.rh.monitor;

import java.io.BufferedReader;
import java.io.IOException;
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

        // Set the working directory (optional)
        processBuilder.directory(new java.io.File("."));

        // Redirect error stream to the standard output
        processBuilder.redirectErrorStream(true);

        try {
          // Start the process
          Process process = processBuilder.start();

          // Read the output of the process
          BufferedReader reader = new BufferedReader(
              new java.io.InputStreamReader(process.getInputStream()));
          String line;
          while ((line = reader.readLine()) != null) {
            log.info(line);
          }

          // Wait for the process to exit
          int exitCode = process.waitFor();
          log.info("Process exited with code: {}", exitCode);
        } catch (IOException | InterruptedException e) {
          log.error("", e);
        }
      } catch (Exception e) {
        log.error("", e);
      }
    }
  }

}
