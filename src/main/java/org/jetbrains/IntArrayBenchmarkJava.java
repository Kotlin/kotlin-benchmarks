package org.jetbrains;

import org.openjdk.jmh.annotations.*;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class IntArrayBenchmarkJava extends SizedBenchmark {
    public int[] data;

    @Setup
    public void setup() {
        ArrayList<Integer> list = new ArrayList<Integer>();
        for (Integer item : JetbrainsPackage.intValues(getSize())) {
            list.add(item);
        }
        int[] array = new int[list.size()];
        for (int i = 0; i < list.size(); i++)
            array[i] = list.get(i);
        data = array;
    }

    @Benchmark
    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    public int countFilteredManual() {
        int count = 0;
        for (int it : data) {
            if (JetbrainsPackage.filterLoad(it))
                count++;
        }
        return count;
    }

    @Benchmark
    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    public int countFilteredSomeManual() {
        int count = 0;
        for (int it : data) {
            if (JetbrainsPackage.filterSome(it))
                count++;
        }
        return count;
    }

    @Benchmark
    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    public int countFilteredPrimeManual() {
        int count = 0;
        for (int it : data) {
            if (JetbrainsPackage.filterPrime(it))
                count++;
        }
        return count;
    }
}
