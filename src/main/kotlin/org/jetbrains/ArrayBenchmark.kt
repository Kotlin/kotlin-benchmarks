package org.jetbrains

import org.openjdk.jmh.annotations.*
import java.util.concurrent.*
import org.openjdk.jmh.infra.Blackhole

State(Scope.Thread)
BenchmarkMode(Mode.AverageTime)
OutputTimeUnit(TimeUnit.MICROSECONDS)
Warmup(iterations = 5)
Measurement(iterations = 5)
Fork(2)
open class ArrayBenchmark {
    val data: IntArray
    {
        data = IntArray(N)
        var index = 0
        for (n in values())
            data[index++] = n
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
