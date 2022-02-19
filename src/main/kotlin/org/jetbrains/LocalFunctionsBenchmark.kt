package org.jetbrains

import org.openjdk.jmh.annotations.*
import org.openjdk.jmh.infra.Blackhole
import java.util.*
import java.util.concurrent.TimeUnit


@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
open class LocalFunctionsBenchmark : SizedBenchmark() {
    private var doubleData = DoubleArray(0)
    private var stringData = Array(0) { "" }

    @Setup
    fun setup() {
        val random = Random()
        doubleData = DoubleArray(size) { random.nextDouble() }
        stringData = Array(size) { doubleData[it].toString() }
    }

    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    @Benchmark
    fun localFunctionWithEmptyCapture(bh: Blackhole) {
        for (i in 0 until size) {
            fun localFun(x: Double) = x * x
            bh.consume(localFun(doubleData[i]))
        }
    }

    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    @Benchmark
    fun localFunctionWithCapturedVal(bh: Blackhole) {
        for (i in 0 until size) {
            val y = doubleData[i]
            fun localFun(x: Double) = x * y
            bh.consume(localFun(doubleData[i]))
        }
    }

    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    @Benchmark
    fun localFunctionReadingCapturedVar(bh: Blackhole) {
        for (i in 0 until size) {
            var y = doubleData[i]
            fun localFun(x: Double) = x * y
            bh.consume(localFun(doubleData[i]))
        }
    }

    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    @Benchmark
    fun localFunctionWritingCapturedVar(bh: Blackhole) {
        var z = 0.0
        for (i in 0 until size) {
            fun localFun(x: Double): Double {
                z += x
                return x * x
            }
            bh.consume(localFun(doubleData[i]))
        }
        bh.consume(z)
    }
}

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
open class LocalFunctionsSmallBenchmark : SmallSizedBenchmark() {
    private var doubleData = DoubleArray(0)
    private var stringData = Array(0) { "" }

    @Setup
    fun setup() {
        val random = Random()
        doubleData = DoubleArray(smallSize) { random.nextDouble() }
        stringData = Array(smallSize) { doubleData[it].toString() }
    }

    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    @Benchmark
    fun localFunctionUpdatingStringBuilder(bh: Blackhole) {
        val sb = StringBuilder()
        fun add(s: String) {
            sb.append(s)
        }
        for (i in 0 until smallSize) {
            add(stringData[i])
        }
        bh.consume(sb.toString())
    }

    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    @Benchmark
    fun localFunctionUpdatingStringVar(bh: Blackhole) {
        var string = ""
        fun add(s: String) {
            string += s
        }
        for (i in 0 until smallSize) {
            add(stringData[i])
        }
        bh.consume(string)
    }
}