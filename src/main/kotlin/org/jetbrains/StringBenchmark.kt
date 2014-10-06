package org.jetbrains

import org.openjdk.jmh.annotations.BenchmarkMode
import org.openjdk.jmh.annotations.Mode
import org.openjdk.jmh.annotations.OutputTimeUnit
import java.util.concurrent.TimeUnit
import java.util.ArrayList
import org.openjdk.jmh.annotations.Setup
import org.openjdk.jmh.annotations.CompilerControl
import org.openjdk.jmh.annotations.Benchmark

BenchmarkMode(Mode.AverageTime)
OutputTimeUnit(TimeUnit.NANOSECONDS)
open class StringBenchmark : SizedBenchmark() {
    private var _data: ArrayList<String>? = null
    val data: ArrayList<String>
        get() = _data!!

    Setup fun setup() {
        val list = ArrayList<String>(size)
        for (n in stringValues(size))
            list.add(n)
        _data = list
    }


    CompilerControl(CompilerControl.Mode.DONT_INLINE)
    Benchmark open public fun stringConcat(): String? {
        var string: String = ""
        for (it in data) string = string + it
        return string
    }

    CompilerControl(CompilerControl.Mode.DONT_INLINE)
    Benchmark open public fun stringConcatNullable(): String? {
        var string: String? = ""
        for (it in data) string = string + it
        return string
    }

    CompilerControl(CompilerControl.Mode.DONT_INLINE)
    Benchmark open public fun stringBuilderConcat(): String {
        var string : StringBuilder = StringBuilder("")
        for (it in data) string.append(it)
        return string.toString()
    }

    CompilerControl(CompilerControl.Mode.DONT_INLINE)
    Benchmark open public fun stringBuilderConcatNullable(): String {
        var string : StringBuilder? = StringBuilder("")
        for (it in data) string?.append(it)
        return string.toString()
    }
}