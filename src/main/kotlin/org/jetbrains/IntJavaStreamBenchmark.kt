package org.jetbrains

import org.openjdk.jmh.annotations.*
import java.util.concurrent.*
import java.util.stream.*
import org.openjdk.jmh.infra.Blackhole
import java.util.ArrayList

State(Scope.Thread)
BenchmarkMode(Mode.Throughput)
OutputTimeUnit(TimeUnit.SECONDS)
open class IntJavaStreamBenchmark {
    val list: ArrayList<Int> = ArrayList(intValues().toList())
    val getStreamMethod = list.javaClass.getMethod("stream")
    val data: Stream<Int>
        get() = getStreamMethod.invoke(list) as Stream<Int>

    Benchmark fun filterAndCount(bh: Blackhole) {
        val stream = data.filter(predicate<Int>({ it % 2 == 0 }))!!
        bh.consume(stream.count())
    }

    Benchmark fun countFiltered(bh: Blackhole) {
        bh.consume(data.mapToLong(longFunction({ if (it % 2 == 0) 1L else 0L }))!!.sum())
    }

    Benchmark fun countFilteredWithValue(bh: Blackhole) {
        val value = data.mapToLong(longFunction({ if (it % 2 == 0) 1L else 0L }))!!.sum()
        bh.consume(value)
    }

    Benchmark fun filterAndMap(bh: Blackhole) {
        val stream = data.filter(predicate<Int>({ it % 2 == 0 }))!!.map(function<Int, Int>({ it * 10 }))!!
        stream.forEach(consume({ bh.consume(it) }))
    }
}
