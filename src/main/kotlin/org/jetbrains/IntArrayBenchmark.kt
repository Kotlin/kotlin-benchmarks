package org.jetbrains

import org.openjdk.jmh.annotations.*
import java.util.concurrent.*
import java.util.ArrayList
import org.openjdk.jmh.infra.Blackhole

State(Scope.Thread)
BenchmarkMode(Mode.AverageTime)
OutputTimeUnit(TimeUnit.NANOSECONDS)
open class IntArrayBenchmark : SizedBenchmark() {
    private var _data: IntArray? = null
    val data: IntArray
        get() = _data!!

    Setup fun setup() {
        val list = IntArray(size)
        var index = 0
        for (n in intValues(size))
            list[index++] = n
        _data = list
    }

    CompilerControl(CompilerControl.Mode.DONT_INLINE)
    Benchmark fun copy(): List<Int> {
        return data.toList()
    }

    CompilerControl(CompilerControl.Mode.DONT_INLINE)
    Benchmark fun copyManual(): List<Int> {
        val list = ArrayList<Int>(data.size)
        for (item in data) {
            list.add(item)
        }
        return list
    }

    CompilerControl(CompilerControl.Mode.DONT_INLINE)
    Benchmark fun filterAndCount(): Int {
        return data.filter { it and 1 == 0 }.count()
    }

    CompilerControl(CompilerControl.Mode.DONT_INLINE)
    Benchmark fun filterAndCountWithValue(): Int {
        val value = data.filter { it and 1 == 0 }.count()
        return value
    }

    CompilerControl(CompilerControl.Mode.DONT_INLINE)
    Benchmark fun filterAndMap(): List<Int> {
        return data.filter { it and 1 == 0 }.map { it * 10 }
    }

    CompilerControl(CompilerControl.Mode.DONT_INLINE)
    Benchmark fun filterAndMapManual(): ArrayList<Int> {
        val list = ArrayList<Int>()
        for (item in data) {
            if (item and 1 == 0) {
                val value = item * 10
                list.add(value)
            }
        }
        return list
    }

    CompilerControl(CompilerControl.Mode.DONT_INLINE)
    Benchmark fun filter(): List<Int> {
        return data.filter { it and 1 == 0 }
    }

    CompilerControl(CompilerControl.Mode.DONT_INLINE)
    Benchmark fun filterManual(): List<Int> {
        val list = ArrayList<Int>()
        for (item in data) {
            if (item and 1 == 0)
                list.add(item)
        }
        return list
    }

    CompilerControl(CompilerControl.Mode.DONT_INLINE)
    Benchmark fun countFilteredManual(): Int {
        var count = 0
        for (it in data) {
            if (it and 1 == 0)
                count++
        }
        return count
    }

    CompilerControl(CompilerControl.Mode.DONT_INLINE)
    Benchmark fun countFiltered(): Int {
        return data.count { it and 1 == 0 }
    }

    CompilerControl(CompilerControl.Mode.DONT_INLINE)
    Benchmark fun countFilteredOSR(bh : Blackhole) {
        bh.consume(data.count { it and 1 == 0 })
    }

    CompilerControl(CompilerControl.Mode.DONT_INLINE)
    Benchmark fun countFilteredOSRValue(bh : Blackhole) {
        val value = data.count { it and 1 == 0 }
        bh.consume(value)
    }

    CompilerControl(CompilerControl.Mode.DONT_INLINE)
    Benchmark fun countFilteredWithValue(): Int {
        val value = data.count { it and 1 == 0 }
        return value
    }

    CompilerControl(CompilerControl.Mode.DONT_INLINE)
    Benchmark fun countFilteredLocal(): Int {
        return data.cnt { it and 1 == 0 }
    }

    CompilerControl(CompilerControl.Mode.DONT_INLINE)
    Benchmark fun countFilteredLocalWithValue(): Int {
        val local = data.cnt { it and 1 == 0 }
        return local
    }

    CompilerControl(CompilerControl.Mode.DONT_INLINE)
    Benchmark fun reduce(): Int {
        return data.fold(0) {(acc, it) -> if (it and 1 == 0) acc + 1 else acc }
    }
}

