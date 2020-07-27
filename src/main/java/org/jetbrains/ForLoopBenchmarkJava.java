package org.jetbrains;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@CompilerControl(CompilerControl.Mode.DONT_INLINE)
public class ForLoopBenchmarkJava extends SizedBenchmark {

    @Benchmark
    public final void intUntilLoop(Blackhole bh) {
        int size = getSize();
        for (int i = 1; i < size; i++) {
            bh.consume(i);
        }
    }

    @Benchmark
    public final void intRangeLiteralLoop(Blackhole bh) {
        int size = getSize();
        for (int i = 1; i <= size; i++) {
            bh.consume(i);
        }
    }
}
