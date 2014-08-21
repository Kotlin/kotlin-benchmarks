package org.jetbrains;

import org.openjdk.jmh.annotations.*;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class JavaClassListBenchmark extends SizedBenchmark {
    public ArrayList<Value> data;

    @Setup
    public void setup() {
        ArrayList<Value> list = new ArrayList<Value>();
        for (Value item : JetbrainsPackage.classValues(getSize())) {
            list.add(item);
        }
        data = list;
    }

    @Benchmark
    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    public int countFilteredManual() {
        int count = 0;
        for (Value item : data) {
            if ((item.getValue() & 1) == 0)
                count++;
        }
        return count;
    }
}
