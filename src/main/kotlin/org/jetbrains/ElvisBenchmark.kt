package org.jetbrains

import org.openjdk.jmh.annotations.*
import java.util.concurrent.*
import org.openjdk.jmh.infra.Blackhole
import java.util.Random

BenchmarkMode(Mode.AverageTime)
OutputTimeUnit(TimeUnit.NANOSECONDS)
open public class ElvisBenchmark : SizedBenchmark() {

    class Value(var value: Int)

    public var array : Array<Value?> = arrayOf()


    @Setup
    public fun init() {
        val random = Random(123)

        array = Array<Value?>(size) {
            if (random.nextInt(size) < size / 10) null else Value(random.nextInt())
        }
    }

    Benchmark fun testElvis(bh: Blackhole) {
        for (obj in array) {
            bh.consume(obj?.value ?: 0)
        }
    }
}
