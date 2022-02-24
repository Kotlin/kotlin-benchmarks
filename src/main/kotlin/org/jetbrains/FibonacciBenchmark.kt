package org.jetbrains

import org.openjdk.jmh.annotations.*
import java.util.concurrent.TimeUnit

/**
 * This test checks work with long numbers using Fibonacci sequence
 *
 * NB: all three tests here work CRITICALLY (x4...x6) slower than their Java equivalents
 * The reason is iteration on a progression formed as max downTo min or min..max step s.
 * In case of a range min..max primitive types are used by the Kotlin compiler,
 * but when we have a progression it's used directly with its iterator and so.
 */

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Thread)
open class FibonacciBenchmark {

    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    @Benchmark
    fun calcClassic(sb: SizedBenchmark): Long {
        var a = 1L
        var b = 2L
        val size = sb.size
        for (i in 0 until size) {
            val next = a + b
            a = b
            b = next
        }
        return b
    }

    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    @Benchmark
    fun calc(sb: SizedBenchmark): Long {
        // This test works CRITICALLY slower compared with java equivalent (05.03.2015)
        var a = 1L
        var b = 2L
        // Probably for with downTo is the reason of slowness
        for (i in sb.size downTo 1) {
            val next = a + b
            a = b
            b = next
        }
        return b
    }

    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    @Benchmark
    fun calcWithProgression(sb: SizedBenchmark): Long {
        // This test works CRITICALLY slower compared with java equivalent (05.03.2015)
        var a = 1L
        var b = 2L
        // Probably for with step is the reason of slowness
        for (i in 1 until 2*sb.size step 2) {
            val next = a + b
            a = b
            b = next
        }
        return b
    }

    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    @Benchmark
    fun calcSquare(sb: SmallSizedBenchmark): Long {
        // This test works CRITICALLY slower compared with java equivalent (05.03.2015)
        var a = 1L
        var b = 2L
        val s = sb.smallSize.toLong()
        val limit = s*s
        // Probably for with downTo is the reason of slowness
        for (i in limit downTo 1) {
            val next = a + b
            a = b
            b = next
        }
        return b
    }
}