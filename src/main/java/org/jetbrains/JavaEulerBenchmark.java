package org.jetbrains;

import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

/**
 * A class tests decisions of various Euler problems
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class JavaEulerBenchmark extends SizedBenchmark {

    @Benchmark
    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    public int problem1() {
        // Number of 3/5 divisible numbers in 1..size
        int res = 0;
        for (int i=1; i<=getSize(); i++) {
            if (i % 3 == 0 || i % 5 == 0)
                res += i;
        }
        return res;
    }

    @Benchmark
    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    public int problem2() {
        // Number of even Fibonacci numbers in 1..size
        int res = 0;
        int prev = 1, curr = 2;
        while (curr <= getSize()) {
            if (curr % 2 == 0)
                res += curr;
            int next = prev + curr;
            prev = curr;
            curr = next;
        }
        return res;
    }

    static private boolean palindromic(long num) {
        final String s = Long.toString(num);
        return s.equals(new StringBuilder(s).reverse().toString());
    }

    @Benchmark
    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    public long problem4() {
        // Largest palindrom which is a production of two [size/10...size-1] numbers
        final long size = getSize();
        final long maxNum = (size-1)*(size-1), minNum = (size / 10) * (size / 10);
        final int maxDiv = getSize()-1, minDiv = getSize() / 10;
        for (long i=maxNum; i>=minNum; i--) {
            if (!palindromic(i))
                continue;
            for (int j=minDiv; j<=maxDiv; j++) {
                if (i % j == 0) {
                    final long res = i / j;
                    if (res >= minDiv && res <= maxDiv) {
                        System.out.printf("%d = %d * %d\n", i, j, res);
                        return i;
                    }
                }
            }
        }
        return -1;
    }
}
