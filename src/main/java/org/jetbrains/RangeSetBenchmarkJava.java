package org.jetbrains;

import org.jetbrains.annotations.NotNull;
import org.openjdk.jmh.annotations.*;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * A set of comparables represented by a set of their ranges
 * @param <T> a given comparable
 */
class RangeSet<T extends Comparable<T>> {
    /**
     * A range of two comparables
     * @param <T> a given comparable
     */
    static class ComparableRange<T extends Comparable<T>> implements Comparable<ComparableRange<T>> {
        private final T start, end;

        private static <T extends Comparable<T>> T min(T a, T b) {
            return a.compareTo(b) >= 0 ? b : a;
        }

        private static <T extends Comparable<T>> T max(T a, T b) {
            return a.compareTo(b) >= 0 ? a : b;
        }

        public ComparableRange(T start, T end) {
            if (start.compareTo(end) < 0) {
                this.end = start;
                this.start = end;
            } else {
                this.start = start;
                this.end = end;
            }
        }

        public int compareTo(@NotNull ComparableRange<T> other) {
            final int res = start.compareTo(other.start);
            return res != 0 ? res : end.compareTo(other.end);
        }

        public boolean joinable(ComparableRange<T> other) {
            return (start.compareTo(other.start) <= 0 && end.compareTo(other.start) >= 0) ||
                    (start.compareTo(other.end) <= 0 && end.compareTo(other.end) >= 0) ||
                    (start.compareTo(other.start) >= 0 && start.compareTo(other.end) <= 0);
        }

        public ComparableRange<T> join(ComparableRange<T> other) {
            return joinable(other) ? new ComparableRange<T>(min(start, other.start), max(end, other.end)) : null;
        }

        public boolean contains(T elem) {
            return start.compareTo(elem) <= 0 && end.compareTo(elem) >= 0;
        }
    }

    private final NavigableSet<ComparableRange<T>> set = new TreeSet<ComparableRange<T>>();

    public void add(ComparableRange<T> range) {
        // Join if necessary
        // Floor must have start <= range.start but can have end > range.end
        final ComparableRange<T> floor = set.floor(range);
        if (floor != null && floor.joinable(range)) {
            // Replace floor with joined range, repeating attempt to join it further
            set.remove(floor);
            add(floor.join(range));
            return;
        }
        // Floor does not exist or floor.start <= floor.end < range.start <= range.end
        // Ceiling must have start >= range.start but can have end < range.end
        final ComparableRange<T> ceiling = set.ceiling(range);
        if (ceiling != null && ceiling.joinable(range)) {
            // Replace ceiling with joined range, repeating attempt to join it further
            set.remove(ceiling);
            add(ceiling.join(range));
            return;
        }
        // Ceiling does not exist or range.start <= range.end < ceiling.start <= ceiling.end
        // Just add it
        set.add(range);
    }

    public boolean contains(T elem) {
        // Fast ranges check, ~log2(set.size())
        final ComparableRange<T> range = new ComparableRange<T>(elem, elem);
        // Floor must have start <= elem but can have end > elem
        final ComparableRange<T> floor = set.floor(range);
        // All ranges have start > elem
        if (floor == null)
            return false;
        if (floor.contains(elem))
            return true;
        // Floor exists but has end < elem
        // Ceiling must have start >= elem and end >= elem
        final ComparableRange<T> ceiling = set.ceiling(range);
        // All ranges have start < elem
        return ceiling != null && ceiling.contains(elem);
    }
}

/**
 * This class tests tree set performance
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class RangeSetBenchmarkJava extends SizedBenchmark {

    private static final Random random = new Random(2424);

    @Benchmark
    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    public boolean create() {
        final RangeSet<Integer> set = new RangeSet<Integer>();
        // Should be ~size*log2(size)
        for (int i=0; i<getSize(); i++) {
            final int start = random.nextInt();
            final int end = start + random.nextInt(1024);
            set.add(new RangeSet.ComparableRange<Integer>(start, end));
        }
        return set.contains(666);
    }
}
