package org.jetbrains.java8;

import org.jetbrains.SizedBenchmark;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * @author yole
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class LambdaBenchmarkJava extends SizedBenchmark {
  public static int globalAddendum;

  private <T> T runLambda(Supplier<T> supplier) {
    return supplier.get();
  }

  @CompilerControl(CompilerControl.Mode.DONT_INLINE)
  private <T> T runLambdaNoInline(Supplier<T> supplier) {
    return supplier.get();
  }

  @Setup
  public void init() {
    globalAddendum = ThreadLocalRandom.current().nextInt(20);
  }

  @Benchmark
  @CompilerControl(CompilerControl.Mode.DONT_INLINE)
  public int noncapturingLambda() {
    int x = 0;
    for (int i=0; i<getSize(); i++) {
      x += runLambda(() -> globalAddendum);
    }
    return x;
  }

  @Benchmark
  @CompilerControl(CompilerControl.Mode.DONT_INLINE)
  public int noncapturingLambdaNoInline() {
    int x = 0;
    for (int i=0; i<getSize(); i++) {
      x += runLambdaNoInline(() -> globalAddendum);
    }
    return x;
  }

  @Benchmark
  @CompilerControl(CompilerControl.Mode.DONT_INLINE)
  public int capturingLambda() {
    int x = 0;
    int addendum = globalAddendum + 1;
    for (int i=0; i<getSize(); i++) {
      x += runLambda(() -> addendum);
    }
    return x;
  }

  @Benchmark
  @CompilerControl(CompilerControl.Mode.DONT_INLINE)
  public int capturingLambdaNoInline() {
    int x = 0;
    int addendum = globalAddendum + 1;
    for (int i=0; i<getSize(); i++) {
      x += runLambdaNoInline(() -> addendum);
    }
    return x;
  }
}
