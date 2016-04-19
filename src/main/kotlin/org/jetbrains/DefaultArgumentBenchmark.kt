package org.jetbrains

import org.openjdk.jmh.annotations.*
import java.util.concurrent.ThreadLocalRandom
import java.util.concurrent.TimeUnit

/**
 * Created by Mikhail.Glukhikh on 10/03/2015.
 *
 * Tests performance for function calls with default parameters
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
open class DefaultArgumentBenchmark {
    private var arg = 0

    @Setup fun init() {
        arg = ThreadLocalRandom.current().nextInt()
    }

    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    fun sumTwo(first: Int, second: Int = 0): Int {
        return first + second
    }

    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    fun sumFour(first: Int, second: Int = 0, third: Int = 1, fourth: Int = third): Int {
        return first + second + third + fourth
    }

    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    fun sumEight(first: Int, second: Int = 0, third: Int = 1, fourth: Int = third,
                 fifth: Int = fourth, sixth: Int = fifth, seventh: Int = second, eighth: Int = seventh): Int {
        return first + second + third + fourth + fifth + sixth + seventh + eighth
    }

    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    @Benchmark
    fun testOneOfTwo(): Int {
        return sumTwo(arg)
    }

    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    @Benchmark
    fun testTwoOfTwo(): Int {
        return sumTwo(arg, arg)
    }
    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    @Benchmark
    fun testOneOfFour(): Int {
        return sumFour(arg)
    }

    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    @Benchmark
    fun testFourOfFour(): Int {
        return sumFour(arg, arg, arg, arg)
    }

    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    @Benchmark
    fun testOneOfEight(): Int {
        return sumEight(arg)
    }

    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    @Benchmark
    fun testEightOfEight(): Int {
        return sumEight(arg, arg, arg, arg, arg, arg, arg, arg)
    }
}
