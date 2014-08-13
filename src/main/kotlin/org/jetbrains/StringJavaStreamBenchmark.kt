package org.jetbrains

import org.openjdk.jmh.annotations.*
import java.util.concurrent.*
import java.util.stream.*
import org.openjdk.jmh.infra.Blackhole
import java.util.ArrayList

State(Scope.Thread)
BenchmarkMode(Mode.Throughput)
OutputTimeUnit(TimeUnit.SECONDS)
open class StringJavaStreamBenchmark {
    val list: ArrayList<String> = ArrayList(stringValues().toList())
    val getStreamMethod = list.javaClass.getMethod("stream")
    val data: Stream<String>
        get() = getStreamMethod.invoke(list) as Stream<String>

    Benchmark fun filterAndCount(bh: Blackhole) {
        val stream = data.filter(predicate<String>({ it.length % 2 == 0 }))!!
        bh.consume(stream.count())
    }

    Benchmark fun countFiltered(bh: Blackhole) {
        val stream = data.mapToLong(longFunction({ if (it.length % 2 == 0) 1L else 0L }))!!
        bh.consume(stream.sum())
    }

    Benchmark fun filterAndMap(bh: Blackhole) {
        val stream = data.filter(predicate<String>({ it.length % 2 == 0 }))!!.map(function<String, String>({ it + "x" }))!!
        stream.forEach(consume({ bh.consume(it) }))
    }
}
