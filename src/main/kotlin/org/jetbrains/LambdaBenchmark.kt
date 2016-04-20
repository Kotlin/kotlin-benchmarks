package org.jetbrains

import org.openjdk.jmh.annotations.*
import java.util.concurrent.ThreadLocalRandom
import java.util.concurrent.TimeUnit

var globalAddendum = 0

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
open class LambdaBenchmark : SizedBenchmark() {
    private fun <T> runLambda(x: () -> T): T = x()

    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    private fun <T> runLambdaNoInline(x: () -> T): T = x()

    @Setup fun init() {
        globalAddendum = ThreadLocalRandom.current().nextInt(20)
    }

    @Benchmark fun noncapturingLambda(): Int {
        var x: Int = 0
        for (i in 0..size) {
            x += runLambda { globalAddendum }
        }
        return x
    }

    @Benchmark fun noncapturingLambdaNoInline(): Int {
        var x: Int = 0
        for (i in 0..size) {
            x += runLambdaNoInline { globalAddendum }
        }
        return x
    }

    @Benchmark fun capturingLambda(): Int {
        val addendum = globalAddendum + 1
        var x: Int = 0
        for (i in 0..size) {
            x += runLambda { addendum }
        }
        return x
    }

    @Benchmark fun capturingLambdaNoInline(): Int {
        val addendum = globalAddendum + 1
        var x: Int = 0
        for (i in 0..size) {
            x += runLambdaNoInline { addendum }
        }
        return x
    }

    @Benchmark fun mutatingLambda(): Int {
        var x: Int = 0
        for (i in 0..size) {
            runLambda { x += globalAddendum }
        }
        return x
    }

    @Benchmark fun mutatingLambdaNoInline(): Int {
        var x: Int = 0
        for (i in 0..size) {
            runLambda { x += globalAddendum }
        }
        return x
    }
}