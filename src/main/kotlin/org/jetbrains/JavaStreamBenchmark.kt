package org.jetbrains

import org.openjdk.jmh.annotations.*
import java.util.concurrent.*
import java.util.stream.*
import org.openjdk.jmh.infra.Blackhole
import java.util.ArrayList
import java.util.function.*

State(Scope.Thread)
BenchmarkMode(Mode.Throughput)
OutputTimeUnit(TimeUnit.SECONDS)
Warmup(iterations = 5)
Measurement(iterations = 5)
Fork(2)
open class JavaStreamBenchmark {
    val list: ArrayList<Int> = ArrayList(values().toList())
    val getStreamMethod = list.javaClass.getMethod("stream")
    val data: Stream<Int>
        get() = getStreamMethod.invoke(list) as Stream<Int>

    Benchmark fun filterAndCount(bh: Blackhole) {
        val stream = data.filter(predicate<Int>({ it % 2 == 0 }))!!
        bh.consume(stream.count())
    }

    Benchmark fun filterAndMap(bh: Blackhole) {
        val stream = data.filter(predicate<Int>({ it % 2 == 0 }))!!.map(function<Int, Int>({ it * 10 }))!!
        stream.forEach(consume({ bh.consume(it) }))
    }
}

fun predicate<T : Any>(f: (T) -> Boolean) = object : Predicate<T> {
    override fun test(t: T?): Boolean {
        return f(t!!)
    }

}

fun function<T : Any, R : Any>(f: (T) -> R) = object : Function<T, R> {
    override fun apply(t: T?): R? {
        return f(t!!)
    }
}

fun consume<T : Any>(f: (T) -> Unit) = object : Consumer<T> {
    override fun accept(t: T?) {
        return f(t!!)
    }
}