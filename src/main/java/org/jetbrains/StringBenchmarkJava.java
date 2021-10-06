package org.jetbrains;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class StringBenchmarkJava extends SizedBenchmark {
    public ArrayList<String> data;
    public double[] doubleData;

    private String csv = "";

    @Setup
    public void setup() {
        ArrayList<String> list = new ArrayList<String>();
        for (String item : DataKt.stringValues(getSize())) {
            list.add(item);
        }
        data = list;
        final Random random = new Random(123456789);
        csv = "";
        boolean p = false;
        for (int i=0; i<getSize(); i++) {
            if (p) {
                csv += ",";
            } else {
                p = true;
            }
            csv += random.nextDouble();
        }

        doubleData = new double[getSize()];
        for (int i = 0; i < getSize(); ++i) {
            doubleData[i] = random.nextDouble();
        }
    }

    @Benchmark
    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    public String stringConcat() {
        String string = "";
        for (String it : data) string = string + it;
        return string;
    }

    @Benchmark
    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    public String stringBuilderConcat() {
        StringBuilder string = new StringBuilder("");
        for (String it : data) string.append(it);
        return string.toString();
    }

    @Benchmark
    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    public double summarizeSplittedCsv() {
        String[] fields = csv.split(",");
        double sum = 0.0;
        for (String field: fields) {
            sum += Double.parseDouble(field);
        }
        return sum;
    }

    @Benchmark
    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    public void concatStringsWithDoubles(Blackhole bh) {
        for (int i = 0, size = getSize(); i < size; ++i) {
            bh.consume(data.get(i) + doubleData[i]);
        }
    }
}
