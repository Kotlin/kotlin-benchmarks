package org.jetbrains;

import org.openjdk.jmh.annotations.*;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
public class JavaIntListBenchmark {
    public static ArrayList<Integer> makeList(Iterable<Integer> iter) {
        ArrayList<Integer> list = new ArrayList<Integer>();
        for (Integer item : iter) {
            list.add(item);
        }
        return list;
    }

    public ArrayList<Integer> data = makeList(JetbrainsPackage.intValues());

    @Benchmark
    public int countFilteredManual() {
        int count = 0;
        for (int item : data) {
            if ((item & 1) == 0)
                count++;
        }
        return count;
    }
}
