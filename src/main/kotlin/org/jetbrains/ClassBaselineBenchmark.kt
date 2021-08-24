package org.jetbrains

import org.openjdk.jmh.annotations.*
import org.openjdk.jmh.infra.Blackhole
import java.util.*
import java.util.concurrent.TimeUnit

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
open class ClassBaselineBenchmark : SizedBenchmark() {

    class LocalValue(var value: Int)

    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    @Benchmark fun consume(bh: Blackhole) {
        for (item in 0 until size) {
            bh.consume(LocalValue(item))
        }
    }

    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    @Benchmark fun consumeField(bh: Blackhole) {
        val value = LocalValue(0)
        for (item in 0 until size) {
            value.value = item
            bh.consume(value)
        }
    }

    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    @Benchmark fun allocateList(): List<LocalValue> {
        return ArrayList(size)
    }

    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    @Benchmark fun allocateArray(): Array<LocalValue?> {
        return arrayOfNulls(size)
    }

    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    @Benchmark fun allocateListAndFill(): List<LocalValue> {
        val list = ArrayList<LocalValue>(size)
        for (item in 0 until size) {
            list.add(LocalValue(item))
        }
        return list
    }

    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    @Benchmark fun allocateListAndWrite(): List<LocalValue> {
        val value = LocalValue(0)
        val list = ArrayList<LocalValue>(size)
        for (item in 0 until size) {
            list.add(value)
        }
        return list
    }

    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    @Benchmark fun allocateArrayAndFill(): Array<LocalValue?> {
        val list = arrayOfNulls<LocalValue>(size)
        var index = 0
        for (item in 0 until size) {
            list[index++] = LocalValue(item)
        }
        return list
    }

    @Benchmark
    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    fun allocateArrayAndWrite(): Array<Value?> {
        val value = Value(0)
        val array = arrayOfNulls<Value>(size)
        var index = 0
        for (item in 0 until size) {
            array[index++] = value
        }
        return array
    }
}