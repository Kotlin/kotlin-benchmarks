package org.jetbrains

import org.openjdk.jmh.annotations.*
import java.util.concurrent.*
import org.openjdk.jmh.infra.Blackhole

fun load(value: Int, size: Int): Int {
    var acc = 0
    for (i in 0..size) {
        acc = acc xor value.hashCode()
    }
    return acc
}

inline fun loadInline(value: Int, size: Int): Int {
    var acc = 0
    for (i in 0..size) {
        acc = acc xor value.hashCode()
    }
    return acc
}

fun <T> loadGeneric(value: T, size: Int): Int {
    var acc = 0
    for (i in 0..size) {
        acc = acc xor value.hashCode()
    }
    return acc
}

inline fun <T> loadGenericInline(value: T, size: Int): Int {
    var acc = 0
    for (i in 0..size) {
        acc = acc xor value.hashCode()
    }
    return acc
}

BenchmarkMode(Mode.AverageTime)
OutputTimeUnit(TimeUnit.NANOSECONDS)
open class InlineBenchmark : SizedBenchmark() {
    private var value = 2138476523


    CompilerControl(CompilerControl.Mode.DONT_INLINE)
    Benchmark fun calculate(): Int {
        return load(value, size)
    }

    CompilerControl(CompilerControl.Mode.DONT_INLINE)
    Benchmark fun calculateInline(): Int {
        return loadInline(value, size)
    }

    CompilerControl(CompilerControl.Mode.DONT_INLINE)
    Benchmark fun calculateGeneric(): Int {
        return loadGeneric(value, size)
    }

    CompilerControl(CompilerControl.Mode.DONT_INLINE)
    Benchmark fun calculateGenericInline(): Int {
        return loadGenericInline(value, size)
    }
}