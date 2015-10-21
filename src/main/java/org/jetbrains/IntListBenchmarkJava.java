package org.jetbrains;

import org.openjdk.jmh.annotations.*;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class IntListBenchmarkJava extends SizedBenchmark {
    public ArrayList<Integer> data;

    @Setup
    public void setup() {
        ArrayList<Integer> list = new ArrayList<Integer>();
        for (Integer item : DataKt.intValues(getSize())) {
            list.add(item);
        }
        data = list;
    }

    @Benchmark
    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    public int countFilteredManual() {
        int count = 0;
        for (int it : data) {
            if (DataKt.filterLoad(it))
                count++;
        }
        return count;
    }
}
