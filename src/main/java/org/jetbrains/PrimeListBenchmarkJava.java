package org.jetbrains;

import org.jetbrains.SizedBenchmark;
import org.openjdk.jmh.annotations.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * This class tests linked list performance
 * using prime number calculation algorithms
 *
 * @author Mikhail Glukhikh
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Thread)
public class PrimeListBenchmarkJava {

    private List<Integer> primes = new LinkedList<>();

    @Benchmark
    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    public void calcDirect(SizedBenchmark sb) {
        primes.clear();
        primes.add(2);
        final int size = sb.getSize();
        for (int i=3; i<=size; i+=2) {
            boolean simple = true;
            for (int prime: primes) {
                if (prime * prime > i)
                    break;
                if (i % prime == 0) {
                    simple = false;
                    break;
                }
            }
            if (simple)
                primes.add(i);
        }
    }

    @Benchmark
    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    public void calcEratosthenes(SmallSizedBenchmark sb) {
        primes.clear();
        final int size = sb.getSmallSize();
        for (int i=2; i<=size; i++)
            primes.add(i);
        for (int i=0; i<primes.size(); i++) {
            final int divisor = primes.get(i);
            final Iterator<Integer> it = primes.iterator();
            while (it.hasNext()) {
                final int num = it.next();
                if (num > divisor && num % divisor == 0)
                    it.remove();
            }
        }
    }

}
