package org.jetbrains

import org.openjdk.jmh.annotations.*
import java.util.concurrent.*
import java.util.ArrayList
import org.openjdk.jmh.infra.Blackhole

State(Scope.Thread)
BenchmarkMode(Mode.Throughput)
OutputTimeUnit(TimeUnit.SECONDS)
open class IntListBenchmark {
    var data: ArrayList<Int>
    {
        data = ArrayList(SIZE)
        for (n in intValues())
            data.add(n)
    }

    Benchmark fun filterAndCount(): Int {
        return data.filter { it and 1 == 0 }.count()
    }

    Benchmark fun filterAndCountWithValue(): Int {
        val value = data.filter { it and 1 == 0 }.count()
        return value
    }

    Benchmark fun filterAndMap(bh: Blackhole) {
        for (item in data.filter { it and 1 == 0 }.map { it * 10 })
            bh.consume(item)
    }

    Benchmark fun filter(bh: Blackhole) {
        for (item in data.filter { it and 1 == 0 })
            bh.consume(item)
    }

    Benchmark fun countFilteredManual(): Int {
        var count = 0
        for (it in data) {
            if (it and 1 == 0)
                count++
        }
        return count
    }

    Benchmark fun countFiltered(): Int {
        return data.count { it and 1 == 0 }
    }

    Benchmark fun countFilteredWithValue(): Int {
        val value = data.count { it and 1 == 0 }
        return value
    }

    Benchmark fun countFilteredLocal(): Int {
        return data.cnt { it and 1 == 0 }
    }

    Benchmark fun countFilteredLocalWithValue(): Int {
        val local = data.cnt { it and 1 == 0 }
        return local
    }

    Benchmark fun reduce(): Int {
        return data.reduce {(acc, value) -> acc xor value }
    }
}
