package org.jetbrains;

import org.openjdk.jmh.annotations.*;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
public class JavaIntArrayBenchmark {
    public static int[] makeList(Iterable<Integer> iter) {
        ArrayList<Integer> list = new ArrayList<Integer>();
        for (Integer item : iter) {
            list.add(item);
        }
        int[] array = new int[list.size()];
        for (int i = 0; i < list.size(); i++)
            array[i] = list.get(i);
        return array;
    }

    public int[] data = makeList(JetbrainsPackage.intValues());

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
