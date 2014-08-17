package org.jetbrains

import org.openjdk.jmh.annotations.*
import java.util.concurrent.*
import java.util.ArrayList
import org.openjdk.jmh.infra.Blackhole

BenchmarkMode(Mode.AverageTime)
OutputTimeUnit(TimeUnit.NANOSECONDS)
open class ClassStreamBenchmark : SizedBenchmark() {
    private var _data: Stream<Value>? = null
    val data: Stream<Value>
        get() = _data!!

    Setup fun setup() {
        _data = classValues(size).stream()
    }

    Benchmark fun copy(): List<Value> {
        return data.toList()
    }

    Benchmark fun copyManual(): List<Value> {
        val list = ArrayList<Value>()
        for (item in data) {
            list.add(item)
        }
        return list
    }

    Benchmark fun filterAndCount(): Int {
        return data.filter { it.value and 1 == 0 }.count()
    }

    Benchmark fun filterAndCountWithValue(): Int {
        val value = data.filter { it.value and 1 == 0 }.count()
        return value
    }

    Benchmark fun filterAndMap(bh: Blackhole) {
        for (item in data.filter { it.value and 1 == 0 }.map { it.value })
            bh.consume(item)
    }

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

    Benchmark fun filter(bh: Blackhole) {
        for (item in data.filter { it.value and 1 == 0 })
            bh.consume(item)
    }

    Benchmark fun filterManual(): List<Value> {
        val list = ArrayList<Value>()
        for (item in data) {
            if (item.value and 1 == 0)
                list.add(item)
        }
        return list
    }

    Benchmark fun countFilteredManual(): Int {
        var count = 0
        for (it in data) {
            if (it.value and 1 == 0)
                count++
        }
        return count
    }

    Benchmark fun countFiltered(): Int {
        return data.count { it.value and 1 == 0 }
    }

    Benchmark fun countFilteredWithValue(): Int {
        val value = data.count { it.value and 1 == 0 }
        return value
    }

    Benchmark fun countFilteredLocal(): Int {
        return data.cnt { it.value and 1 == 0 }
    }

    Benchmark fun countFilteredLocalWithValue(): Int {
        val local = data.cnt { it.value and 1 == 0 }
        return local
    }

    Benchmark fun reduce(): Int {
        return data.fold(0) {(acc, it) -> if (it.value and 1 == 0) acc + 1 else acc }
    }
}