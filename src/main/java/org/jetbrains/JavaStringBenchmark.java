package org.jetbrains;

import org.openjdk.jmh.annotations.*;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class JavaStringBenchmark extends SizedBenchmark {
    public ArrayList<String> data;

    @Setup
    public void setup() {
        ArrayList<String> list = new ArrayList<String>();
        for (String item : JetbrainsPackage.stringValues(getSize())) {
            list.add(item);
        }
        data = list;
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
}
