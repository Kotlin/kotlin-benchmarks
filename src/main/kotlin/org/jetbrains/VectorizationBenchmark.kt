package org.jetbrains

import org.openjdk.jmh.annotations.*
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.max

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@CompilerControl(CompilerControl.Mode.DONT_INLINE)
open class VectorizationBenchmark : VectorSizedBenchmark() {
    private var vA = DoubleArray(0)
    private var vB = DoubleArray(0)
    private var vI = IntArray(0)
    private var vP = BooleanArray(0)

    @Setup
    open fun setup() {
        val n = size
        val random = Random()
        vA = DoubleArray(n)
        vB = DoubleArray(n)
        vI = IntArray(n)
        vP = BooleanArray(n)
        for (i in 0 until n) {
            vA[i] = random.nextDouble()
            vB[i] = random.nextDouble()
            vI[i] = i
            vP[i] = i % 2 == 0
        }
    }

    @Benchmark
    fun vectorAdd(): DoubleArray {
        val n = size
        val vX = DoubleArray(n)
        for (i in 0 until n) {
            vX[i] = vA[i] + vB[i]
        }
        return vX
    }

    @Benchmark
    open fun vectorAddGather(): DoubleArray? {
        val n = size
        val vX = DoubleArray(n)
        for (i in 0 until n) {
            vX[i] = vA[i] + vB[vI[i]]
        }
        return vX
    }

    @Benchmark
    fun vectorReductionSum(): Double {
        val n = size
        var x = 0.0
        for (i in 0 until n) {
            x += vA[i]
        }
        return x
    }

    @Benchmark
    open fun nonVectorReductionSum(): Double {
        val n = size
        var x = 0.0
        var i = 0
        // Trip-counted loop to prevent vectorization.
        while (i < n) {
            x += vA[i]
            i += step()
        }
        return x
    }

    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    private fun step() = 1

    @Benchmark
    open fun vectorReductionSumPredicated(): Double {
        val n = size
        var x = 0.0
        for (i in 0 until n) {
            if (vP[i]) {
                x += vA[i]
            }
        }
        return x
    }

    @Benchmark
    fun vectorDotProduct(): Double {
        val n = size
        var x = 0.0
        for (i in 0 until n) {
            x += vA[i] * vB[i]
        }
        return x
    }

    @Benchmark
    fun vectorReductionMax(): Double {
        val n = size
        var x = Double.NEGATIVE_INFINITY
        for (i in 0 until n) {
            x = max(x, vA[i])
        }
        return x
    }

}