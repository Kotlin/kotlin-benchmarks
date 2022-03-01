package org.jetbrains

import org.openjdk.jmh.annotations.*
import org.openjdk.jmh.infra.Blackhole
import java.util.concurrent.TimeUnit

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
open class LoopBenchmark : SizedBenchmark() {
    lateinit var arrayList: List<Value>
    lateinit var array: Array<Value>

    @Setup
    fun setup() {
        val list = ArrayList<Value>(size)
        for (n in classValues(size))
            list.add(n)
        arrayList = list
        array = list.toTypedArray()
    }

    @Benchmark
    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    fun arrayLoop(bh: Blackhole) {
        for (x in array) {
            bh.consume(x)
        }
    }

    @Benchmark
    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    fun arrayIndexLoop(bh: Blackhole) {
        for (i in array.indices) {
            bh.consume(array[i])
        }
    }

    @Benchmark
    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    fun arrayListIndexLoop(bh: Blackhole) {
        for (i in arrayList.indices) {
            bh.consume(array[i])
        }
    }

    @Benchmark
    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    fun arrayListLoop(bh: Blackhole) {
        for (x in arrayList) {
            bh.consume(x)
        }
    }

    @Benchmark
    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    fun arrayWhileLoop(bh: Blackhole) {
        var i = 0
        val s = array.size
        while (i < s) {
            bh.consume(array[i])
            i++
        }
    }

    @Benchmark
    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    fun arrayForeachLoop(bh: Blackhole) {
        array.forEach { bh.consume(it) }
    }

    @Benchmark
    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    fun arrayListForeachLoop(bh: Blackhole) {
        arrayList.forEach { bh.consume(it) }
    }
}