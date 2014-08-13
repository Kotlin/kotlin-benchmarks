package org.jetbrains

import org.openjdk.jmh.annotations.*
import java.util.concurrent.*
import java.util.ArrayList
import org.openjdk.jmh.infra.Blackhole

State(Scope.Thread)
BenchmarkMode(Mode.Throughput)
OutputTimeUnit(TimeUnit.SECONDS)
open class IntListBenchmark {
    val data: ArrayList<Int>
    {
        data = ArrayList(SIZE)
        for (n in intValues())
            data.add(n)
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

    Benchmark fun countFilteredLocal(bh: Blackhole) {
        bh.consume(data.cnt { it % 2 == 0 })
    }

    Benchmark fun countFilteredWithValue(bh: Blackhole) {
        val value = data.count { it % 2 == 0 }
        bh.consume(value)
    }

    Benchmark fun countFilteredLocalWithValue(bh: Blackhole) {
        val value = data.cnt { it % 2 == 0 }
        bh.consume(value)
    }

}