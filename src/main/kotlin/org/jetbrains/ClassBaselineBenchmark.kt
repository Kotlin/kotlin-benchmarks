package org.jetbrains

import org.openjdk.jmh.annotations.BenchmarkMode
import org.openjdk.jmh.annotations.Mode
import org.openjdk.jmh.annotations.OutputTimeUnit
import java.util.concurrent.TimeUnit
import org.openjdk.jmh.annotations.Benchmark
import java.util.ArrayList
import org.openjdk.jmh.infra.Blackhole
import org.openjdk.jmh.annotations.CompilerControl

BenchmarkMode(Mode.AverageTime)
OutputTimeUnit(TimeUnit.NANOSECONDS)
open class ClassBaselineBenchmark : SizedBenchmark() {

    CompilerControl(CompilerControl.Mode.DONT_INLINE)
    Benchmark fun consume(bh: Blackhole) {
        for (item in 1..size) {
            bh.consume(Value(item))
        }
    }

    CompilerControl(CompilerControl.Mode.DONT_INLINE)
    Benchmark fun consumeField(bh: Blackhole) {
        val value = Value(0)
        for (item in 1..size) {
            value.value = item
            bh.consume(value)
        }
    }

    CompilerControl(CompilerControl.Mode.DONT_INLINE)
    Benchmark fun allocateList(): List<Value> {
        val list = ArrayList<Value>(size)
        return list
    }

    CompilerControl(CompilerControl.Mode.DONT_INLINE)
    Benchmark fun allocateArray(): Array<Value?> {
        val list = arrayOfNulls<Value>(size)
        return list
    }

    CompilerControl(CompilerControl.Mode.DONT_INLINE)
    Benchmark fun allocateListAndFill(): List<Value> {
        val list = ArrayList<Value>(size)
        for (item in 1..size) {
            list.add(Value(item))
        }
        return list
    }

    CompilerControl(CompilerControl.Mode.DONT_INLINE)
    Benchmark fun allocateListAndWrite(): List<Value> {
        val value = Value(0)
        val list = ArrayList<Value>(size)
        for (item in 1..size) {
            list.add(value)
        }
        return list
    }

    CompilerControl(CompilerControl.Mode.DONT_INLINE)
    Benchmark fun allocateArrayAndFill(): Array<Value?> {
        val list = arrayOfNulls<Value>(size)
        var index = 0
        for (item in 1..size) {
            list[index++] = Value(item)
        }
        return list
    }
}