package org.jetbrains

import org.openjdk.jmh.annotations.*
import java.util.concurrent.*

State(Scope.Thread)
BenchmarkMode(Mode.AverageTime)
OutputTimeUnit(TimeUnit.MICROSECONDS)
Warmup(iterations = 5)
Measurement(iterations = 5)
Fork(2)
open class CollectionsBenchmark {

    val list = arrayListOf<Int>();
    {
        for (index in 0..100000)
            list.add(index)
    }

    Benchmark fun filterAndCount(): Int {
        return list.filter { it % 2 == 0 }.count()
    }

    Benchmark fun countFiltered(): Int {
        return list.count { it % 2 == 0 }
    }

    Benchmark fun streamFilterAndCount(): Int {
        return list.stream().filter { it % 2 == 0 }.count()
    }

    Benchmark fun streamCountFiltered(): Int {
        return list.stream().count { it % 2 == 0 }
    }

}
