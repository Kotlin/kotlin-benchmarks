package org.jetbrains;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

@State(Scope.Thread)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
public class StringJavaStreamBenchmark {
    public static <E> ArrayList<E> makeList(Iterable<E> iter) {
        ArrayList<E> list = new ArrayList<E>();
        for (E item : iter) {
            list.add(item);
        }
        return list;
    }

    public ArrayList<String> data = makeList(JetbrainsPackage.stringValues());

    @Benchmark
    public Long filterAndCount() {
        return data.stream().filter(it -> it.length() % 2 == 0).count();
    }

    @Benchmark
    public void filterAndMap(final Blackhole bh) {
        Stream<String> stream = data.stream().filter(it -> it.length() % 2 == 0).map(it -> it + "x");
        stream.forEach(it -> bh.consume(it));
    }

    @Benchmark
    public Long countFiltered() {
        return data.stream().mapToLong(it -> it.length() % 2 == 0 ? 1 : 0).sum();
    }
}
