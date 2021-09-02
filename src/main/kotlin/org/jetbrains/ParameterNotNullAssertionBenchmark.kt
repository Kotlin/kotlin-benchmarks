package org.jetbrains

import org.openjdk.jmh.annotations.*
import org.openjdk.jmh.infra.Blackhole
import java.util.concurrent.TimeUnit

val OBJ = Any()

@Suppress("UNUSED_PARAMETER")
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
open class ParameterNotNullAssertionBenchmark {
    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    fun methodWithOneNotnullParameter(p: Any): Any {
        return p
    }

    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    private fun privateMethodWithOneNotnullParameter(p: Any): Any {
        return p
    }

    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    fun methodWithTwoNotnullParameters(p: Any, p2: Any): Any {
        return p
    }

    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    private fun privateMethodWithTwoNotnullParameters(p: Any, p2: Any): Any {
        return p
    }

    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    fun methodWithEightNotnullParameters(p: Any, p2: Any, p3: Any, p4: Any, p5: Any, p6: Any, p7: Any, p8: Any): Any {
        return p
    }

    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    private fun privateMethodWithEightNotnullParameters(p: Any, p2: Any, p3: Any, p4: Any, p5: Any, p6: Any, p7: Any, p8: Any): Any {
        return p
    }

    @Benchmark fun invokeOneArgWithNullCheck(): Any {
        return methodWithOneNotnullParameter(OBJ)
    }

    @Benchmark fun invokeOneArgWithoutNullCheck(): Any {
        return privateMethodWithOneNotnullParameter(OBJ)
    }

    @Benchmark fun invokeTwoArgsWithNullCheck(): Any {
        return methodWithTwoNotnullParameters(OBJ, OBJ)
    }

    @Benchmark fun invokeTwoArgsWithoutNullCheck(): Any {
        return privateMethodWithTwoNotnullParameters(OBJ, OBJ)
    }

    @Benchmark fun invokeEightArgsWithNullCheck(): Any {
        return methodWithEightNotnullParameters(OBJ, OBJ, OBJ, OBJ, OBJ, OBJ, OBJ, OBJ)
    }

    @Benchmark fun invokeEightArgsWithoutNullCheck(): Any {
        return privateMethodWithEightNotnullParameters(OBJ, OBJ, OBJ, OBJ, OBJ, OBJ, OBJ, OBJ)
    }
}

