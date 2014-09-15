package org.jetbrains.java8;

import org.jetbrains.JetbrainsPackage;
import org.jetbrains.SizedBenchmark;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

@State(Scope.Thread)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
public class IntJavaStreamBenchmark extends SizedBenchmark {
    public ArrayList<Integer> data;

    @Setup
    public void setup() {
        ArrayList<Integer> list = new ArrayList<Integer>();
        for (Integer item : JetbrainsPackage.intValues(getSize())) {
            list.add(item);
        }
        data = list;
    }

    @Benchmark
    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    public Long filterAndCount() {
        return data.stream().filter(JetbrainsPackage::filterLoad).count();
    }

    @Benchmark
    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    public void filterAndMap(final Blackhole bh) {
        Stream<Integer> stream = data.stream().filter(JetbrainsPackage::filterLoad).map(it -> it * 10);
        stream.forEach(it -> bh.consume(it));
    }

    @Benchmark
    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    public Long countFiltered() {
        return data.stream().mapToLong(it -> JetbrainsPackage.filterLoad(it) ? 1 : 0).sum();
    }
}
