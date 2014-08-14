package org.jetbrains

import org.openjdk.jmh.annotations.*
import java.util.concurrent.*
import org.openjdk.jmh.infra.Blackhole
import java.util.ArrayList

State(Scope.Thread)
BenchmarkMode(Mode.Throughput)
OutputTimeUnit(TimeUnit.SECONDS)
open class StringArrayBenchmark {
    var data: Array<String>
    {
        val list = ArrayList<String>(SIZE)
        for (n in stringValues())
            list.add(n)
        data = list.copyToArray()
    }

    Benchmark fun filterAndCount(bh: Blackhole) {
        bh.consume(data.filter { it.length and 1 == 0 }.count())
    }

    Benchmark fun filterAndCountWithValue(bh: Blackhole) {
        val value = data.filter { it.length and 1 == 0 }.count()
        bh.consume(value)
    }

    Benchmark fun filterAndMap(bh: Blackhole) {
        for (item in data.filter { it.length and 1 == 0 }.map { it + "x" })
            bh.consume(item)
    }

    Benchmark fun filter(bh: Blackhole) {
        for (item in data.filter { it.length and 1 == 0 })
            bh.consume(item)
    }

    Benchmark fun countFilteredManual(bh: Blackhole) {
        var count = 0
        for (it in data) {
            if (it.length and 1 == 0)
                count++
        }
        bh.consume(count)
    }

    Benchmark fun countFiltered(bh: Blackhole) {
        bh.consume(data.count { it.length and 1 == 0 })
    }

    Benchmark fun countFilteredWithValue(bh: Blackhole) {
        val value = data.count { it.length and 1 == 0 }
        bh.consume(value)
    }

    Benchmark fun countFilteredLocal(bh: Blackhole) {
        bh.consume(data.cnt { it.length and 1 == 0 })
    }

    Benchmark fun countFilteredLocalWithValue(bh: Blackhole) {
        val local = data.cnt { it.length and 1 == 0 }
        bh.consume(local)
    }
}

