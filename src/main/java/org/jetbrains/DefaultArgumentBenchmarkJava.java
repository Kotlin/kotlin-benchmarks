package org.jetbrains;

import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

/**
 * Created by Mikhail.Glukhikh on 10/03/2015.
 *
 * Comparison with Kotlin's functions with default parameters
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class DefaultArgumentBenchmarkJava extends SizedBenchmark {

    public int squareFun(int first, int second, int third, int fourth) {
        return first*first + second*second + third*third + fourth*fourth;
    }

    @Benchmark
    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    public int testWithOne() {
        final int size = getSize();
        int res = 0;
        for (int i=0; i<size; i++)
            res += squareFun(i*i, 0, 1, 1);
        return res;
    }

}
