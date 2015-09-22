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

private fun <T: Comparable<T>> Range<T>.joinable(other: Range<T>): Boolean {
    return (other.start in this) || (other.end in this) || (start in other);
}

private class RangeComparator<T: Comparable<T>>: Comparator<Range<T>> {
    public override fun compare(first: Range<T>, other: Range<T>): Int {
        when {
            first.start < other.start -> return -1
            first.start > other.start -> return 1
            first.end < other.end -> return -1
            first.end > other.end -> return 1
            else -> return 0
        }
    }
}

private fun <T: Comparable<T>> min(a: T, b: T) = if (a > b) b else a

private fun <T: Comparable<T>> max(a: T, b: T) = if (a > b) a else b

private fun <T: Comparable<T>> ComparableRange<T>.join(other: Range<T>): ComparableRange<T>? {
    return if (this joinable other) ComparableRange(min(start, this.start), max(end, this.end)) else null
}

private class KRangeSet<T: Comparable<T>> {
    val set: NavigableSet<ComparableRange<T>> = TreeSet(RangeComparator<T>())

    private fun addInternal(range: ComparableRange<T>?) {
        if (range != null) add(range)
    }

    public fun add(range: ComparableRange<T>) {
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

    public fun contains(elem: T): Boolean {
        // Fast ranges check, ~log2(set.size())
        val range = ComparableRange(elem, elem)
        // Floor must have start <= elem but can have end > elem
        val floor = set.floor(range)
        // All ranges have start > elem
        if (floor == null)
            return false
        if (floor.contains(elem))
            return true
        // Floor exists but has end < elem
        // Ceiling must have start >= elem and end >= elem
        val ceiling = set.ceiling(range)
        // All ranges have start < elem
        if (ceiling == null)
            return false
        return ceiling.contains(elem)
    }
}

/**
 * This class tests tree set performance
 *
 * @author Mikhail Glukhikh
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
open class RangeSetBenchmark: SizedBenchmark() {
    companion object {
        val random = Random(2424)
    }

    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    @Benchmark
    fun add(): Boolean {
        val set = KRangeSet<Int>()
        // Should be ~size*log2(size)
        for (i in 1..size) {
            val start = random.nextInt()
            val end = start + random.nextInt(1024)
            set.add(ComparableRange(start, end))
        }
        return set.contains(666)
    }
}

