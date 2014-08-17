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
        _data = list.copyToArray()
    }

    Benchmark fun copy(): List<Value> {
        return data.toList()
    }

    Benchmark fun copyManual(): List<Value> {
        val list = ArrayList<Value>(data.size)
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

    Benchmark fun filterAndMap(): List<Int> {
        return data.filter { it.value and 1 == 0 }.map { it.value }
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

    Benchmark fun filter(): List<Value> {
        return data.filter { it.value and 1 == 0 }
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

