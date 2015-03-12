package org.jetbrains

import org.openjdk.jmh.annotations.*
import java.util.ArrayList
import java.util.HashMap
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
    Benchmark fun problem1() = (1..size).sum( { it % 3 == 0 || it % 5 == 0} )

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
                        return i
                    }
                }
            }
        }
        return -1
    }

    private val veryLongNumber = """
        73167176531330624919225119674426574742355349194934
        96983520312774506326239578318016984801869478851843
        85861560789112949495459501737958331952853208805511
        12540698747158523863050715693290963295227443043557
        66896648950445244523161731856403098711121722383113
        62229893423380308135336276614282806444486645238749
        30358907296290491560440772390713810515859307960866
        70172427121883998797908792274921901699720888093776
        65727333001053367881220235421809751254540594752243
        52584907711670556013604839586446706324415722155397
        53697817977846174064955149290862569321978468622482
        83972241375657056057490261407972968652414535100474
        82166370484403199890008895243450658541227588666881
        16427171479924442928230863465674813919123162824586
        17866458359124566529476545682848912883142607690042
        24219022671055626321111109370544217506941658960408
        07198403850962455444362981230987879927244284909188
        84580156166097919133875499200524063689912560717606
        05886116467109405077541002256983155200055935729725
        71636269561882670428252483600823257530420752963450
    """

    CompilerControl(CompilerControl.Mode.DONT_INLINE)
    Benchmark fun problem8(): Long {
        val productSize = when(size) {
            in 1..10 -> 4
            in 11..1000 -> 8
            else -> 13
        }
        val digits: MutableList<Int> = ArrayList<Int>()
        for (digit in veryLongNumber) {
            if (digit in '0'..'9') {
                digits.add(digit.toInt() - '0'.toInt())
            }
        }
        var largest = 0L
        for (i in 0..digits.size()-productSize-1) {
            var product = 1L
            for (j in 0..productSize-1) {
                product *= digits[i+j]
            }
            largest = max(product, largest)
        }
        return largest
    }

    CompilerControl(CompilerControl.Mode.DONT_INLINE)
    Benchmark fun problem9(): Long {
        val size = size // Looks awful but removes all implicit getSize() calls
        for (c in size/3..size-3) {
            val c2 = c.toLong() * c.toLong()
            for (b in (size-c)/2..c-1) {
                if (b+c >= size)
                    break
                val a = size - b - c
                if (a >= b)
                    continue
                val b2 = b.toLong() * b.toLong()
                val a2 = a.toLong() * a.toLong()
                if (c2 == b2 + a2) {
                    return a.toLong() * b.toLong() * c.toLong()
                }
            }
        }
        return -1L
    }

    data class Children(val left: Int, val right: Int)

    CompilerControl(CompilerControl.Mode.DONT_INLINE)
    Benchmark fun problem14(): List<Int> {
        // Simplified problem is solved here: it's not allowed to leave the interval [0..size) inside a number chain
        val size = size
        // Build a tree
        // index is produced from first & second
        val tree = Array<Children>(size, { i -> Children(i*2, if (i>4 && (i+2) % 6 == 0) (i-1)/3 else 0)})
        // Find longest chain by DFS
        fun dfs(begin: Int): List<Int> {
            if (begin == 0 || begin >= size)
                return listOf()
            val left = dfs(tree[begin].left)
            val right = dfs(tree[begin].right)
            return linkedListOf(begin) + if (left.size() > right.size()) left else right
        }
        return dfs(1)
    }

    data class Way(val length: Int, val next: Int)

    CompilerControl(CompilerControl.Mode.DONT_INLINE)
    Benchmark fun problem14full(): List<Int> {
        val size = size
        // Previous achievements: map (number) -> (length, next)
        val map: MutableMap<Int, Way> = HashMap()
        // Starting point
        map.put(1, Way(0, 0))
        // Check all other numbers
        var bestNum = 0
        var bestLen = 0
        fun go(begin: Int): Way {
            val res = map[begin]
            if (res != null)
                return res
            val next = if (begin % 2 == 0) begin/2 else 3*begin+1
            val childRes = go(next)
            val myRes = Way(childRes.length + 1, next)
            map[begin] = myRes
            return myRes
        }
        for (i in 2..size-1) {
            val res = go(i)
            if (res.length > bestLen) {
                bestLen = res.length
                bestNum = i
            }
        }
        fun unroll(begin: Int): List<Int> {
            if (begin == 0)
                return listOf()
            val next = map[begin]?.next ?: 0
            return linkedListOf(begin) + unroll(next)
        }
        return unroll(bestNum)
    }
}
