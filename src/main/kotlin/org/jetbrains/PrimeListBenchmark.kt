package org.jetbrains

import org.openjdk.jmh.annotations.*
import java.util.LinkedList
import java.util.concurrent.TimeUnit

/**
 * This class tests linked list performance
 * using prime number calculation algorithms
 *
 * @author Mikhail Glukhikh
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
open class PrimeListBenchmark: SizedBenchmark() {
    private var primes: MutableList<Int> = LinkedList()

    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    @Benchmark
    fun calcDirect() {
        primes.clear()
        primes.add(2)
        var i = 3
        while (i <= size) {
            var simple = true
            for (prime in primes) {
                if (prime * prime > i)
                    break
                if (i % prime == 0) {
                    simple = false
                    break
                }
            }
            if (simple)
                primes.add(i)
            i += 2
        }
    }

    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    @Benchmark
    fun calcEratosthenes() {
        primes.clear()
        primes.addAll(2..size)
        var i = 0
        while (i < primes.size) {
            val divisor = primes[i]
            primes.removeAll { it -> it > divisor && it % divisor == 0 }
            i++
        }
    }
}