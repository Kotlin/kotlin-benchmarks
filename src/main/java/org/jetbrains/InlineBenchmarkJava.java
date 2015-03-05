package org.jetbrains;

import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class InlineBenchmarkJava extends SizedBenchmark {
    private int data = 2138476523;

    @Benchmark
    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    public int calculate() {
        return load(data);
    }

    @Benchmark
    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    public int calculateGeneric() {
        return loadGeneric(data);
    }

    @Benchmark
    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    public int calculateManual() {
        Integer boxed = data;
        int acc = 0;
        for (int i = 0; i < getSize(); i++) {
            acc = acc ^ boxed.hashCode();
        }
        return acc;
    }

    private int load(int data) {
        int acc = 0;
        for (int i = 0; i < getSize(); i++) {
            acc = acc ^ Integer.valueOf(data).hashCode();
        }
        return acc;
    }

    private <T> int loadGeneric(T data) {
        int acc = 0;
        for (int i = 0; i < getSize(); i++) {
            acc = acc ^ data.hashCode();
        }
        return acc;
    }
}
