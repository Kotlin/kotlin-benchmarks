package org.jetbrains

import org.openjdk.jmh.annotations.*
import java.util.Comparator
import java.util.NavigableSet
import java.util.Random
import java.util.TreeSet
import java.util.concurrent.TimeUnit

/**
 * Additional range functions
 */

private infix fun <T: Comparable<T>> ClosedRange<T>.joinable(other: ClosedRange<T>): Boolean {
    return (other.start in this) || (other.endInclusive in this) || (start in other)
}

private class RangeComparator<T: Comparable<T>>: Comparator<ClosedRange<T>> {
    override fun compare(first: ClosedRange<T>, other: ClosedRange<T>): Int {
        return when {
            first.start < other.start -> -1
            first.start > other.start -> 1
            first.endInclusive < other.endInclusive -> -1
            first.endInclusive > other.endInclusive -> 1
            else -> 0
        }
    }
}

private data class InternalRange<T: Comparable<T>>(override val start: T, override val endInclusive: T): ClosedRange<T> {

    override fun contains(value: T) = value in start..endInclusive
}

private fun <T: Comparable<T>> ClosedRange<T>.join(other: ClosedRange<T>): ClosedRange<T>? {
    return if (this joinable other)
        InternalRange(minOf(start, this.start), maxOf(endInclusive, this.endInclusive))
    else
        null
}

private class KRangeSet<T: Comparable<T>> {
    val set: NavigableSet<ClosedRange<T>> = TreeSet(RangeComparator())

    private fun addInternal(range: ClosedRange<T>?) {
        if (range != null) add(range)
    }

    fun add(range: ClosedRange<T>) {
        // Join if necessary
        // Floor must have start <= range.start but can have end > range.end
        val floor = set.floor(range)
        if (floor != null && floor.joinable(range)) {
            // Replace floor with joined range, repeating attempt to join it further
            set.remove(floor)
            addInternal(floor.join(range))
            return
        }
        // Floor does not exist or floor.start <= floor.end < range.start <= range.end
        // Ceiling must have start >= range.start but can have end < range.end
        val ceiling = set.ceiling(range)
        if (ceiling != null && ceiling.joinable(range)) {
            // Replace ceiling with joined range, repeating attempt to join it further
            set.remove(ceiling)
            addInternal(ceiling.join(range))
            return
        }
        // Ceiling does not exist or range.start <= range.end < ceiling.start <= ceiling.end
        // Just add it
        set.add(range)
    }

    fun contains(elem: T): Boolean {
        // Fast ranges check, ~log2(set.size())
        val range = InternalRange(elem, elem)
        // Floor must have start <= elem but can have end > elem
        // All ranges have start > elem
        val floor = set.floor(range) ?: return false
        if (floor.contains(elem))
            return true
        // Floor exists but has end < elem
        // Ceiling must have start >= elem and end >= elem
        // All ranges have start < elem
        val ceiling = set.ceiling(range) ?: return false
        return ceiling.contains(elem)
    }
}

/**
 * This class tests tree set performance
 *
 * @author Mikhail Glukhikh
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
open class RangeSetBenchmark: SmallSizedBenchmark() {
    companion object {
        val random = Random(2424)
    }

    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    @Benchmark
    fun add(): Boolean {
        val set = KRangeSet<Int>()
        // Should be ~size*log2(size)
        for (i in 1..smallSize) {
            val start = random.nextInt()
            val end = start + random.nextInt(1024)
            set.add(InternalRange(start, end))
        }
        return set.contains(666)
    }
}

