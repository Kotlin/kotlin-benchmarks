package org.jetbrains

import org.openjdk.jmh.annotations.*
import java.util.concurrent.*
import java.util.ArrayList
import org.openjdk.jmh.infra.Blackhole


BenchmarkMode(Mode.AverageTime)
OutputTimeUnit(TimeUnit.NANOSECONDS)
open class WithIndiciesBenchmark : SizedBenchmark() {
    private var _data: ArrayList<Value>? = null
    val data: ArrayList<Value>
        get() = _data!!

    Setup fun setup() {
        val list = ArrayList<Value>(size)
        for (n in classValues(size))
            list.add(n)
        _data = list
    }

    CompilerControl(CompilerControl.Mode.DONT_INLINE)
    Benchmark fun withIndicies(bh: Blackhole) {
        for ((index, value) in data.withIndex()) {
            if (filterLoad(value)) {
                bh.consume(index)
                bh.consume(value)
            }
        }
    }

    CompilerControl(CompilerControl.Mode.DONT_INLINE)
    Benchmark fun withIndiciesManual(bh: Blackhole) {
        var index = 0
        for (value in data) {
            if (filterLoad(value)) {
                bh.consume(index)
                bh.consume(value)
            }
            index++
        }
    }
}
