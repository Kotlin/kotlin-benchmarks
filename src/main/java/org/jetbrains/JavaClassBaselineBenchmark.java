package org.jetbrains;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class JavaClassBaselineBenchmark extends SizedBenchmark {

    static class LocalValue {
        int value;
    }

    @Benchmark
    public void consume(Blackhole bh) {
        for (int i = 0; i < getSize(); i++) {
            bh.consume(new Value(i));
        }
    }

    @Benchmark
    public void consumeField(Blackhole bh) {
        LocalValue value = new LocalValue();
        for (int i = 0; i < getSize(); i++) {
            value.value = i;
            bh.consume(value);
        }
    }

    @Benchmark
    public List<Value> allocateList() {
        return new ArrayList<Value>(getSize());
    }

    @Benchmark
    public Value[] allocateArray() {
        return new Value[getSize()];
    }

    @Benchmark
    public List<Value> allocateListAndFill() {
        ArrayList<Value> list = new ArrayList<>(getSize());
        for (int i = 0; i < getSize(); i++) {
            list.add(new Value(i));
        }
        return list;
    }

    @Benchmark
    public List<Value> allocateListAndWrite() {
        Value value = new Value(0);
        ArrayList<Value> list = new ArrayList<>(getSize());
        for (int i = 0; i < getSize(); i++) {
            list.add(value);
        }
        return list;
    }

    @Benchmark
    public Value[] allocateArrayAndFill() {
        Value[] list = new Value[getSize()];
        for (int i = 0; i < getSize(); i++) {
            list[i] = new Value(i);
        }
        return list;
    }

    @Benchmark
    public Value[] allocateArrayAndWrite() {
        Value value = new Value(0);
        Value[] list = new Value[getSize()];
        for (int i = 0; i < getSize(); i++) {
            list[i] = value;
        }
        return list;
    }
}
