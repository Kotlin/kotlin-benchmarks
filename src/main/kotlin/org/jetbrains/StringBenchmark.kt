package org.jetbrains

import org.openjdk.jmh.annotations.BenchmarkMode
import org.openjdk.jmh.annotations.Mode
import org.openjdk.jmh.annotations.OutputTimeUnit
import java.util.concurrent.TimeUnit
import java.util.ArrayList
import org.openjdk.jmh.annotations.Setup
import org.openjdk.jmh.annotations.CompilerControl
import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.infra.Blackhole
import java.util.Random

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
open class StringBenchmark : SizedBenchmark() {
    var data = ArrayList<String>()
    var csv: String = ""

    var doubleData = DoubleArray(0)

    @Setup
    fun setup() {
        stringValues(size).mapTo(data) { it }

        val random = Random(123456789)

        csv = ""
        var p = false
        for (i in 1..size) {
            if (p) {
                csv += ","
            } else {
                p = true
            }
            val elem = random.nextDouble()
            csv += elem
        }

        doubleData = DoubleArray(size) { random.nextDouble() }
    }


    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    @Benchmark
    open fun stringConcat(): String? {
        var string = ""
        for (it in data) string += it
        return string
    }

    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    @Benchmark
    open fun stringConcatNullable(): String? {
        var string: String? = ""
        for (it in data) string += it
        return string
    }

    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    @Benchmark
    open fun stringBuilderConcat(): String {
        val string: StringBuilder = StringBuilder("")
        for (it in data) string.append(it)
        return string.toString()
    }

    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    @Benchmark
    open fun stringBuilderConcatNullable(): String {
        val string: StringBuilder? = StringBuilder("")
        for (it in data) string?.append(it)
        return string.toString()
    }

    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    @Benchmark
    open fun summarizeSplittedCsv(): Double {
        val fields = csv.split(",")
        var sum = 0.0
        for (field in fields) {
            sum += java.lang.Double.parseDouble(field)
        }
        return sum
    }

    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    @Benchmark
    open fun concatStringsWithDoubles(bh: Blackhole) {
        // See KT-48947
        for (i in 0 until size) {
            bh.consume(data[i] + doubleData[i])
        }
    }
}