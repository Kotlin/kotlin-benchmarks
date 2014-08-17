package org.jetbrains

import org.openjdk.jmh.annotations.*
import java.util.concurrent.*
import java.util.ArrayList

BenchmarkMode(Mode.AverageTime)
OutputTimeUnit(TimeUnit.NANOSECONDS)
open class IntListBenchmark : SizedBenchmark() {
    private var _data: List<Int>? = null
    val data: List<Int>
        get() = _data!!

    Setup fun setup() {
        val list = ArrayList<Int>(size)
        for (n in intValues(size))
            list.add(n)
        _data = list
    }

    Benchmark fun copy(): List<Int> {
        return data.toList()
    }

    Benchmark fun copyManual(): List<Int> {
        val list = ArrayList<Int>(data.size)
        for (item in data) {
            list.add(item)
        }
        return list
    }


    Benchmark fun filterAndCount(): Int {
        return data.filter { it and 1 == 0 }.count()
    }

    Benchmark fun filterAndCountWithValue(): Int {
        val value = data.filter { it and 1 == 0 }.count()
        return value
    }

    Benchmark fun filterAndMap(): List<Int> {
        return data.filter { it and 1 == 0 }.map { it * 10 }
    }

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

    Benchmark fun filter(): List<Int> {
        return data.filter { it and 1 == 0 }
    }

    Benchmark fun filterManual(): List<Int> {
        val list = ArrayList<Int>()
        for (item in data) {
            if (item and 1 == 0)
                list.add(item)
        }
        return list
    }

    Benchmark fun countFilteredManual(): Int {
        var count = 0
        for (it in data) {
            if (it and 1 == 0)
                count++
        }
        return count
    }

    Benchmark fun countFiltered(): Int {
        return data.count { it and 1 == 0 }
    }

    Benchmark fun countFilteredWithValue(): Int {
        val value = data.count { it and 1 == 0 }
        return value
    }

    Benchmark fun countFilteredLocal(): Int {
        return data.cnt { it and 1 == 0 }
    }

    Benchmark fun countFilteredLocalWithValue(): Int {
        val local = data.cnt { it and 1 == 0 }
        return local
    }

    Benchmark fun reduce(): Int {
        return data.fold(0) {(acc, it) -> if (it and 1 == 0) acc + 1 else acc }
    }
}
