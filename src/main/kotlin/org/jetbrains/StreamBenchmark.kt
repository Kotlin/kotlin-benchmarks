package org.jetbrains

import org.openjdk.jmh.annotations.*
import java.util.concurrent.*
import org.openjdk.jmh.infra.Blackhole

State(Scope.Thread)
BenchmarkMode(Mode.Throughput)
OutputTimeUnit(TimeUnit.SECONDS)
Warmup(iterations = 5)
Measurement(iterations = 5)
Fork(2)
open class StreamBenchmark {
    val data: Stream<Int>
    {
        data = values().stream()
    }

    Benchmark fun filterAndCount(bh: Blackhole) {
        bh.consume(data.filter { it % 2 == 0 }.count())
    }

    Benchmark fun filterAndMap(bh: Blackhole) {
        for (item in data.filter { it % 2 == 0 }.map { it * 10 })
            bh.consume(item)
    }

    Benchmark fun countFiltered(bh: Blackhole) {
        bh.consume(data.count { it % 2 == 0 })
    }
}