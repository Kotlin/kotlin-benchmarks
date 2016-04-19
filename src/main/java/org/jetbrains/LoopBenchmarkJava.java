package org.jetbrains;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@CompilerControl(CompilerControl.Mode.DONT_INLINE)
public class LoopBenchmarkJava extends SizedBenchmark {
  public ArrayList<Value> arrayList;
  public Value[] array;

  @Setup
  public void setUp() {
    arrayList = new ArrayList<Value>();
    for (Value item : DataKt.classValues(getSize())) {
      arrayList.add(item);
    }
    array = arrayList.toArray(new Value[0]);
  }

  @Benchmark
  public void arrayForLoop(Blackhole bh) {
    for (int i=0; i<array.length; i++) {
      bh.consume(array[i]);
    }
  }

  @Benchmark
  public void arrayForeachLoop(Blackhole bh) {
    for (Value v: array) {
      bh.consume(v);
    }
  }

  @Benchmark
  public void arrayListForLoop(Blackhole bh) {
    for (int i=0; i<arrayList.size(); i++) {
      bh.consume(arrayList.get(i));
    }
  }

  @Benchmark
  public void arrayListForeachLoop(Blackhole bh) {
    for (Value v: arrayList) {
      bh.consume(v);
    }
  }
}
