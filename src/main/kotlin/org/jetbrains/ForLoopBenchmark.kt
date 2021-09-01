package org.jetbrains

import org.openjdk.jmh.annotations.*
import org.openjdk.jmh.infra.Blackhole
import java.util.*
import java.util.concurrent.TimeUnit

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@CompilerControl(CompilerControl.Mode.DONT_INLINE)
open class ForLoopBenchmark: SizedBenchmark() {
    var sizeLong: Long = 0L
    var intRange: IntRange = IntRange.EMPTY
    var longRange: LongRange = LongRange.EMPTY
    var array: IntArray = IntArray(0)

    @Setup
    fun setup() {
        sizeLong = size.toLong()
        intRange = 1..size
        longRange = 1L..sizeLong
        array = IntArray(size)
    }

    @Benchmark fun arrayLoop(bh: Blackhole) {
        for (i in array) {
            bh.consume(i)
        }
    }

    @Benchmark fun arrayIndicesLoop(bh: Blackhole) {
        for (i in array.indices) {
            bh.consume(i)
        }
    }

    @Benchmark fun intRangeLiteralLoop(bh: Blackhole) {
        for (i in 1..size) {
            bh.consume(i)
        }
    }

    @Benchmark fun intRangeExpressionLoop(bh: Blackhole) {
        for (i in intRange) {
            bh.consume(i)
        }
    }

   @Benchmark fun longRangeLiteralLoop(bh: Blackhole) {
        for (i in 1L..sizeLong) {
            bh.consume(i)
        }
    }

    @Benchmark fun longRangeExpressionLoop(bh: Blackhole) {
        for (i in longRange) {
            bh.consume(i)
        }
    }

    @Benchmark fun intDownToLoop(bh: Blackhole) {
        for (i in size downTo 1) {
            bh.consume(i)
        }
    }

    @Benchmark fun longDownToLoop(bh: Blackhole) {
        for (i in sizeLong downTo 1L) {
            bh.consume(i)
        }
    }

    @Benchmark fun intUntilLoop(bh: Blackhole) {
        for (i in 0 until size) {
            bh.consume(i)
        }
    }

    @Benchmark fun longUntilLoop(bh: Blackhole) {
        for (i in 0L until sizeLong) {
            bh.consume(i)
        }
    }
}