package org.jetbrains

import org.openjdk.jmh.annotations.*
import org.openjdk.jmh.infra.Blackhole
import java.util.concurrent.ThreadLocalRandom
import java.util.concurrent.TimeUnit
import kotlin.random.Random

@JvmInline
private value class DPointMfvc(val x: Double, val y: Double)

private data class DPointRegular(val x: Double, val y: Double)

@JvmInline
private value class OuterDPointMfvc(val value: DPointMfvc)

private data class OuterDPointRegular(val value: DPointRegular)


@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@CompilerControl(CompilerControl.Mode.DONT_INLINE)
@State(Scope.Thread)
open class ValueClassesGenerating {
    private var flag = false
    private var xD = 0.0
    private var yD = 0.0
    private var savedInstanceMfvc: DPointMfvc = DPointMfvc(1.0, 2.0)
    private var savedInstanceRegular: DPointRegular = DPointRegular(1.0, 2.0)

    @Setup(Level.Iteration)
    fun shake() {
        flag = ThreadLocalRandom.current().nextBoolean()
        xD++
        yD++
    }

    @Benchmark
    fun generateDPointsMfvc(bh: Blackhole) {
        val point = DPointMfvc(xD, yD)
        bh.consume(point.x)
        bh.consume(point.y)
    }


    @Benchmark
    fun generateDPointsRegular(bh: Blackhole) {
        val point = DPointRegular(xD, yD)
        bh.consume(point.x)
        bh.consume(point.y)
    }

    @Benchmark
    fun generateEscapingDPointsMfvc(bh: Blackhole) {
        val point = DPointMfvc(xD, yD)
        savedInstanceMfvc = point
        bh.consume(point.x)
        bh.consume(point.y)
    }

    @Benchmark
    fun generateEscapingDPointsRegular(bh: Blackhole) {
        val point = DPointRegular(xD, yD)
        savedInstanceRegular = point
        bh.consume(point.x)
        bh.consume(point.y)
    }

    private var savedInstanceOuterMfvc: OuterDPointMfvc = OuterDPointMfvc(savedInstanceMfvc)
    private var savedInstanceOuterRegular: OuterDPointRegular = OuterDPointRegular(savedInstanceRegular)

    @Benchmark
    fun generateOuterDPointsMfvc(bh: Blackhole) {
        val point = OuterDPointMfvc(DPointMfvc(xD, yD))
        bh.consume(point.value.x)
        bh.consume(point.value.y)
    }


    @Benchmark
    fun generateOuterDPointsRegular(bh: Blackhole) {
        val point = OuterDPointRegular(DPointRegular(xD, yD))
        bh.consume(point.value.x)
        bh.consume(point.value.y)
    }

    @Benchmark
    fun generateEscapingOuterDPointsMfvc(bh: Blackhole) {
        val point = OuterDPointMfvc(DPointMfvc(xD, yD))
        savedInstanceOuterMfvc = point
        bh.consume(point.value.x)
        bh.consume(point.value.y)
    }

    @Benchmark
    fun generateEscapingOuterDPointsRegular(bh: Blackhole) {
        val point = OuterDPointRegular(DPointRegular(xD, yD))
        savedInstanceOuterRegular = point
        bh.consume(point.value.x)
        bh.consume(point.value.y)
    }
}

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@CompilerControl(CompilerControl.Mode.DONT_INLINE)
@State(Scope.Benchmark)
open class ValueClassesComplexUsageDouble : SizedBenchmark() {

    val n
        get() = size.toDouble()

    @Benchmark
    fun searchMfvc(bh: Blackhole) {
        var p = DPointMfvc(n, n)
        while (p.x != 1.0 && p.y != 1.0) {
            p = DPointMfvc(
                if (p.x % 2 == 0.0) p.x / 2 else 3 * p.x + 1,
                if (p.y % 2 == 0.0) p.y / 2 else 3 * p.y + 1,
            )
        }
        bh.consume(p.x)
        bh.consume(p.y)
    }

    @Benchmark
    fun searchRegular(bh: Blackhole) {
        var p = DPointRegular(n, n)
        while (p.x != 1.0 && p.y != 1.0) {
            p = DPointRegular(
                if (p.x % 2 == 0.0) p.x / 2 else 3 * p.x + 1,
                if (p.y % 2 == 0.0) p.y / 2 else 3 * p.y + 1,
            )
        }
        bh.consume(p.x)
        bh.consume(p.y)
    }

    @Benchmark
    fun searchMfvcOuter(bh: Blackhole) {
        var p = OuterDPointMfvc(DPointMfvc(n, n))
        while (p.value.x != 1.0 && p.value.y != 1.0) {
            p = OuterDPointMfvc(
                DPointMfvc(
                    if (p.value.x % 2 == 0.0) p.value.x / 2 else 3 * p.value.x + 1,
                    if (p.value.y % 2 == 0.0) p.value.y / 2 else 3 * p.value.y + 1,
                )
            )
        }
        bh.consume(p.value.x)
        bh.consume(p.value.y)
    }

    @Benchmark
    fun searchRegularOuter(bh: Blackhole) {
        var p = OuterDPointRegular(DPointRegular(n, n))
        while (p.value.x != 1.0 && p.value.y != 1.0) {
            p = OuterDPointRegular(
                DPointRegular(
                    if (p.value.x % 2 == 0.0) p.value.x / 2 else 3 * p.value.x + 1,
                    if (p.value.y % 2 == 0.0) p.value.y / 2 else 3 * p.value.y + 1,
                )
            )
        }
        bh.consume(p.value.x)
        bh.consume(p.value.y)
    }
}

@Suppress("DuplicatedCode")
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@CompilerControl(CompilerControl.Mode.DONT_INLINE)
@State(Scope.Benchmark)
open class ValueClassesBoxUsageDouble {
    private var x = 1.0
    private var y = 2.0
    private val count = 10

    @Setup(Level.Iteration)
    fun setup() {
        x = Random.nextDouble()
        y = Random.nextDouble()
    }

    @Benchmark
    fun regularNotUsingLocally(bh: Blackhole) {
        val p = DPointRegular(x, y)
        repeat(count) {
            bh.consume(p.x)
            bh.consume(p.y)
        }
    }

    @Benchmark
    fun regularUsingLocally(bh: Blackhole) {
        val p = DPointRegular(x, y)
        repeat(count) {
            bh.consume(p)
            bh.consume(p.x)
            bh.consume(p.y)
        }
    }

    @Benchmark
    fun regularUsingPassed(bh: Blackhole) {
        val p = DPointRegular(x, y)
        repeat(count) {
            usePassedBox(bh, p)
        }
    }

    @Benchmark
    fun regularNotUsingPassed(bh: Blackhole) {
        val p = DPointRegular(x, y)
        repeat(count) {
            doNotUsePassedBox(bh, p)
        }
    }

    @Benchmark
    fun mfvcNotUsingLocally(bh: Blackhole) {
        val p = DPointMfvc(x, y)
        repeat(count) {
            bh.consume(p.x)
            bh.consume(p.y)
        }
    }

    @Benchmark
    fun mfvcUsingLocally(bh: Blackhole) {
        val p = DPointMfvc(x, y)
        repeat(count) {
            bh.consume(p)
            bh.consume(p.x)
            bh.consume(p.y)
        }
    }

    @Benchmark
    fun mfvcUsingPassed(bh: Blackhole) {
        repeat(count) {
            usePassedBox(bh, DPointMfvc(x, y))
        }
    }

    @Benchmark
    fun mfvcNotUsingPassed(bh: Blackhole) {
        repeat(count) {
            doNotUsePassedBox(bh, DPointMfvc(x, y))
        }
    }

    @Benchmark
    fun mfvcSmartNotUsingLocally(bh: Blackhole) {
        val p = DPointMfvc(x, y)
        repeat(count) {
            bh.consume(p.x)
            bh.consume(p.y)
        }
    }

    @Benchmark
    fun mfvcSmartUsingLocally(bh: Blackhole) {
        val p = DPointMfvc(x, y)
        val pointOrNull = p as DPointMfvc?
        repeat(count) {
            bh.consume(pointOrNull)
            bh.consume(p.x)
            bh.consume(p.y)
        }
    }

    @Benchmark
    fun mfvcSmartUsingPassed(bh: Blackhole) {
        val p = DPointMfvc(x, y)
        val pointOrNull = p as DPointMfvc?
        repeat(count) {
            usePassedBox(bh, pointOrNull, p)
        }
    }

    @Benchmark
    fun mfvcSmartNotUsingPassed(bh: Blackhole) {
        val p = DPointMfvc(x, y)
        val pointOrNull = p as DPointMfvc?
        repeat(count) {
            doNotUsePassedBox(bh, pointOrNull, p)
        }
    }

    private fun usePassedBox(bh: Blackhole, box: DPointMfvc?, point: DPointMfvc) {
        bh.consume(box ?: point)
        bh.consume(point.x)
        bh.consume(point.y)
    }

    private fun doNotUsePassedBox(bh: Blackhole, @Suppress("UNUSED_PARAMETER") box: DPointMfvc?, point: DPointMfvc) {
        bh.consume(point.x)
        bh.consume(point.y)
    }

    private fun usePassedBox(bh: Blackhole, point: DPointMfvc) {
        bh.consume(point)
        bh.consume(point.x)
        bh.consume(point.y)
    }

    private fun doNotUsePassedBox(bh: Blackhole, point: DPointMfvc) {
        bh.consume(point.x)
        bh.consume(point.y)
    }

    private fun usePassedBox(bh: Blackhole, point: DPointRegular) {
        bh.consume(point)
        bh.consume(point.x)
        bh.consume(point.y)
    }

    private fun doNotUsePassedBox(bh: Blackhole, point: DPointRegular) {
        bh.consume(point.x)
        bh.consume(point.y)
    }
}

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@CompilerControl(CompilerControl.Mode.DONT_INLINE)
@State(Scope.Benchmark)
open class ValueClassesInlineFunctionsUsageDouble {
    private var dPointRegular = DPointRegular(1.0, 2.0)
    private var dPointMfvc = DPointMfvc(1.0, 2.0)

    @Benchmark
    fun regular(bh: Blackhole) {
        dPointRegular = dPointRegular
            .let { DPointRegular(it.y, it.x) }
            .let { DPointRegular(it.x * 2, it.y * 2) }
            .let { DPointRegular(it.y, it.x) }
            .let { DPointRegular(it.x / 2, it.y / 2) }
            .let { DPointRegular(it.y, it.x) }
        bh.consume(dPointRegular.x)
        bh.consume(dPointRegular.y)
    }

    @Benchmark
    fun mfvc(bh: Blackhole) {
        dPointMfvc = dPointMfvc
            .let { DPointMfvc(it.y, it.x) }
            .let { DPointMfvc(it.x * 2, it.y * 2) }
            .let { DPointMfvc(it.y, it.x) }
            .let { DPointMfvc(it.x / 2, it.y / 2) }
            .let { DPointMfvc(it.y, it.x) }
        bh.consume(dPointMfvc.x)
        bh.consume(dPointMfvc.y)
    }
}

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@CompilerControl(CompilerControl.Mode.DONT_INLINE)
@State(Scope.Benchmark)
open class ValueClassesLongPack {
    private var float1 = 0.0f
    private var float2 = 0.0f

    private data class FloatPair(val f1: Float, val f2: Float)
    private data class Wrapper(var l1: Long, var l2: Long, var l3: Long, var l4: Long)
    private val wrapper = Wrapper(0, 0, 0, 0)

    private fun longPacked(): Long = float1.toRawBits().toLong().shl(32).or(float2.toRawBits().toLong())
    private fun boxPacked(): FloatPair = FloatPair(float1, float2)
    private fun wrapperPacked(wrapper: Wrapper) {
        wrapper.l1 = float1.toRawBits().toLong()
        wrapper.l2 = float2.toRawBits().toLong()
    }

    @Setup(Level.Iteration)
    fun setup() {
        float1 = Random.nextFloat()
        float2 = Random.nextFloat()
    }

    @Benchmark
    @JvmName("long_")
    fun long(bh: Blackhole) {
        val long = longPacked()
        bh.consume(Float.fromBits(long.shr(32).toInt()))
        bh.consume(Float.fromBits(long.toInt()))
    }

    @Benchmark
    fun box(bh: Blackhole) {
        val box = boxPacked()
        bh.consume(box.f1)
        bh.consume(box.f2)
    }

    @Benchmark
    fun existingWrapper(bh: Blackhole) {
        val w = wrapper
        wrapperPacked(w)
        bh.consume(Float.fromBits(w.l1.toInt()))
        bh.consume(Float.fromBits(w.l2.toInt()))
    }
}
