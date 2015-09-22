package org.jetbrains

import org.openjdk.jmh.annotations.*
import java.util.concurrent.TimeUnit

/**
 * Created by Mikhail.Glukhikh on 10/03/2015.
 *
 * Tests performance for function calls with default parameters
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
open class DefaultArgumentBenchmark: SizedBenchmark() {

    public fun squareFun(first: Int, second: Int = 0, third: Int = 1, fourth: Int = third): Int {
        return first*first + second*second + third*third + fourth*fourth
    }

    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    @Benchmark
    fun testWithOne(): Int {
        var sum = 0
        for (i in 0..size-1)
            sum += squareFun(i)
        return sum
    }

}
