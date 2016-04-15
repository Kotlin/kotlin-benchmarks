package org.jetbrains.java8;

import org.jetbrains.DataKt;
import org.jetbrains.SizedBenchmark;
import org.jetbrains.Value;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class ClassJavaStreamBenchmark extends SizedBenchmark {
    public ArrayList<Value> data;

    @Setup
    public void setup() {
        ArrayList<Value> list = new ArrayList<>();
        for (Value item : DataKt.classValues(getSize())) {
            list.add(item);
        }
        data = list;
    }

    @Benchmark
    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    public Long filterAndCount() {
        return data.stream().filter(DataKt::filterLoad).count();
    }

    @Benchmark
    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    public Long filterAndCountWithLambda() {
        return data.stream().filter(x -> x.getValue() % 2 == 0).count();
    }

    @Benchmark
    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    public void filterAndMap(final Blackhole bh) {
        Stream<String> stream = data.stream().filter(DataKt::filterLoad).map(DataKt::mapLoad);
        stream.forEach(bh::consume);
    }

    @Benchmark
    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    public Long countFiltered() {
        return data.stream().mapToLong(it -> DataKt.filterLoad(it) ? 1 : 0).sum();
    }
}
