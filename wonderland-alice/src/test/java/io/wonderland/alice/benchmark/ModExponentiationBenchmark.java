package io.wonderland.alice.benchmark;

import java.io.IOException;
import org.junit.jupiter.api.Disabled;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Warmup;

/**
 * More on how <a
 * href="https://hg.openjdk.org/code-tools/jmh/file/6cc1450c6a0f/jmh-core/src/main/java/org/openjdk/jmh/results">result
 * calculation</a> is done.
 */
@SuppressWarnings("ALL")
@Disabled
public class ModExponentiationBenchmark {

  public static void main(String[] args) throws IOException {
    org.openjdk.jmh.Main.main(args);
  }

  @Warmup(iterations = 1)
  @Fork(value = 2)
  @Measurement(iterations = 3)
  @Benchmark
  @BenchmarkMode(Mode.All)
  public void multiplyReduce() {

  }

  @Warmup(iterations = 1)
  @Fork(value = 2)
  @Measurement(iterations = 3)
  @Benchmark
  @BenchmarkMode(Mode.All)
  public void squareMultiply() {

  }
}