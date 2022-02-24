package org.jetbrains;

import org.openjdk.jmh.annotations.*;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class ClassListBenchmarkJava extends SizedBenchmark {
    public ArrayList<Value> data;

    @Setup
    public void setup() {
        ArrayList<Value> list = new ArrayList<>();
        for (Value item : DataKt.classValues(getSize())) {
            list.add(item);
        }
        data = list;
    }

    @Benchmark
    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    public int countFilteredManual() {
        int count = 0;
        for (Value it : data) {
            if (DataKt.filterLoad(it))
                count++;
        }
        return count;
    }
}
