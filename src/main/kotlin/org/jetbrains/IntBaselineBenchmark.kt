package org.jetbrains

import org.openjdk.jmh.annotations.BenchmarkMode
import org.openjdk.jmh.annotations.Mode
import org.openjdk.jmh.annotations.OutputTimeUnit
import java.util.concurrent.TimeUnit
import org.openjdk.jmh.annotations.Benchmark
import java.util.ArrayList
import org.openjdk.jmh.infra.Blackhole
import org.openjdk.jmh.annotations.CompilerControl

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
open class IntBaselineBenchmark : SizedBenchmark() {

    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    @Benchmark
    fun consume(bh: Blackhole) {
        for (item in 1..size) {
            bh.consume(item)
        }
    }

    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    @Benchmark
    fun boxAndConsume(bh: Blackhole) {
        for (item in 1..size) {
            bh.consume(item as Any)
        }
    }

    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    @Benchmark
    fun allocateList(): List<Int> {
        return ArrayList(size)
    }

    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    @Benchmark
    fun allocateArray(): IntArray {
        return IntArray(size)
    }

    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    @Benchmark
    fun allocateListAndFill(): List<Int> {
        val list = ArrayList<Int>(size)
        for (item in 1..size) {
            list.add(item)
        }
        return list
    }

    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    @Benchmark
    fun allocateArrayAndFill(): IntArray {
        var index = 0
        val list = IntArray(size)
        for (item in 1..size) {
            list[index++] = item
        }
        return list
    }
}