package org.jetbrains

import org.openjdk.jmh.annotations.*
import org.openjdk.jmh.infra.Blackhole
import java.util.concurrent.TimeUnit
import kotlin.random.Random

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
open class ImplicitCheckCastBenchmark: SizedBenchmark() {
    interface Base {
        fun test() : Base
    }

    interface Derived: Base

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

    @JvmField
    val class1 = Class1()

    @JvmField
    val class2 = Class2()

    @JvmField
    var base: Base? = null


    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    open fun returnBase(): Class1 {
        return class1
    }

    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    open fun returnCheckCast(): Base {
        return if (Random.nextBoolean()) class2 else class1
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
            if (Random.nextBoolean()) {
                array[i] = class1
            }
            else {
                array[i] = class2
            }
            i--
        }
        return array
    }

    @Benchmark
    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    open fun fieldWrite(bh: Blackhole) {
        var i = size -1
        while (i >= 0) {
            if (Random.nextBoolean()) {
                base = class1
            }
            else {
                base = class2
            }
            bh.consume(base)
            i--
        }
    }
}