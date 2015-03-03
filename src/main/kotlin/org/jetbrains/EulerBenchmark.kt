package org.jetbrains

import org.openjdk.jmh.annotations.*
import java.util.ArrayList
import java.util.concurrent.TimeUnit

fun fibonacci(): Stream<Int> {
    var a = 0
    var b = 1
    fun next(): Int {
        val res = a + b
        a = b
        b = res
        return res
    }
    return stream { next() }
}

fun Any.isPalindrome() = toString() == toString().reverse()

fun IntRange.sum(predicate: (Int) -> Boolean): Int {
    var sum = 0
    for (i in this) if (predicate(i)) sum += i
    return sum
}

fun Stream<Int>.sum(predicate: (Int) -> Boolean): Int {
    var sum = 0
    for (i in this) if (predicate(i)) sum += i
    return sum
}

/**
 * A class tests decisions of various Euler problems
 *
 * NB: all tests here work slower than Java, probably because of all these functional wrappers
 */
BenchmarkMode(Mode.AverageTime)
OutputTimeUnit(TimeUnit.NANOSECONDS)
open public class EulerBenchmark : SizedBenchmark() {


    CompilerControl(CompilerControl.Mode.DONT_INLINE)
    Benchmark fun problem1byStream() = (1..size).stream().sum( { it % 3 == 0 || it % 5 == 0} )

    CompilerControl(CompilerControl.Mode.DONT_INLINE)
    Benchmark fun problem1byIterable() = (1..size).sum( { it % 3 == 0 || it % 5 == 0} )

    CompilerControl(CompilerControl.Mode.DONT_INLINE)
    Benchmark fun problem2() = fibonacci().takeWhile { it < size }.sum { it % 2 == 0 }

    CompilerControl(CompilerControl.Mode.DONT_INLINE)
    Benchmark fun problem4(): Long {
        val s: Long = size.toLong()
        val maxLimit = (s-1)*(s-1)
        val minLimit = (s/10)*(s/10)
        val maxDiv = size-1
        val minDiv = size/10
        for (i in maxLimit downTo minLimit) {
            if (!i.isPalindrome()) continue;
            for (j in minDiv..maxDiv) {
                if (i % j == 0L) {
                    val res = i / j
                    if (res in minDiv..maxDiv) {
                        println("$i = $j * $res")
                        return i
                    }
                }
            }
        }
        return -1
//        val result = ((size/10)..(size-1)).palindromes().max()
//
//        val (multiplier, multiplicand, product) = result
//        return product
    }
}
