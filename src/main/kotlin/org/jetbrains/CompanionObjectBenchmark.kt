package org.jetbrains

import org.openjdk.jmh.annotations.*
import java.util.concurrent.TimeUnit

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
open class CompanionObjectBenchmark {
    @Benchmark fun invokeRegularFunction(): Any {
        return regularCompanionObjectFunction("")
    }

    @Benchmark fun invokeJvmStaticFunction(): Any {
        return staticCompanionObjectFunction("")
    }

    companion object {
        @CompilerControl(CompilerControl.Mode.DONT_INLINE)
        fun regularCompanionObjectFunction(o: Any): Any {
            return o
        }

        @CompilerControl(CompilerControl.Mode.DONT_INLINE)
        @JvmStatic fun staticCompanionObjectFunction(o: Any): Any {
            return o
        }
    }
}