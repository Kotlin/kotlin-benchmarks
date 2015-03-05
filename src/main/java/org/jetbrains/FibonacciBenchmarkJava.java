package org.jetbrains;

import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

/**
 * This test checks work with long numbers using Fibonacci sequence
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class FibonacciBenchmarkJava extends SizedBenchmark {
    @Benchmark
    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    public long calc() {
        int a = 1, b = 2;
        for (int i=getSize(); i>0; i--) {
            int next = a + b;
            a = b;
            b = next;
        }
        return b;
    }

    @Benchmark
    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    public long calcSquare() {
        int a = 1, b = 2;
        long s = getSize();
        long limit = s*s;
        for (long i=limit; i>0; i--) {
            int next = a + b;
            a = b;
            b = next;
        }
        return b;
    }
}