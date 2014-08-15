package org.jetbrains

import org.openjdk.jmh.annotations.*
import java.util.concurrent.*

val loadSize = 100000

fun load(value: Int): Int {
    var acc = 0
    for (i in 0..loadSize) {
        acc = acc xor value.hashCode()
    }
    return acc
}

inline fun loadInline(value: Int): Int {
    var acc = 0
    for (i in 0..loadSize) {
        acc = acc xor value.hashCode()
    }
    return acc
}

fun <T> loadGeneric(value: T): Int {
    var acc = 0
    for (i in 0..loadSize) {
        acc = acc xor value.hashCode()
    }
    return acc
}

inline fun <T> loadGenericInline(value: T): Int {
    var acc = 0
    for (i in 0..loadSize) {
        acc = acc xor value.hashCode()
    }
    return acc
}

State(Scope.Thread)
BenchmarkMode(Mode.Throughput)
OutputTimeUnit(TimeUnit.SECONDS)
open class InlineBenchmark {
    private var data = 2138476523

    Benchmark fun calculate(): Int {
        return load(data)
    }

    Benchmark fun calculateInline(): Int {
        return loadInline(data)
    }

    Benchmark fun calculateGeneric(): Int {
        return loadGeneric(data)
    }

    Benchmark fun calculateGenericInline(): Int {
        return loadGenericInline(data)
    }
}