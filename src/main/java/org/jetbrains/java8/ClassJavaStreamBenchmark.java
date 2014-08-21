package org.jetbrains.java8;

import org.jetbrains.JetbrainsPackage;
import org.jetbrains.SizedBenchmark;
import org.jetbrains.Value;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

@State(Scope.Thread)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
public class ClassJavaStreamBenchmark extends SizedBenchmark {
    public ArrayList<Value> data;

    @Setup
    public void setup() {
        ArrayList<Value> list = new ArrayList<>();
        for (Value item : JetbrainsPackage.classValues(getSize())) {
            list.add(item);
        }
        data = list;
    }

    @Benchmark
    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    public Long filterAndCount() {
        return data.stream().filter(it -> (it.getValue() & 1) == 0).count();
    }

    @Benchmark
    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    public void filterAndMap(final Blackhole bh) {
        Stream<Integer> stream = data.stream().filter(it -> (it.getValue() & 1) == 0).map(it -> it.getValue());
        stream.forEach(it -> bh.consume(it));
    }

    @Benchmark
    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    public Long countFiltered() {
        return data.stream().mapToLong(it -> (it.getValue() & 1) == 0 ? 1 : 0).sum();
    }
}
