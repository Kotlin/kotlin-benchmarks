package org.jetbrains

import org.openjdk.jmh.annotations.*
import org.openjdk.jmh.infra.Blackhole
import java.util.concurrent.TimeUnit

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@CompilerControl(CompilerControl.Mode.DONT_INLINE)
open class ConstForLoopBenchmark {
    @Benchmark fun intRangeLiteralLoop(bh: Blackhole) {
        for (i in 1..CONST_SIZE) {
            bh.consume(i)
        }
    }

   @Benchmark fun longRangeLiteralLoop(bh: Blackhole) {
        for (i in 1L..CONST_SIZE_LONG) {
            bh.consume(i)
        }
    }

    @Benchmark fun intDownToLoop(bh: Blackhole) {
        for (i in CONST_SIZE downTo 1) {
            bh.consume(i)
        }
    }

    @Benchmark fun longDownToLoop(bh: Blackhole) {
        for (i in CONST_SIZE_LONG downTo 1L) {
            bh.consume(i)
        }
    }

    @Benchmark fun intUntilLoop(bh: Blackhole) {
        for (i in 0 until CONST_SIZE) {
            bh.consume(i)
        }
    }

    @Benchmark fun longUntilLoop(bh: Blackhole) {
        for (i in 0L until CONST_SIZE_LONG) {
            bh.consume(i)
        }
    }
}