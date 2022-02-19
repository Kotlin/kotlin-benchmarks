package org.jetbrains

import org.openjdk.jmh.annotations.*
import org.openjdk.jmh.infra.Blackhole
import java.util.*
import java.util.concurrent.TimeUnit

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
open class StringBenchmark : SmallSizedBenchmark() {
    var data = ArrayList<String>()
    var csv: String = ""

    var doubleData = DoubleArray(0)

    @Setup
    fun setup() {
        stringValues(smallSize).mapTo(data) { it }

        val random = Random(123456789)

        csv = ""
        var p = false
        for (i in 1..smallSize) {
            if (p) {
                csv += ","
            } else {
                p = true
            }
            val elem = random.nextDouble()
            csv += elem
        }

        doubleData = DoubleArray(smallSize) { random.nextDouble() }
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
        for (i in 0 until smallSize) {
            bh.consume(data[i] + doubleData[i])
        }
    }
}