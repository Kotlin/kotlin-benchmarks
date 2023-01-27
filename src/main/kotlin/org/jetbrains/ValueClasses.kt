package org.jetbrains

import org.openjdk.jmh.annotations.*
import org.openjdk.jmh.infra.Blackhole
import java.util.concurrent.ThreadLocalRandom
import java.util.concurrent.TimeUnit

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
open class ValueClassesComplexUsageDouble: SizedBenchmark() {

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

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@CompilerControl(CompilerControl.Mode.DONT_INLINE)
@State(Scope.Benchmark)
open class ValueClassesBoxUsageDouble: SmallSizedBenchmark() {
    @Benchmark
    fun regular(bh: Blackhole) {
        var point = DPointRegular(1.0, 2.0)
        val list = Array(1) { point }
        repeat(smallSize) {
            list[0] = point
            point = list[0]
        }
        bh.consume(point)
    }
    @Benchmark
    fun mfvc(bh: Blackhole) {
        var point = DPointMfvc(1.0, 2.0)
        val list = Array(1) { point }
        repeat(smallSize) {
            list[0] = point
            point = list[0]
        }
        bh.consume(point)
    }
    @Benchmark
    fun mfvcSmart(bh: Blackhole) {
        var point = DPointMfvc(1.0, 2.0)
        var pointOrNull: DPointMfvc? = point
        val list = Array(1) {
            if (pointOrNull == null) pointOrNull = point
            pointOrNull
        }
        repeat(smallSize) {
            if (pointOrNull == null) pointOrNull = point
            list[0] = pointOrNull
            pointOrNull = list[0]
            point = pointOrNull!!
        }
        bh.consume(pointOrNull)
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
