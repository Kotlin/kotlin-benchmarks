package org.jetbrains

import org.openjdk.jmh.annotations.*
import java.util.HashMap
import java.util.Random
import java.util.concurrent.TimeUnit

/**
 * This class emulates matrix behaviour using a hash map as its implementation
 */
class KMatrix internal constructor(rows: Int, columns: Int) {
    companion object {
        private val random = Random(4242)
    }

    private val matrix: MutableMap<Pair<Int, Int>, Double> = HashMap()

    init {
        for (row in 0 until rows) {
            for (col in 0 until columns) {
                matrix[Pair(row, col)] = random.nextDouble()
            }
        }
    }

    fun get(row: Int, col: Int): Double {
        return get(Pair(row, col))
    }

    fun get(pair: Pair<Int, Int>): Double {
        return matrix.getOrElse(pair) { 0.0 }
    }

    fun put(pair: Pair<Int, Int>, elem: Double) {
        matrix[pair] = elem
    }

    operator fun plusAssign(other: KMatrix) {
        for (entry in matrix.entries) {
            put(entry.key, entry.value + other.get(entry.key))
        }
    }
}

/**
 * This class tests hash map performance
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
open class MatrixMapBenchmark: SizedBenchmark() {

    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    @Benchmark
    fun add(): KMatrix {
        var rows = size
        var cols = 1
        while (rows > cols) {
            rows /= 2
            cols *= 2
        }
        val a = KMatrix(rows, cols)
        val b = KMatrix(rows, cols)
        a += b
        return a
    }

}