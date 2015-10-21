package org.jetbrains;

import org.openjdk.jmh.annotations.*;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class StringBenchmarkJava extends SizedBenchmark {
    public ArrayList<String> data;

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
        for (int i=0; i<getSize(); i++) {
            csv += random.nextDouble();
            csv += ",";
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
}
