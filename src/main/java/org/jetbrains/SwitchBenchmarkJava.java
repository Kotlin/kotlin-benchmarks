package org.jetbrains;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@CompilerControl(CompilerControl.Mode.DONT_INLINE)
public class SwitchBenchmarkJava extends SizedBenchmark {
  public static final int V1 = 1;
  public static final int V2 = 2;
  public static final int V3 = 3;
  public static final int V4 = 4;
  public static final int V5 = 5;
  public static final int V6 = 6;
  public static final int V7 = 7;
  public static final int V8 = 8;
  public static final int V9 = 9;
  public static final int V10 = 10;
  public static final int V11 = 11;
  public static final int V12 = 12;
  public static final int V13 = 13;
  public static final int V14 = 14;
  public static final int V15 = 15;
  public static final int V16 = 16;
  public static final int V17 = 17;
  public static final int V18 = 18;
  public static final int V19 = 19;
  public static final int V20 = 20;

  public final int sparseIntSwitch(int i) {
    int t;
    switch (i) {
      case 11: t = 1; break;
      case 29: t = 2; break;
      case 47: t = 3; break;
      case 71: t = 4; break;
      case 103: t = 5; break;
      case 149: t = 6; break;
      case 175: t = 7; break;
      case 227: t = 1; break;
      case 263: t = 9; break;
      case 307: t = 1; break;
      case 361: t = 2; break;
      case 487: t = 3; break;
      case 563: t = 4; break;
      case 617: t = 4; break;
      case 677: t = 4; break;
      case 751: t = 435; break;
      case 823: t = 31; break;
      case 883: t = 1; break;
      case 967: t = 1; break;
      case 1031: t = 1; break;
      case 20: t = 1; break;
      default: t = 5;
    }
    return t;
  }

  public final int denseIntSwitch(int i) {
    int t;
    switch (i) {
      case 1: t = 1; break;
      case -1: t = 2; break;
      case 2: t = 3; break;
      case 3: t = 4; break;
      case 4: t = 5; break;
      case 5: t = 6; break;
      case 6: t = 7; break;
      case 7: t = 1; break;
      case 8: t = 9; break;
      case 9: t = 1; break;
      case 10: t = 2; break;
      case 11: t = 3; break;
      case 12: t = 4; break;
      case 13: t = 4; break;
      case 14: t = 4; break;
      case 15: t = 435; break;
      case 16: t = 31; break;
      case 17: t = 1; break;
      case 18: t = 1; break;
      case 19: t = 1; break;
      case 20: t = 1; break;
      default: t = 5;
    }
    return t;
  }

  public final int constSwitch(int i) {
    int t;
    switch (i) {
      case V1: t = 1; break;
      case V2: t = 3; break;
      case V3: t = 4; break;
      case V4: t = 5; break;
      case V5: t = 6; break;
      case V6: t = 7; break;
      case V7: t = 1; break;
      case V8: t = 9; break;
      case V9: t = 1; break;
      case V10: t = 2; break;
      case V11: t = 3; break;
      case V12: t = 4; break;
      case V13: t = 4; break;
      case V14: t = 4; break;
      case V15: t = 435; break;
      case V16: t = 31; break;
      case V17: t = 1; break;
      case V18: t = 1; break;
      case V19: t = 1; break;
      case V20: t = 1; break;
      default: t = 5;
    }
    return t;
  }

  int[] denseIntData;
  int[] sparseIntData;

  @Setup public final void initInts() {
    denseIntData = new int[getSize()];
    for (int i=0; i<denseIntData.length; i++) {
      denseIntData[i] = ThreadLocalRandom.current().nextInt(25) - 1;
    }

    sparseIntData = new int[getSize()];
    for (int i=0; i<sparseIntData.length; i++) {
      sparseIntData[i] = SwitchBenchmarkKt.getSPARSE_SWITCH_CASES()[ThreadLocalRandom.current().nextInt(20)];
    }
  }

  @Benchmark
  public final void testSparseIntSwitch(Blackhole bh) {
    for (int i: sparseIntData) {
      bh.consume(sparseIntSwitch(i));
    }
  }

  @Benchmark
  public final void testDenseIntSwitch(Blackhole bh) {
    for (int i: denseIntData) {
      bh.consume(denseIntSwitch(i));
    }
  }

  @Benchmark
  public final void testConstSwitch(Blackhole bh) {
    for (int i : denseIntData) {
      bh.consume(constSwitch(i));
    }
  }

  public final int stringSwitch(String s) {
    switch (s) {
      case "ABCDEFG1":
        return 1;
      case "ABCDEFG2":
        return 3;
      case "ABCDEFG3":
        return 4;
      case "ABCDEFG4":
        return 5;
      case "ABCDEFG5":
        return 6;
      case "ABCDEFG6":
        return 7;
      case "ABCDEFG7":
        return 8;
      case "ABCDEFG8":
        return 9;
      case "ABCDEFG9":
        return 10;
      case "ABCDEFG10":
        return 11;
      case "ABCDEFG11":
        return 12;
      case "ABCDEFG12":
        return 1;
      case "ABCDEFG13":
        return 2;
      case "ABCDEFG14":
        return 3;
      case "ABCDEFG15":
        return 4;
      case "ABCDEFG16":
        return 5;
      case "ABCDEFG17":
        return 6;
      case "ABCDEFG18":
        return 7;
      case "ABCDEFG19":
        return 8;
      case "ABCDEFG20":
        return 9;
      default:
        return -1;
    }
  }

  private String[] data;

  @Setup
  public void setupStrings() {
    Random random = new Random();
    data = new String[getSize()];
    for (int i=0; i<getSize(); i++) {
      data[i] = "ABCDEFG" + random.nextInt(22);
    }
  }

  @Benchmark public final void testStringsSwitch(Blackhole bh) {
    int size = getSize();
    for (int i = 0; i < size; i++) {
      bh.consume(stringSwitch(data[i]));
    }
  }

  enum MyEnum {
    ITEM1, ITEM2, ITEM3, ITEM4, ITEM5, ITEM6, ITEM7, ITEM8, ITEM9, ITEM10, ITEM11, ITEM12, ITEM13, ITEM14, ITEM15, ITEM16, ITEM17, ITEM18, ITEM19, ITEM20, ITEM21, ITEM22, ITEM23, ITEM24, ITEM25, ITEM26, ITEM27, ITEM28, ITEM29, ITEM30, ITEM31, ITEM32, ITEM33, ITEM34, ITEM35, ITEM36, ITEM37, ITEM38, ITEM39, ITEM40, ITEM41, ITEM42, ITEM43, ITEM44, ITEM45, ITEM46, ITEM47, ITEM48, ITEM49, ITEM50, ITEM51, ITEM52, ITEM53, ITEM54, ITEM55, ITEM56, ITEM57, ITEM58, ITEM59, ITEM60, ITEM61, ITEM62, ITEM63, ITEM64, ITEM65, ITEM66, ITEM67, ITEM68, ITEM69, ITEM70, ITEM71, ITEM72, ITEM73, ITEM74, ITEM75, ITEM76, ITEM77, ITEM78, ITEM79, ITEM80, ITEM81, ITEM82, ITEM83, ITEM84, ITEM85, ITEM86, ITEM87, ITEM88, ITEM89, ITEM90, ITEM91, ITEM92, ITEM93, ITEM94, ITEM95, ITEM96, ITEM97, ITEM98, ITEM99, ITEM100
  }

  @CompilerControl(CompilerControl.Mode.DONT_INLINE)
  public final int enumSwitch(MyEnum x) {
    switch (x) {
      case ITEM5: return 1;
      case ITEM10: return 2;
      case ITEM15: return 3;
      case ITEM20: return 4;
      case ITEM25: return 5;
      case ITEM30: return 6;
      case ITEM35: return 7;
      case ITEM40: return 8;
      case ITEM45: return 9;
      case ITEM50: return 10;
      case ITEM55: return 11;
      case ITEM60: return 12;
      case ITEM65: return 13;
      case ITEM70: return 14;
      case ITEM75: return 15;
      case ITEM80: return 16;
      case ITEM85: return 17;
      case ITEM90: return 18;
      case ITEM95: return 19;
      case ITEM100: return 20;
      default: return -1;
    }
  }

  @CompilerControl(CompilerControl.Mode.DONT_INLINE)
  public final int denseEnumSwitch(MyEnum x) {
    switch (x) {
      case ITEM1: return 1;
      case ITEM2: return 2;
      case ITEM3: return 3;
      case ITEM4: return 4;
      case ITEM5: return 5;
      case ITEM6: return 6;
      case ITEM7: return 7;
      case ITEM8: return 8;
      case ITEM9: return 9;
      case ITEM10: return 10;
      case ITEM11: return 11;
      case ITEM12: return 12;
      case ITEM13: return 13;
      case ITEM14: return 14;
      case ITEM15: return 15;
      case ITEM16: return 16;
      case ITEM17: return 17;
      case ITEM18: return 18;
      case ITEM19: return 19;
      case ITEM20: return 20;
      default: return -1;
    }
  }

  private MyEnum[] enumData;
  private MyEnum[] denseEnumData;

  @Setup public void setupEnums() {
    enumData = new MyEnum[getSize()];
    denseEnumData = new MyEnum[getSize()];
    for (int i = 0; i < getSize(); i++) {
      enumData[i] = MyEnum.values()[i % MyEnum.values().length];
      denseEnumData[i] = MyEnum.values()[i % 20];
    }
  }

  @Benchmark public final void testEnumsSwitch(Blackhole bh) {
    int n = enumData.length;
    MyEnum[] data = enumData;
    for (int i=0; i < n; i++) {
      bh.consume(enumSwitch(data[i]));
    }
  }

  @Benchmark public final void testDenseEnumsSwitch(Blackhole bh) {
    int n = denseEnumData.length;
    MyEnum[] data = denseEnumData;
    for (int i=0; i < n; i++) {
      bh.consume(denseEnumSwitch(data[i]));
    }
  }
}
