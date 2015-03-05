package org.jetbrains;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class ClassBaselineBenchmarkJava extends SizedBenchmark {

    static class LocalValue {
        int value;
    }

    @Benchmark
    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    public void consume(Blackhole bh) {
        for (int i = 0; i < getSize(); i++) {
            bh.consume(new Value(i));
        }
    }

    @Benchmark
    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    public void consumeField(Blackhole bh) {
        LocalValue value = new LocalValue();
        for (int i = 0; i < getSize(); i++) {
            value.value = i;
            bh.consume(value);
        }
    }

    @Benchmark
    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    public List<Value> allocateList() {
        return new ArrayList<Value>(getSize());
    }

    @Benchmark
    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    public Value[] allocateArray() {
        return new Value[getSize()];
    }

    @Benchmark
    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    public List<Value> allocateListAndFill() {
        ArrayList<Value> list = new ArrayList<Value>(getSize());
        for (int i = 0; i < getSize(); i++) {
            list.add(new Value(i));
        }
        return list;
    }

    @Benchmark
    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    public List<Value> allocateListAndWrite() {
        Value value = new Value(0);
        ArrayList<Value> list = new ArrayList<Value>(getSize());
        for (int i = 0; i < getSize(); i++) {
            list.add(value);
        }
        return list;
    }

    @Benchmark
    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    public Value[] allocateArrayAndFill() {
        Value[] list = new Value[getSize()];
        for (int i = 0; i < getSize(); i++) {
            list[i] = new Value(i);
        }
        return list;
    }

    @Benchmark
    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    public Value[] allocateArrayAndWrite() {
        Value value = new Value(0);
        Value[] list = new Value[getSize()];
        for (int i = 0; i < getSize(); i++) {
            list[i] = value;
        }
        return list;
    }
}
