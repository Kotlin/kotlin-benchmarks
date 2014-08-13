package org.jetbrains

import org.openjdk.jmh.annotations.*
import java.util.concurrent.*
import org.openjdk.jmh.infra.Blackhole

State(Scope.Thread)
BenchmarkMode(Mode.Throughput)
OutputTimeUnit(TimeUnit.SECONDS)
open class StringStreamBenchmark {
    val data: Stream<String>
    {
        data = stringValues().stream()
    }

    Benchmark fun filterAndCount(bh: Blackhole) {
        bh.consume(data.filter { it.length % 2 == 0 }.count())
    }

    Benchmark fun filterAndMap(bh: Blackhole) {
        for (item in data.filter { it.length % 2 == 0 }.map { it + "x" })
            bh.consume(item)
    }

    Benchmark fun countFiltered(bh: Blackhole) {
        bh.consume(data.count { it.length % 2 == 0 })
    }
}