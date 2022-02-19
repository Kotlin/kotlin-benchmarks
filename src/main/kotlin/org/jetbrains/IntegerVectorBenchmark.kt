package org.jetbrains

import org.openjdk.jmh.annotations.*
import org.openjdk.jmh.infra.Blackhole
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.max

@ExperimentalUnsignedTypes
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@CompilerControl(CompilerControl.Mode.DONT_INLINE)
open class IntegerVectorBenchmark : VectorSizedBenchmark() {
    // This benchmarks mostly checks that UInt operations don't get in the way of loop optimizations.

    private var iA = IntArray(0)
    private var iB = IntArray(0)
    private var uA = UIntArray(0)
    private var uB = UIntArray(0)

    @Setup
    fun setup() {
        val n = vectorSize
        val random = Random()
        iA = IntArray(n) { random.nextInt() }
        iB = IntArray(n) { random.nextInt() }
        uA = UIntArray(n) { random.nextInt().toUInt() }
        uB = UIntArray(n) { random.nextInt().toUInt() }
    }

    @Benchmark
    fun intVectorAdd(bh: Blackhole) {
        val iX = IntArray(iA.size)
        for (i in iA.indices) {
            iX[i] = iA[i] + iB[i]
        }
        bh.consume(iX)
    }

    @Benchmark
    fun intVectorSumReduction(bh: Blackhole) {
        var x = 0
        for (i in iA.indices) {
            x += iA[i]
        }
        bh.consume(x)
    }

    @Benchmark
    fun intVectorMaxReduction(bh: Blackhole) {
        var x = Int.MIN_VALUE
        for (i in iA.indices) {
            x = max(x, iA[i])
        }
        bh.consume(x)
    }

    @Benchmark
    fun uintVectorAdd(bh: Blackhole) {
        val uX = UIntArray(uA.size)
        for (i in uA.indices) {
            uX[i] = uA[i] + uB[i]
        }
        bh.consume(uX)
    }

    @Benchmark
    fun uintVectorSumReduction(bh: Blackhole) {
        var ux = 0U
        for (i in uA.indices) {
            ux += uA[i]
        }
        bh.consume(ux)
    }

    @Benchmark
    fun uintVectorMaxReduction(bh: Blackhole) {
        var ux = UInt.MIN_VALUE
        for (i in uA.indices) {
            ux = max(ux, uA[i])
        }
        bh.consume(ux)
    }
}