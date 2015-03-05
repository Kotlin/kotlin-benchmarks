package org.jetbrains

import org.openjdk.jmh.annotations.*
import java.util.concurrent.TimeUnit

/**
 * This test checks work with long numbers using Fibonacci sequence
 */

BenchmarkMode(Mode.AverageTime)
OutputTimeUnit(TimeUnit.NANOSECONDS)
open public class FibonacciBenchmark : SizedBenchmark() {

    CompilerControl(CompilerControl.Mode.DONT_INLINE)
    Benchmark fun calc(): Long {
        // This test works CRITICALLY slower compared with java equivalent (05.03.2015)
        var a = 1L
        var b = 2L
        // Probably for with downTo is the reason of slowness
        for (i in size downTo 1) {
            val next = a + b
            a = b
            b = next
        }
        return b
    }

    CompilerControl(CompilerControl.Mode.DONT_INLINE)
    Benchmark fun calcSquare(): Long {
        // This test works CRITICALLY slower compared with java equivalent (05.03.2015)
        var a = 1L
        var b = 2L
        val s = size.toLong()
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