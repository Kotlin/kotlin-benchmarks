package org.jetbrains

import org.openjdk.jmh.annotations.*
import java.util.concurrent.*
import java.util.ArrayList

BenchmarkMode(Mode.AverageTime)
OutputTimeUnit(TimeUnit.NANOSECONDS)
open class ClassArrayBenchmark : SizedBenchmark() {
    private var _data: Array<Value>? = null
    val data: Array<Value>
        get() = _data!!

    Setup fun setup() {
        val list = ArrayList<Value>(size)
        for (n in classValues(size))
            list.add(n)
        _data = list.toTypedArray()
    }

    CompilerControl(CompilerControl.Mode.DONT_INLINE)
    Benchmark fun copy(): List<Value> {
        return data.toList()
    }

    CompilerControl(CompilerControl.Mode.DONT_INLINE)
    Benchmark fun copyManual(): List<Value> {
        val list = ArrayList<Value>(data.size())
        for (item in data) {
            list.add(item)
        }
        return list
    }

    CompilerControl(CompilerControl.Mode.DONT_INLINE)
    Benchmark fun filterAndCount(): Int {
        return data.filter { filterLoad(it) }.count()
    }

    CompilerControl(CompilerControl.Mode.DONT_INLINE)
    Benchmark fun filterAndMap(): List<String> {
        return data.filter { filterLoad(it) }.map { mapLoad(it) }
    }

    CompilerControl(CompilerControl.Mode.DONT_INLINE)
    Benchmark fun filterAndMapManual(): ArrayList<String> {
        val list = ArrayList<String>()
        for (it in data) {
            if (filterLoad(it)) {
                val value = mapLoad(it)
                list.add(value)
            }
        }
        return list
    }

    CompilerControl(CompilerControl.Mode.DONT_INLINE)
    Benchmark fun filter(): List<Value> {
        return data.filter { filterLoad(it) }
    }

    CompilerControl(CompilerControl.Mode.DONT_INLINE)
    Benchmark fun filterManual(): List<Value> {
        val list = ArrayList<Value>()
        for (it in data) {
            if (filterLoad(it))
                list.add(it)
        }
        return list
    }

    CompilerControl(CompilerControl.Mode.DONT_INLINE)
    Benchmark fun countFilteredManual(): Int {
        var count = 0
        for (it in data) {
            if (filterLoad(it))
                count++
        }
        return count
    }

    CompilerControl(CompilerControl.Mode.DONT_INLINE)
    Benchmark fun countFiltered(): Int {
        return data.count { filterLoad(it) }
    }

    CompilerControl(CompilerControl.Mode.DONT_INLINE)
    Benchmark fun countFilteredLocal(): Int {
        return data.cnt { filterLoad(it) }
    }

    CompilerControl(CompilerControl.Mode.DONT_INLINE)
    Benchmark fun reduce(): Int {
        return data.fold(0) { acc, it -> if (filterLoad(it)) acc + 1 else acc }
    }
}

