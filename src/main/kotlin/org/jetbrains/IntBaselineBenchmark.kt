package org.jetbrains

import org.openjdk.jmh.annotations.BenchmarkMode
import org.openjdk.jmh.annotations.Mode
import org.openjdk.jmh.annotations.OutputTimeUnit
import java.util.concurrent.TimeUnit
import org.openjdk.jmh.annotations.Benchmark
import java.util.ArrayList
import org.openjdk.jmh.infra.Blackhole
import org.openjdk.jmh.annotations.CompilerControl

BenchmarkMode(Mode.Throughput)
OutputTimeUnit(TimeUnit.SECONDS)
open class IntBaselineBenchmark : SizedBenchmark() {

    CompilerControl(CompilerControl.Mode.DONT_INLINE)
    Benchmark fun consume(bh: Blackhole) {
        for (item in 1..size) {
            bh.consume(item)
        }
    }

    CompilerControl(CompilerControl.Mode.DONT_INLINE)
    Benchmark fun boxAndConsume(bh: Blackhole) {
        for (item in 1..size) {
            bh.consume(item as Integer)
        }
    }

    CompilerControl(CompilerControl.Mode.DONT_INLINE)
    Benchmark fun allocateList(): List<Int> {
        val list = ArrayList<Int>(size)
        return list
    }

    CompilerControl(CompilerControl.Mode.DONT_INLINE)
    Benchmark fun allocateArray(): IntArray {
        val list = IntArray(size)
        return list
    }

    CompilerControl(CompilerControl.Mode.DONT_INLINE)
    Benchmark fun allocateListAndFill(): List<Int> {
        val list = ArrayList<Int>(size)
        for (item in 1..size) {
            list.add(item)
        }
        return list
    }

    CompilerControl(CompilerControl.Mode.DONT_INLINE)
    Benchmark fun allocateArrayAndFill(): IntArray {
        var index = 0
        val list = IntArray(size)
        for (item in 1..size) {
            list[index++] = item
        }
        return list
    }
}