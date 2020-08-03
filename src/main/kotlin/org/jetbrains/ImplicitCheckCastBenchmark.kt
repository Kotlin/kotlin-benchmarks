package org.jetbrains

import org.openjdk.jmh.annotations.*
import org.openjdk.jmh.infra.Blackhole
import java.util.concurrent.TimeUnit

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
open class ImplicitCheckCastBenchmark: SizedBenchmark() {
    interface Base {
        fun test() : Base
    }
    interface Derived: Base {
    }
    val state = Class1()
    val state2 = Class2()

    @JvmField
    var base: Base? = null

    open class Class1 : Derived {
        override fun test(): Base {
            return this
        }
    }

    open class Class2 : Derived {
        override fun test(): Base {
            return this
        }
    }


    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    open fun returnBase(): Class1 {
        return state
    }

    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    open fun returnCheckCast(): Base {
        return state2
    }


    @Benchmark
    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    open fun testReturnBase(bh: Blackhole) {
        var i = size
        while (i > 0) {
            bh.consume(returnBase().test())
            i--
        }
    }

    @Benchmark
    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    open fun testReturnCheckCast(bh: Blackhole) {
        var i = size
        while (i > 0) {
            bh.consume(returnCheckCast().test())
            i--
        }
    }

    @Benchmark
    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    open fun testArrayInit():Array<Base?> {
        val array = arrayOfNulls<Base>(size)
        var i = size -1
        while (i >= 0) {
            array[i] = Class1()
            i--
        }
        return array
    }

    @Benchmark
    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    open fun fieldWrite(bh: Blackhole) {
        var i = size -1
        while (i >= 0) {
            base = Class1()
            bh.consume(base)
            i--
        }
    }
}