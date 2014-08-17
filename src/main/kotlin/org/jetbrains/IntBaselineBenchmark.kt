package org.jetbrains

import org.openjdk.jmh.annotations.BenchmarkMode
import org.openjdk.jmh.annotations.Mode
import org.openjdk.jmh.annotations.OutputTimeUnit
import java.util.concurrent.TimeUnit
import org.openjdk.jmh.annotations.Benchmark
import java.util.ArrayList
import org.openjdk.jmh.infra.Blackhole

BenchmarkMode(Mode.Throughput)
OutputTimeUnit(TimeUnit.SECONDS)
open class IntBaselineBenchmark : SizedBenchmark() {

    Benchmark fun consume(bh: Blackhole) {
        for (item in 1..size) {
            bh.consume(item)
        }
    }

    Benchmark fun boxAndConsume(bh: Blackhole) {
        for (item in 1..size) {
            bh.consume(item as Integer)
        }
    }

    Benchmark fun allocateList(): List<Int> {
        val list = ArrayList<Int>(size)
        return list
    }

    Benchmark fun allocateArray(): IntArray {
        val list = IntArray(size)
        return list
    }

    Benchmark fun allocateListAndFill(): List<Int> {
        val list = ArrayList<Int>(size)
        for (item in 1..size) {
            list.add(item)
        }
        return list
    }

    Benchmark fun allocateArrayAndFill(): IntArray {
        var index = 0
        val list = IntArray(size)
        for (item in 1..size) {
            list[index++] = item
        }
        return list
    }
}