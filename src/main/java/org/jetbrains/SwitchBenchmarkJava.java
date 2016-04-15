package org.jetbrains;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class SwitchBenchmarkJava extends SizedBenchmark {
  @CompilerControl(CompilerControl.Mode.DONT_INLINE)
  int stringSwitch(String s) {
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

  @Benchmark public void testStringsSwitch(Blackhole bh) {
    for (int i = 0; i < getSize(); i++) {
      bh.consume(stringSwitch(data[i]));
    }
  }

  enum MyEnum {
    ITEM1, ITEM2, ITEM3, ITEM4, ITEM5, ITEM6, ITEM7, ITEM8, ITEM9, ITEM10, ITEM11, ITEM12, ITEM13, ITEM14, ITEM15, ITEM16, ITEM17, ITEM18, ITEM19, ITEM20, ITEM21, ITEM22, ITEM23, ITEM24, ITEM25, ITEM26, ITEM27, ITEM28, ITEM29, ITEM30, ITEM31, ITEM32, ITEM33, ITEM34, ITEM35, ITEM36, ITEM37, ITEM38, ITEM39, ITEM40, ITEM41, ITEM42, ITEM43, ITEM44, ITEM45, ITEM46, ITEM47, ITEM48, ITEM49, ITEM50, ITEM51, ITEM52, ITEM53, ITEM54, ITEM55, ITEM56, ITEM57, ITEM58, ITEM59, ITEM60, ITEM61, ITEM62, ITEM63, ITEM64, ITEM65, ITEM66, ITEM67, ITEM68, ITEM69, ITEM70, ITEM71, ITEM72, ITEM73, ITEM74, ITEM75, ITEM76, ITEM77, ITEM78, ITEM79, ITEM80, ITEM81, ITEM82, ITEM83, ITEM84, ITEM85, ITEM86, ITEM87, ITEM88, ITEM89, ITEM90, ITEM91, ITEM92, ITEM93, ITEM94, ITEM95, ITEM96, ITEM97, ITEM98, ITEM99, ITEM100
  }

  @CompilerControl(CompilerControl.Mode.DONT_INLINE)
  int enumSwitch(MyEnum x) {
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

  private MyEnum[] enumData;

  @Setup public void setupEnums() {
    enumData = new MyEnum[getSize()];
    for (int i = 0; i < getSize(); i++) {
      enumData[i] = MyEnum.values()[i % MyEnum.values().length];
    }
  }

  @Benchmark public void testEnumsSwitch(Blackhole bh) {
    int n = enumData.length - 1;
    MyEnum[] data = enumData;
    for (int i=0; i <= n; i++) {
      bh.consume(enumSwitch(data[i]));
    }
  }
}
