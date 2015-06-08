package org.jetbrains

import org.openjdk.jmh.annotations.*
import java.util.concurrent.*
import org.openjdk.jmh.infra.Blackhole

BenchmarkMode(Mode.AverageTime)
OutputTimeUnit(TimeUnit.NANOSECONDS)
open class SwitchBenchmark : SizedBenchmark() {
    fun sparseIntSwitch(u : Int) : Int {
        var t : Int
        when (u) {
            1 -> {
                t = 1
            }
            100500 -> {
                t = 2
            }
            2 -> {
                t = 3
            }
            3 -> {
                t = 4
            }
            4 -> {
                t = 5
            }
            5 -> {
                t = 6
            }
            6 -> {
                t = 7
            }
            7 -> {
                t = 1
            }
            8 -> {
                t = 9
            }
            9 -> {
                t = 1
            }
            10 -> {
                t = 2
            }
            11 -> {
                t = 3
            }
            12 -> {
                t = 4
            }
            13 -> {
                t = 4
            }
            14 -> {
                t = 4
            }
            15 -> {
                t = 435
            }
            16 -> {
                t = 31
            }
            17 -> {
                t = 1
            }
            18 -> {
                t = 1
            }
            19 -> {
                t = 1
            }
            20 -> {
                t = 1
            }
            else -> {
                t = 5
            }
        }
        return t
    }

    fun denseIntSwitch(u : Int) : Int {
        var t : Int
        when (u) {
            1 -> {
                t = 1
            }
            -1 -> {
                t = 2
            }
            2 -> {
                t = 3
            }
            3 -> {
                t = 4
            }
            4 -> {
                t = 5
            }
            5 -> {
                t = 6
            }
            6 -> {
                t = 7
            }
            7 -> {
                t = 1
            }
            8 -> {
                t = 9
            }
            9 -> {
                t = 1
            }
            10 -> {
                t = 2
            }
            11 -> {
                t = 3
            }
            12 -> {
                t = 4
            }
            13 -> {
                t = 4
            }
            14 -> {
                t = 4
            }
            15 -> {
                t = 435
            }
            16 -> {
                t = 31
            }
            17 -> {
                t = 1
            }
            18 -> {
                t = 1
            }
            19 -> {
                t = 1
            }
            20 -> {
                t = 1
            }
            else -> {
                t = 5
            }
        }
        return t
    }

    fun stringSwitch(s: String) : Int {
        when(s) {
            "ABCDEFG1" -> return 1
            "ABCDEFG2" -> return 2
            "ABCDEFG2" -> return 3
            "ABCDEFG3" -> return 4
            "ABCDEFG4" -> return 5
            "ABCDEFG5" -> return 6
            "ABCDEFG6" -> return 7
            "ABCDEFG7" -> return 8
            "ABCDEFG8" -> return 9
            "ABCDEFG9" -> return 10
            "ABCDEFG10" -> return 11
            "ABCDEFG11" -> return 12
            "ABCDEFG12" -> return 1
            "ABCDEFG13" -> return 2
            "ABCDEFG14" -> return 3
            "ABCDEFG15" -> return 4
            "ABCDEFG16" -> return 5
            "ABCDEFG17" -> return 6
            "ABCDEFG18" -> return 7
            "ABCDEFG19" -> return 8
            "ABCDEFG20" -> return 9
            else -> return -1
        }
    }

    Benchmark fun testSparseIntSwitch(bh: Blackhole) {
        val n = size
        for (i in 0..n - 1) {
            bh.consume(denseIntSwitch(i))
        }
    }

    Benchmark fun testDenseIntSwitch(bh: Blackhole) {
        val n = size
        for (i in 0..n - 1) {
            bh.consume(denseIntSwitch(i))
        }
    }

    var data : Array<String> = arrayOf()

    Setup fun setupStrings() {
        data = Array(100) {
            "ABCDEFG$it"
        }
    }

    Benchmark fun testStringsSwitch(bh: Blackhole) {
        val n = data.size()
        for (i in 0..size) {
            bh.consume(stringSwitch(data[i % n]))
        }
    }

    enum class MyEnum {
        ITEM1, ITEM2, ITEM3, ITEM4, ITEM5, ITEM6, ITEM7, ITEM8, ITEM9, ITEM10, ITEM11, ITEM12, ITEM13, ITEM14, ITEM15, ITEM16, ITEM17, ITEM18, ITEM19, ITEM20, ITEM21, ITEM22, ITEM23, ITEM24, ITEM25, ITEM26, ITEM27, ITEM28, ITEM29, ITEM30, ITEM31, ITEM32, ITEM33, ITEM34, ITEM35, ITEM36, ITEM37, ITEM38, ITEM39, ITEM40, ITEM41, ITEM42, ITEM43, ITEM44, ITEM45, ITEM46, ITEM47, ITEM48, ITEM49, ITEM50, ITEM51, ITEM52, ITEM53, ITEM54, ITEM55, ITEM56, ITEM57, ITEM58, ITEM59, ITEM60, ITEM61, ITEM62, ITEM63, ITEM64, ITEM65, ITEM66, ITEM67, ITEM68, ITEM69, ITEM70, ITEM71, ITEM72, ITEM73, ITEM74, ITEM75, ITEM76, ITEM77, ITEM78, ITEM79, ITEM80, ITEM81, ITEM82, ITEM83, ITEM84, ITEM85, ITEM86, ITEM87, ITEM88, ITEM89, ITEM90, ITEM91, ITEM92, ITEM93, ITEM94, ITEM95, ITEM96, ITEM97, ITEM98, ITEM99, ITEM100
    }

    fun enumSwitch(x: MyEnum) : Int {
        when (x) {
            MyEnum.ITEM5 -> return 1
            MyEnum.ITEM10 -> return 2
            MyEnum.ITEM15 -> return 3
            MyEnum.ITEM20 -> return 4
            MyEnum.ITEM25 -> return 5
            MyEnum.ITEM30 -> return 6
            MyEnum.ITEM35 -> return 7
            MyEnum.ITEM40 -> return 8
            MyEnum.ITEM45 -> return 9
            MyEnum.ITEM50 -> return 10
            MyEnum.ITEM55 -> return 11
            MyEnum.ITEM60 -> return 12
            MyEnum.ITEM65 -> return 13
            MyEnum.ITEM70 -> return 14
            MyEnum.ITEM75 -> return 15
            MyEnum.ITEM80 -> return 16
            MyEnum.ITEM85 -> return 17
            MyEnum.ITEM90 -> return 18
            MyEnum.ITEM95 -> return 19
            MyEnum.ITEM100 -> return 20
            else -> return -1
        }
    }

    var enumData : Array<MyEnum> = arrayOf()

    Setup fun setupEnums() {
        enumData = Array(size) {
            MyEnum.values()[it % MyEnum.values().size()]
        }
    }

    Benchmark fun testEnumsSwitch(bh: Blackhole) {
        val n = enumData.size() -1
        val data = enumData
        for (i in 0..n) {
            bh.consume(enumSwitch(data[i]))
        }
    }
}
