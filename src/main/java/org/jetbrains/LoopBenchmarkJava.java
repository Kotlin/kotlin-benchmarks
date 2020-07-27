package org.jetbrains;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@CompilerControl(CompilerControl.Mode.DONT_INLINE)
public class LoopBenchmarkJava extends SizedBenchmark {
    public ArrayList<Value> arrayList;
    public Value[] array;

    @Setup
    public void setUp() {
        arrayList = new ArrayList<>(getSize());
        for (Value item : DataKt.classValues(getSize())) {
            arrayList.add(item);
        }
        array = arrayList.toArray(new Value[0]);
    }

    @Benchmark
    public final void arrayIndexLoop(Blackhole bh) {
        int length = array.length;
        for (int i = 0; i < length; i++) {
            bh.consume(array[i]);
        }
    }

    @Benchmark
    public final void arrayListIndexLoop(Blackhole bh) {
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            bh.consume(arrayList.get(i));
        }
    }

    @Benchmark
    public final void arrayListLoop(Blackhole bh) {
        for (Value x : arrayList) {
            bh.consume(x);
        }
    }

    @Benchmark
    public final void arrayWhileLoop(Blackhole bh) {
        int i = 0;
        int s = array.length;
        while (i < s) {
            bh.consume(array[i]);
            i++;
        }
    }

    @Benchmark
    public final void arrayForeachLoop(Blackhole bh) {
        //no forEach
        for (Value v : array) {
            bh.consume(v);
        }
    }

    @Benchmark
    public final void arrayListForeachLoop(Blackhole bh) {
        arrayList.forEach(it -> bh.consume(it));
    }
}
