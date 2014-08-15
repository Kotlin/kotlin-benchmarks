package org.jetbrains;

import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
public class JavaInlineBenchmark {
    private int data = 2138476523;
    int loadSize = 100000;

    @Benchmark
    public int calculate() {
        return load(data);
    }

    @Benchmark
    public int calculateGeneric() {
        return loadGeneric(data);
    }

    @Benchmark
    public int calculateManual() {
        Integer boxed = data;
        int acc = 0;
        for (int i = 0; i < loadSize; i++) {
            acc = acc ^ boxed.hashCode();
        }
        return acc;
    }

    private int load(int data) {
        int acc = 0;
        for (int i = 0; i < loadSize; i++) {
            acc = acc ^ Integer.valueOf(data).hashCode();
        }
        return acc;
    }

    private <T> int loadGeneric(T data) {
        int acc = 0;
        for (int i = 0; i < loadSize; i++) {
            acc = acc ^ data.hashCode();
        }
        return acc;
    }
}
