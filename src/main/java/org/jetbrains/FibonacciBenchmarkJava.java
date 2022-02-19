package org.jetbrains;

import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

/**
 * This test checks work with long numbers using Fibonacci sequence
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Thread)
public class FibonacciBenchmarkJava {

    @Benchmark
    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    public long calcClassic(SizedBenchmark sb) {
        int a = 1, b = 2, size = sb.getSize();
        for (int i=0; i<size; i++) {
            int next = a + b;
            a = b;
            b = next;
        }
        return b;
    }

    @Benchmark
    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    public long calc(SizedBenchmark sb) {
        int a = 1, b = 2;
        for (int i=sb.getSize(); i>0; i--) {
            int next = a + b;
            a = b;
            b = next;
        }
        return b;
    }

    @Benchmark
    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    public long calcWithProgression(SizedBenchmark sb) {
        int a = 1, b = 2;
        for (int i=1; i<2*sb.getSize(); i+=2) {
            int next = a + b;
            a = b;
            b = next;
        }
        return b;
    }

    @Benchmark
    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    public long calcSquare(SmallSizedBenchmark sb) {
        int a = 1, b = 2;
        long s = sb.getSmallSize();
        long limit = s*s;
        for (long i=limit; i>0; i--) {
            int next = a + b;
            a = b;
            b = next;
        }
        return b;
    }
}