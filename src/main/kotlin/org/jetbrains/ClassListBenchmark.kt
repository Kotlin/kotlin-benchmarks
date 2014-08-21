package org.jetbrains

import org.openjdk.jmh.annotations.*
import java.util.concurrent.*
import java.util.ArrayList

BenchmarkMode(Mode.AverageTime)
OutputTimeUnit(TimeUnit.NANOSECONDS)
open class ClassListBenchmark : SizedBenchmark() {
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
    Benchmark fun copy(): List<Value> {
        return data.toList()
    }

    CompilerControl(CompilerControl.Mode.DONT_INLINE)
    Benchmark fun copyManual(): List<Value> {
        val list = ArrayList<Value>(data.size)
        for (item in data) {
            list.add(item)
        }
        return list
    }

    CompilerControl(CompilerControl.Mode.DONT_INLINE)
    Benchmark fun filterAndCount(): Int {
        return data.filter { it.value and 1 == 0 }.count()
    }

    CompilerControl(CompilerControl.Mode.DONT_INLINE)
    Benchmark fun filterAndCountWithValue(): Int {
        val value = data.filter { it.value and 1 == 0 }.count()
        return value
    }

    CompilerControl(CompilerControl.Mode.DONT_INLINE)
    Benchmark fun filterAndMap(): List<Int> {
        return data.filter { it.value and 1 == 0 }.map { it.value }
    }

    CompilerControl(CompilerControl.Mode.DONT_INLINE)
    Benchmark fun filterAndMapManual(): ArrayList<Int> {
        val list = ArrayList<Int>()
        for (item in data) {
            if (item.value and 1 == 0) {
                val value = item.hashCode()
                list.add(value)
            }
        }
        return list
    }

    CompilerControl(CompilerControl.Mode.DONT_INLINE)
    Benchmark fun filter(): List<Value> {
        return data.filter { it.value and 1 == 0 }
    }

    CompilerControl(CompilerControl.Mode.DONT_INLINE)
    Benchmark fun filterManual(): List<Value> {
        val list = ArrayList<Value>()
        for (item in data) {
            if (item.value and 1 == 0)
                list.add(item)
        }
        return list
    }

    CompilerControl(CompilerControl.Mode.DONT_INLINE)
    Benchmark fun countFilteredManual(): Int {
        var count = 0
        for (it in data) {
            if (it.value and 1 == 0)
                count++
        }
        return count
    }

    CompilerControl(CompilerControl.Mode.DONT_INLINE)
    Benchmark fun countFiltered(): Int {
        return data.count { it.value and 1 == 0 }
    }

    CompilerControl(CompilerControl.Mode.DONT_INLINE)
    Benchmark fun countFilteredWithValue(): Int {
        val value = data.count { it.value and 1 == 0 }
        return value
    }

    CompilerControl(CompilerControl.Mode.DONT_INLINE)
    Benchmark fun countFilteredLocal(): Int {
        return data.cnt { it.value and 1 == 0 }
    }

    CompilerControl(CompilerControl.Mode.DONT_INLINE)
    Benchmark fun countFilteredLocalWithValue(): Int {
        val local = data.cnt { it.value and 1 == 0 }
        return local
    }

    CompilerControl(CompilerControl.Mode.DONT_INLINE)
    Benchmark fun reduce(): Int {
        return data.fold(0) {(acc, it) -> if (it.value and 1 == 0) acc + 1 else acc }
    }
}
