package org.jetbrains

import org.openjdk.jmh.annotations.*
import org.openjdk.jmh.infra.Blackhole
import java.util.*
import java.util.concurrent.TimeUnit

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@CompilerControl(CompilerControl.Mode.DONT_INLINE)
open class NestedForLoopBenchmark: SmallSizedBenchmark() {
    var sizeLong: Long = 0L

    @Setup
    fun setup() {
        sizeLong = size.toLong()
    }

    @Benchmark fun nestedIntRangeToLoop(bh: Blackhole) {
        for (i in 1..size) {
            for (j in 1..i) {
                bh.consume(j)
            }
        }
    }

    @Benchmark fun nestedLongRangeToLoop(bh: Blackhole) {
        for (i in 1L..sizeLong) {
            for (j in 1L..i) {
                bh.consume(j)
            }
        }
    }

    @Benchmark fun nestedIntDownToLoop(bh: Blackhole) {
        for (i in size downTo 1) {
            for (j in i downTo 1) {
                bh.consume(j)
            }
        }
    }

    @Benchmark fun nestedLongDownToLoop(bh: Blackhole) {
        for (i in sizeLong downTo 1L) {
            for (j in i downTo 1L) {
                bh.consume(j)
            }
        }
    }

    @Benchmark fun nestedIntUntilLoop(bh: Blackhole) {
        for (i in 0 until size) {
            for (j in 0 until i) {
                bh.consume(j)
            }
        }
    }

    @Benchmark fun nestedLongUntilLoop(bh: Blackhole) {
        for (i in 0L until sizeLong) {
            for (j in 0L until i) {
                bh.consume(j)
            }
        }
    }

    @Benchmark fun nestedIntRangeToAndDownToLoop(bh: Blackhole) {
        for (i in 1..size) {
            for (j in i downTo 1) {
                bh.consume(j)
            }
        }
    }

    @Benchmark fun nestedLongRangeToAndDownToLoop(bh: Blackhole) {
        for (i in 1L..sizeLong) {
            for (j in i downTo 1L) {
                bh.consume(j)
            }
        }
    }

    @Benchmark fun nestedIntUntilAndRangeToLoop(bh: Blackhole) {
        for (i in 0 until size) {
            for (j in 1..i) {
                bh.consume(j)
            }
        }
    }

    @Benchmark fun nestedLongUntilAndRangeToLoop(bh: Blackhole) {
        for (i in 0L until sizeLong) {
            for (j in 1L..i) {
                bh.consume(j)
            }
        }
    }
}