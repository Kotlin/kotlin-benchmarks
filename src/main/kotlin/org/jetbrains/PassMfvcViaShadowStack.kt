package org.jetbrains

import org.openjdk.jmh.annotations.*
import org.openjdk.jmh.infra.Blackhole
import java.util.concurrent.TimeUnit

private class ShadowStack(initialCapacity: Int = 10) {
    init {
        require(initialCapacity > 0)
    }
    private var storage = LongArray(initialCapacity)
    private var offset = 0
    fun pushLong(value: Long) {
        if (offset == storage.size) {
            val oldStorage = storage
            storage = LongArray(storage.size * 2)
            for (i in oldStorage.indices) {
                storage[i] = oldStorage[i]
            }
        }
        storage[offset++] = value
    }
    fun popLong(): Long = storage[--offset]
}

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Thread)
open class MfvcShadowStack {
    @Param("3", "10", "50")
    private var n = 0
    private val shadowStack = ShadowStack()

    @Setup(Level.Iteration)
    fun setContent() {
        val shadowResult = fibonacciShadow(n)
        val boxedResult = fibonacciShadow(n)
        require(shadowResult == boxedResult) { "Shadow: $shadowResult\nBoxed: $boxedResult" }
    }
    
    private fun fibonacciShadowHelper(n: Int) {
        if (n > 0) {
            fibonacciShadowHelper(n - 1)
            val y = shadowStack.popLong()
            val x = shadowStack.popLong()
            shadowStack.pushLong(y)
            shadowStack.pushLong(x + y)
        } else {
            shadowStack.pushLong(0)
            shadowStack.pushLong(1)
        }
    }

    @Benchmark
    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    fun shadow(bh: Blackhole) {
        bh.consume(fibonacciShadow(n))
    }

    private fun fibonacciShadow(n: Int): Long {
        fibonacciShadowHelper(n)
        shadowStack.popLong()
        return shadowStack.popLong()
    }

    @Benchmark
    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    fun boxed(bh: Blackhole) {
        bh.consume(fibonacciBoxed(n))
    }

    private fun fibonacciBoxed(n: Int) = fibonacciBoxedHelper(n).x

    private fun fibonacciBoxedHelper(n: Int): LongPair {
        if (n <= 0) return LongPair(0L, 1L)
        val (x, y) = fibonacciBoxedHelper(n - 1)
        return LongPair(y, x + y)
    }

    private data class LongPair(val x: Long, val y: Long)
}