package org.jetbrains

import org.openjdk.jmh.annotations.*
import org.openjdk.jmh.infra.Blackhole
import java.util.concurrent.TimeUnit

// Point in 2D space with integer coordinates
data class IntPoint2D(val x: Int, val y: Int)

// Rectangle in 2D space with integer coordinates
data class Rectangle(val a: IntPoint2D, val b: IntPoint2D)

// Point in 3D space with Double coordinates
data class Point3D(val x: Double, val y: Double, val z: Double)

// Triangle in 3D space with associated Boolean flag and Float weight
data class Triangle(val p1: Point3D, val p2: Point3D, val p3: Point3D, val flag: Boolean, val weight: Float)

class WrapperPerType(
        @JvmField val booleans: BooleanArray,
        @JvmField val bytes: ByteArray,
        @JvmField val shorts: ShortArray,
        @JvmField val ints: IntArray,
        @JvmField val longs: LongArray,
        @JvmField val floats: FloatArray,
        @JvmField val doubles: DoubleArray,
        @JvmField val chars: CharArray,
        @JvmField val refs: Array<Any?>
)

class WrapperPerSize(
        @JvmField val ones: ByteArray,
        @JvmField val twos: ShortArray,
        @JvmField val fours: IntArray,
        @JvmField val eights: LongArray,
        @JvmField val refs: Array<Any?>
)

class WrapperTwoArrays(val primitives: LongArray, refs: Array<Any?>)

class IntSource {
    private var current: Int = 0
    fun next() = current++
}

class FloatSource {
    private var current: Float = 0.0f
    fun next(): Float {
        current += 0.5f
        return current
    }
}

class DoubleSource {
    private var current: Double = 0.0
    fun next(): Double {
        current += 0.5
        return current
    }
}

class BoolSource {
    private var current: Boolean = false
    fun next(): Boolean {
        current = !current
        return current
    }
}

inline fun Boolean.toBits(): Byte = if (this) 1 else 0

inline fun Byte.toBool(): Boolean = this != 0.toByte()

inline fun Long.toBool(): Boolean = this != 0.toLong()

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@CompilerControl(CompilerControl.Mode.DONT_INLINE)
@State(Scope.Thread)
@Fork(value = 1)
@Warmup(iterations = 1)
@Measurement(iterations = 1)
open class VArrays : SmallSizedBenchmark() {

    private lateinit var randOrderOne: IntArray
    private lateinit var randOrderTwo: IntArray
    private lateinit var naturalOrder: IntArray

    // IntPoint2D fields

    private lateinit var intPoint2DBoxed: Array<IntPoint2D>
    private lateinit var intPoint2DPerType: WrapperPerType
    private lateinit var intPoint2DPerSize: WrapperPerSize
    private lateinit var intPoint2DTwoArrays: WrapperTwoArrays

    // Rectangle fields
    private lateinit var rectangleBoxed: Array<Rectangle>
    private lateinit var rectanglePerType: WrapperPerType
    private lateinit var rectanglePerSize: WrapperPerSize
    private lateinit var rectangleTwoArrays: WrapperTwoArrays

    // Triangle fields

    private lateinit var triangleBoxed: Array<Triangle>
    private lateinit var trianglePerType: WrapperPerType
    private lateinit var trianglePerSize: WrapperPerSize

    private lateinit var triangleTwoArrays: WrapperTwoArrays

    // Arrays of IntPoint2D functions starts

    private fun createIntPoint2DBoxed(source: IntSource): Array<IntPoint2D> {
        return Array(smallSize) { IntPoint2D(source.next(), source.next()) }
    }

    private fun createIntPoint2DPerType(source: IntSource): WrapperPerType {
        val intArray = IntArray(smallSize * 2)
        for (index in 0 until smallSize) {
            intArray[index * 2] = source.next()
            intArray[index * 2 + 1] = source.next()
        }
        return WrapperPerType(
                BooleanArray(0),
                ByteArray(0),
                ShortArray(0),
                intArray,
                LongArray(0),
                FloatArray(0),
                DoubleArray(0),
                CharArray(0),
                Array(0) {}
        )
    }

    private fun createIntPoint2DPerSize(source: IntSource): WrapperPerSize {
        val intArray = IntArray(smallSize * 2)
        for (index in 0 until smallSize) {
            intArray[index * 2] = source.next()
            intArray[index * 2 + 1] = source.next()
        }
        return WrapperPerSize(
                ByteArray(0),
                ShortArray(0),
                intArray,
                LongArray(0),
                Array(0) {}
        )
    }

    private fun createIntPoint2DTwoArrays(source: IntSource): WrapperTwoArrays {
        val longArray = LongArray(smallSize * 2)
        for (index in 0 until smallSize) {
            longArray[index * 2] = source.next().toLong()
            longArray[index * 2 + 1] = source.next().toLong()
        }
        return WrapperTwoArrays(longArray, Array(0) {})
    }

    private fun writeIntPoint2DBoxed(arr: Array<IntPoint2D>, order: IntArray, source: IntSource) {
        for (index in order) {
            arr[index] = IntPoint2D(source.next(), source.next())
        }
    }

    private fun writeIntPoint2DPerType(arr: WrapperPerType, order: IntArray, source: IntSource) {
        for (index in order) {
            arr.ints[index * 2] = source.next()
            arr.ints[index * 2 + 1] = source.next()
        }
    }

    private fun writeIntPoint2DPerSize(arr: WrapperPerSize, order: IntArray, source: IntSource) {
        for (index in order) {
            arr.fours[index * 2] = source.next()
            arr.fours[index * 2 + 1] = source.next()
        }
    }

    private fun writeIntPoint2DTwoArrays(arr: WrapperTwoArrays, order: IntArray, source: IntSource) {
        for (index in order) {
            arr.primitives[index * 2] = source.next().toLong()
            arr.primitives[index * 2 + 1] = source.next().toLong()
        }
    }

    private fun calcSumIntPoint2DBoxed(arr: Array<IntPoint2D>, order: IntArray): IntPoint2D {
        var xSum = 0
        var ySum = 0
        for (index in order) {
            val point = arr[index]
            xSum += point.x
            ySum += point.y
        }
        return IntPoint2D(xSum, ySum)
    }

    private fun calcSumIntPoint2DPerType(arr: WrapperPerType, order: IntArray): IntPoint2D {
        var xSum = 0
        var ySum = 0
        for (index in order) {
            xSum += arr.ints[index * 2]
            ySum += arr.ints[index * 2 + 1]
        }
        return IntPoint2D(xSum, ySum)
    }

    private fun calcSumIntPoint2DPerSize(arr: WrapperPerSize, order: IntArray): IntPoint2D {
        var xSum = 0
        var ySum = 0
        for (index in order) {
            xSum += arr.fours[index * 2]
            ySum += arr.fours[index * 2 + 1]
        }
        return IntPoint2D(xSum, ySum)

    }

    private fun calcSumIntPoint2DTwoArrays(arr: WrapperTwoArrays, order: IntArray): IntPoint2D {
        var xSum = 0
        var ySum = 0
        for (index in order) {
            xSum += arr.primitives[index * 2].toInt()
            ySum += arr.primitives[index * 2 + 1].toInt()
        }
        return IntPoint2D(xSum, ySum)

    }

    private fun initIntPoint2DArrays() {
        intPoint2DBoxed = createIntPoint2DBoxed(IntSource())
        intPoint2DPerType = createIntPoint2DPerType(IntSource())
        intPoint2DPerSize = createIntPoint2DPerSize(IntSource())
        intPoint2DTwoArrays = createIntPoint2DTwoArrays(IntSource())
    }

    private fun initTriangleArrays() {
        triangleBoxed = createTriangleArrayBoxed(DoubleSource(), FloatSource(), BoolSource())
        trianglePerType =
                createTriangleArrayPerType(DoubleSource(), FloatSource(), BoolSource())
        trianglePerSize =
                createTriangleArrayPerSize(DoubleSource(), FloatSource(), BoolSource())
        triangleTwoArrays =
                createTriangleArrayTwoArrays(DoubleSource(), FloatSource(), BoolSource())
    }

    // Arrays of Rectangle functions start

    private fun createRectangleArrayBoxed(source: IntSource): Array<Rectangle> {
        return Array(smallSize) {
            Rectangle(
                    IntPoint2D(source.next(), source.next()),
                    IntPoint2D(source.next(), source.next()))
        }
    }

    private fun createRectangleArrayPerType(source: IntSource): WrapperPerType {
        val intArray = IntArray(4 * smallSize)
        for (index in 0 until smallSize) {
            intArray[index * 4 + 0] = source.next()
            intArray[index * 4 + 1] = source.next()
            intArray[index * 4 + 2] = source.next()
            intArray[index * 4 + 3] = source.next()
        }
        return WrapperPerType(
                BooleanArray(0),
                ByteArray(0),
                ShortArray(0),
                intArray,
                LongArray(0),
                FloatArray(0),
                DoubleArray(0),
                CharArray(0),
                Array(0) {}
        )
    }

    private fun createRectangleArrayPerSize(source: IntSource): WrapperPerSize {
        val intArray = IntArray(4 * smallSize)
        for (index in 0 until smallSize) {
            intArray[index * 4 + 0] = source.next()
            intArray[index * 4 + 1] = source.next()
            intArray[index * 4 + 2] = source.next()
            intArray[index * 4 + 3] = source.next()
        }
        return WrapperPerSize(
                ByteArray(0),
                ShortArray(0),
                intArray,
                LongArray(0),
                Array(0) {}
        )
    }

    private fun createRectangleArrayTwoArrays(source: IntSource): WrapperTwoArrays {
        val longArray = LongArray(4 * smallSize)
        for (index in 0 until smallSize) {
            longArray[index * 4 + 0] = source.next().toLong()
            longArray[index * 4 + 1] = source.next().toLong()
            longArray[index * 4 + 2] = source.next().toLong()
            longArray[index * 4 + 3] = source.next().toLong()
        }
        return WrapperTwoArrays(longArray, Array(0) {})
    }

    private fun writeAXCoordinateRectangleBoxed(arr: Array<Rectangle>, order: IntArray, source: IntSource) {
        for (index in order) {
            val rec = arr[index]
            val a = rec.a
            val b = rec.b
            arr[index] = Rectangle(IntPoint2D(source.next(), a.y), b)
        }
    }

    private fun writeAXCoordinateRectanglePerType(arr: WrapperPerType, order: IntArray, source: IntSource) {
        for (index in order) {
            arr.ints[index * 4 + 0] = source.next()
        }
    }

    private fun writeAXCoordinateRectanglePerSize(arr: WrapperPerSize, order: IntArray, source: IntSource) {
        for (index in order) {
            arr.fours[index * 4 + 0] = source.next()
        }
    }

    private fun writeAXCoordinateRectangleTwoArrays(arr: WrapperTwoArrays, order: IntArray, source: IntSource) {
        for (index in order) {
            arr.primitives[index * 4 + 0] = source.next().toLong()
        }
    }

    private fun calcXMeanRectangleBoxed(arr: Array<Rectangle>, order: IntArray): Double {
        var xMean = 0.0
        for (index in order) {
            xMean += arr[index].a.x + arr[index].b.x
        }
        return xMean / smallSize
    }

    private fun calcXMeanRectanglePerType(arr: WrapperPerType, order: IntArray): Double {
        var xMean = 0.0
        for (index in order) {
            xMean += arr.ints[index * 4 + 0] + arr.ints[index * 4 + 2]
        }
        return xMean / smallSize
    }

    private fun calcXMeanRectanglePerSize(arr: WrapperPerSize, order: IntArray): Double {
        var xMean = 0.0
        for (index in order) {
            xMean += arr.fours[index * 4 + 0] + arr.fours[index * 4 + 2]
        }
        return xMean / smallSize
    }

    private fun calcXMeanRectangleTwoArrays(arr: WrapperTwoArrays, order: IntArray): Double {
        var xMean = 0.0
        for (index in order) {
            xMean += arr.primitives[index * 4 + 0].toInt() + arr.primitives[index * 4 + 2].toInt()
        }
        return xMean / smallSize
    }

    // Arrays of Triangle functions start

    private fun createTriangleArrayBoxed(
            doubleSource: DoubleSource,
            floatSource: FloatSource,
            boolSource: BoolSource
    ): Array<Triangle> {
        return Array(smallSize) {
            Triangle(
                    p1 = Point3D(doubleSource.next(), doubleSource.next(), doubleSource.next()),
                    p2 = Point3D(doubleSource.next(), doubleSource.next(), doubleSource.next()),
                    p3 = Point3D(doubleSource.next(), doubleSource.next(), doubleSource.next()),
                    flag = boolSource.next(),
                    weight = floatSource.next()
            )
        }
    }

    private fun createTriangleArrayPerType(
            doubleSource: DoubleSource,
            floatSource: FloatSource,
            boolSource: BoolSource
    ): WrapperPerType {
        val size = smallSize
        val doubleArray = DoubleArray(9 * size)
        val floatArray = FloatArray(size)
        val boolArray = BooleanArray(size)
        for (index in 0 until size) {
            doubleArray[index * 9 + 0] = doubleSource.next()
            doubleArray[index * 9 + 1] = doubleSource.next()
            doubleArray[index * 9 + 2] = doubleSource.next()
            doubleArray[index * 9 + 3] = doubleSource.next()
            doubleArray[index * 9 + 4] = doubleSource.next()
            doubleArray[index * 9 + 5] = doubleSource.next()
            doubleArray[index * 9 + 6] = doubleSource.next()
            doubleArray[index * 9 + 7] = doubleSource.next()
            doubleArray[index * 9 + 8] = doubleSource.next()
            floatArray[index] = floatSource.next()
            boolArray[index] = boolSource.next()
        }
        return WrapperPerType(
                boolArray,
                ByteArray(0),
                ShortArray(0),
                IntArray(0),
                LongArray(0),
                floatArray,
                doubleArray,
                CharArray(0),
                Array(0) {}
        )
    }


    private fun createTriangleArrayPerSize(
            doubleSource: DoubleSource,
            floatSource: FloatSource,
            boolSource: BoolSource
    ): WrapperPerSize {
        val size = smallSize
        val longArray = LongArray(size * 9)
        val intArray = IntArray(size)
        val byteArray = ByteArray(size)

        for (index in 0 until size) {
            longArray[index * 9 + 0] = doubleSource.next().toBits()
            longArray[index * 9 + 1] = doubleSource.next().toBits()
            longArray[index * 9 + 2] = doubleSource.next().toBits()
            longArray[index * 9 + 3] = doubleSource.next().toBits()
            longArray[index * 9 + 4] = doubleSource.next().toBits()
            longArray[index * 9 + 5] = doubleSource.next().toBits()
            longArray[index * 9 + 6] = doubleSource.next().toBits()
            longArray[index * 9 + 7] = doubleSource.next().toBits()
            longArray[index * 9 + 8] = doubleSource.next().toBits()
            intArray[index] = floatSource.next().toBits()
            byteArray[index] = boolSource.next().toBits()
        }

        return WrapperPerSize(
                byteArray,
                ShortArray(0),
                intArray,
                longArray,
                Array(0) {}
        )
    }


    private fun createTriangleArrayTwoArrays(
            doubleSource: DoubleSource,
            floatSource: FloatSource,
            boolSource: BoolSource
    ): WrapperTwoArrays {
        val size = smallSize
        val longArray = LongArray(size * 11)

        for (index in 0 until size) {
            longArray[index * 11 + 0] = doubleSource.next().toBits()
            longArray[index * 11 + 1] = doubleSource.next().toBits()
            longArray[index * 11 + 2] = doubleSource.next().toBits()
            longArray[index * 11 + 3] = doubleSource.next().toBits()
            longArray[index * 11 + 4] = doubleSource.next().toBits()
            longArray[index * 11 + 5] = doubleSource.next().toBits()
            longArray[index * 11 + 6] = doubleSource.next().toBits()
            longArray[index * 11 + 7] = doubleSource.next().toBits()
            longArray[index * 11 + 8] = doubleSource.next().toBits()
            longArray[index * 11 + 9] = floatSource.next().toBits().toLong()
            longArray[index * 11 + 10] = boolSource.next().toBits().toLong()
        }

        return WrapperTwoArrays(longArray, Array(0) {})
    }

    private fun writeXAndFlagTriangleBoxed(
            arr: Array<Triangle>,
            order: IntArray,
            source: DoubleSource,
            boolSource: BoolSource
    ) {
        for (index in order) {
            val point = arr[index]
            arr[index] = arr[index].copy(
                    p1 = point.p1.copy(x = source.next()),
                    p2 = point.p2.copy(x = source.next()),
                    p3 = point.p3.copy(x = source.next()),
                    flag = boolSource.next()
            )
        }
    }

    private fun writeXAndFlagTrianglePerType(
            arr: WrapperPerType,
            order: IntArray,
            source: DoubleSource,
            boolSource: BoolSource
    ) {
        for (index in order) {
            arr.doubles[index * 9 + 0] = source.next()
            arr.doubles[index * 9 + 3] = source.next()
            arr.doubles[index * 9 + 6] = source.next()
            arr.booleans[index] = boolSource.next()
        }
    }

    private fun writeXAndFlagTrianglePerSize(
            arr: WrapperPerSize,
            order: IntArray,
            source: DoubleSource,
            boolSource: BoolSource
    ) {
        for (index in order) {
            arr.eights[index * 9 + 0] = source.next().toBits()
            arr.eights[index * 9 + 3] = source.next().toBits()
            arr.eights[index * 9 + 6] = source.next().toBits()
            arr.ones[index] = boolSource.next().toBits()
        }
    }

    private fun writeXAndFlagTriangleTwoArrays(
            arr: WrapperTwoArrays,
            order: IntArray,
            source: DoubleSource,
            boolSource: BoolSource
    ) {
        for (index in order) {
            arr.primitives[index * 11 + 0] = source.next().toBits()
            arr.primitives[index * 11 + 3] = source.next().toBits()
            arr.primitives[index * 11 + 6] = source.next().toBits()
            arr.primitives[index * 11 + 10] = boolSource.next().toBits().toLong()
        }
    }

    private fun calcWeightedMeanTriangleBoxed(arr: Array<Triangle>, order: IntArray): Point3D {
        var xMean = 0.0
        var yMean = 0.0
        var zMean = 0.0
        for (index in order) {
            val triangle = arr[index]
            if (!triangle.flag) continue
            val weight = triangle.weight
            xMean += (triangle.p1.x + triangle.p2.x + triangle.p3.x) * weight
            yMean += (triangle.p1.y + triangle.p2.y + triangle.p3.y) * weight
            zMean += (triangle.p1.z + triangle.p2.z + triangle.p3.z) * weight
        }
        return Point3D(xMean / arr.size, yMean / arr.size, zMean / arr.size)
    }

    private fun calcWeightedMeanTrianglePerType(arr: WrapperPerType, order: IntArray): Point3D {
        var xMean = 0.0
        var yMean = 0.0
        var zMean = 0.0
        val size = smallSize
        for (index in order) {
            if (!arr.booleans[index]) continue
            val weight = arr.floats[index]
            xMean += (arr.doubles[index * 9 + 0] + arr.doubles[index * 9 + 3] + arr.doubles[index * 9 + 6]) * weight
            yMean += (arr.doubles[index * 9 + 1] + arr.doubles[index * 9 + 4] + arr.doubles[index * 9 + 7]) * weight
            zMean += (arr.doubles[index * 9 + 2] + arr.doubles[index * 9 + 5] + arr.doubles[index * 9 + 8]) * weight
        }
        return Point3D(xMean / size, yMean / size, zMean / size)
    }

    private fun calcWeightedMeanTrianglePerSize(arr: WrapperPerSize, order: IntArray): Point3D {
        var xMean = 0.0
        var yMean = 0.0
        var zMean = 0.0
        val size = smallSize
        for (index in order) {
            if (!arr.ones[index].toBool()) continue
            val weight = Float.fromBits(arr.fours[index])
            xMean += (Double.fromBits(arr.eights[index * 9 + 0]) + Double.fromBits(arr.eights[index * 9 + 3]) + Double.fromBits(
                    arr.eights[index * 9 + 6]
            )) * weight
            yMean += (Double.fromBits(arr.eights[index * 9 + 1]) + Double.fromBits(arr.eights[index * 9 + 4]) + Double.fromBits(
                    arr.eights[index * 9 + 7]
            )) * weight
            zMean += (Double.fromBits(arr.eights[index * 9 + 2]) + Double.fromBits(arr.eights[index * 9 + 5]) + Double.fromBits(
                    arr.eights[index * 9 + 8]
            )) * weight
        }
        return Point3D(xMean / size, yMean / size, zMean / size)
    }


    private fun calcWeightedMeanTriangleTwoArrays(arr: WrapperTwoArrays, order: IntArray): Point3D {
        var xMean = 0.0
        var yMean = 0.0
        var zMean = 0.0
        val size = smallSize
        for (index in order) {
            if (!arr.primitives[index * 11 + 10].toBool()) continue
            val weight = Float.fromBits(arr.primitives[index * 11 + 9].toInt())
            xMean += (Double.fromBits(arr.primitives[index * 11 + 0]) + Double.fromBits(arr.primitives[index * 11 + 3]) + Double.fromBits(
                    arr.primitives[index * 11 + 6]
            )) * weight
            yMean += (Double.fromBits(arr.primitives[index * 11 + 1]) + Double.fromBits(arr.primitives[index * 11 + 4]) + Double.fromBits(
                    arr.primitives[index * 11 + 7]
            )) * weight
            zMean += (Double.fromBits(arr.primitives[index * 11 + 2]) + Double.fromBits(arr.primitives[index * 11 + 5]) + Double.fromBits(
                    arr.primitives[index * 11 + 8]
            )) * weight
        }
        return Point3D(xMean / size, yMean / size, zMean / size)
    }

    @Setup
    fun setup() {
        naturalOrder = IntArray(smallSize) { it }
        randOrderOne = IntArray(smallSize) { it }
        randOrderOne.shuffle()
        randOrderTwo = IntArray(smallSize) { it }
        randOrderTwo.shuffle()

        doIntPoint2DPreparations()
        doRectanglePreparations()
        doTrianglePreparations()
    }

    private fun doRectanglePreparations() {
        assertRectangleXMeanMeanCalculationCorrectness()
        initRectangleArrays()
    }

    private fun initRectangleArrays() {
        rectangleBoxed = createRectangleArrayBoxed(IntSource())
        rectanglePerType = createRectangleArrayPerType(IntSource())
        rectanglePerSize = createRectangleArrayPerSize(IntSource())
        rectangleTwoArrays = createRectangleArrayTwoArrays(IntSource())
    }

    private fun assertRectangleXMeanMeanCalculationCorrectness() {
        val expectedMean = calcXMeanRectangleBoxed(createRectangleArrayBoxed(IntSource()), naturalOrder)
        val perTypeMean = calcXMeanRectanglePerType(createRectangleArrayPerType(IntSource()), randOrderOne)
        val perSizeMean = calcXMeanRectanglePerSize(createRectangleArrayPerSize(IntSource()), randOrderTwo)
        val twoArraysMean = calcXMeanRectangleTwoArrays(createRectangleArrayTwoArrays(IntSource()), naturalOrder)
        require(perTypeMean == expectedMean)
        require(perSizeMean == expectedMean)
        require(twoArraysMean == expectedMean)
    }

    private fun doTrianglePreparations() {
        assertTriangleWeightedMeanCalculationCorrectness()
        initTriangleArrays()
    }

    private fun assertTriangleWeightedMeanCalculationCorrectness() {
        val expectedMean =
                calcWeightedMeanTriangleBoxed(
                        createTriangleArrayBoxed(DoubleSource(), FloatSource(), BoolSource()),
                        naturalOrder
                )
        val perTypeMean = calcWeightedMeanTrianglePerType(
                createTriangleArrayPerType(
                        DoubleSource(), FloatSource(), BoolSource()
                ), randOrderOne
        )
        val perSizeMean = calcWeightedMeanTrianglePerSize(
                createTriangleArrayPerSize(
                        DoubleSource(), FloatSource(), BoolSource()
                ), randOrderTwo
        )
        val twoArraysMean = calcWeightedMeanTriangleTwoArrays(
                createTriangleArrayTwoArrays(
                        DoubleSource(), FloatSource(), BoolSource()
                ), naturalOrder
        )
        require(perTypeMean == expectedMean)
        require(perSizeMean == expectedMean)
        require(twoArraysMean == expectedMean)
    }

    private fun doIntPoint2DPreparations() {
        assert2DPointSumCalculationCorrectness()
        initIntPoint2DArrays()
    }

    private fun assert2DPointSumCalculationCorrectness() {
        val expectedSum = calcSumIntPoint2DBoxed(createIntPoint2DBoxed(IntSource()), naturalOrder)
        val perTypeSum =
                calcSumIntPoint2DPerType(createIntPoint2DPerType(IntSource()), randOrderOne)
        val perSizeSum =
                calcSumIntPoint2DPerSize(createIntPoint2DPerSize(IntSource()), randOrderTwo)
        val twoArraysSum = calcSumIntPoint2DTwoArrays(
                createIntPoint2DTwoArrays(
                        IntSource()
                ), naturalOrder
        )
        require(perTypeSum == expectedSum)
        require(perSizeSum == expectedSum)
        require(twoArraysSum == expectedSum)
    }

    // Start IntPoint2D initialization benchmarks

    @Benchmark
    fun run_Point2D_Create_Boxed(bh: Blackhole) {
        bh.consume(createIntPoint2DBoxed(IntSource()))
    }

    @Benchmark
    fun run_Point2D_Create_PerType(bh: Blackhole) {
        bh.consume(createIntPoint2DPerType(IntSource()))
    }

    @Benchmark
    fun run_Point2D_Create_PerSize(bh: Blackhole) {
        bh.consume(createIntPoint2DPerSize(IntSource()))
    }

    @Benchmark
    fun run_Point2D_Create_TwoArrays(bh: Blackhole) {
        bh.consume(createIntPoint2DTwoArrays(IntSource()))
    }

    // Start IntPoint2D sum calculation in natural order benchmarks

    @Benchmark
    fun run_Point2D_CalcSumInNaturalOrder_Boxed(bh: Blackhole) {
        bh.consume(calcSumIntPoint2DBoxed(intPoint2DBoxed, naturalOrder))
    }

    @Benchmark
    fun run_Point2D_CalcSumInNaturalOrder_PerType(bh: Blackhole) {
        bh.consume(calcSumIntPoint2DPerType(intPoint2DPerType, naturalOrder))
    }


    @Benchmark
    fun run_Point2D_CalcSumInNaturalOrder_PerSize(bh: Blackhole) {
        bh.consume(calcSumIntPoint2DPerSize(intPoint2DPerSize, naturalOrder))
    }

    @Benchmark
    fun run_Point2D_CalcSumInNaturalOrder_TwoArrays(bh: Blackhole) {
        bh.consume(calcSumIntPoint2DTwoArrays(intPoint2DTwoArrays, naturalOrder))
    }

    // Start IntPoint2D sum calculation in random order benchmarks

    @Benchmark
    fun run_Point2D_CalcSumInRandomOrder_Boxed() {
        calcSumIntPoint2DBoxed(intPoint2DBoxed, randOrderOne)
    }

    @Benchmark
    fun run_Point2D_CalcSumInRandomOrder_PerType(bh: Blackhole) {
        bh.consume(calcSumIntPoint2DPerType(intPoint2DPerType, randOrderOne))
    }

    @Benchmark
    fun run_Point2D_CalcSumInRandomOrder_PerSize(bh: Blackhole) {
        bh.consume(calcSumIntPoint2DPerSize(intPoint2DPerSize, randOrderOne))
    }

    @Benchmark
    fun run_Point2D_CalcSumInRandomOrder_TwoArrays(bh: Blackhole) {
        bh.consume(calcSumIntPoint2DTwoArrays(intPoint2DTwoArrays, randOrderOne))
    }

    // Start IntPoint2D WriteIn in natural order benchmarks

    @Benchmark
    fun run_Point2D_WriteInNaturalOrder_Boxed() {
        writeIntPoint2DBoxed(intPoint2DBoxed, naturalOrder, IntSource())
    }

    @Benchmark
    fun run_Point2D_WriteInNaturalOrder_PerType() {
        writeIntPoint2DPerType(intPoint2DPerType, naturalOrder, IntSource())
    }


    @Benchmark
    fun run_Point2D_WriteInNaturalOrder_PerSize() {
        writeIntPoint2DPerSize(intPoint2DPerSize, naturalOrder, IntSource())
    }

    @Benchmark
    fun run_Point2D_WriteInNaturalOrder_TwoArrays() {
        writeIntPoint2DTwoArrays(intPoint2DTwoArrays, naturalOrder, IntSource())
    }

    // Start IntPoint2D WriteIn in random order benchmarks

    @Benchmark
    fun run_Point2D_WriteInRandomOrder_Boxed() {
        writeIntPoint2DBoxed(intPoint2DBoxed, randOrderTwo, IntSource())
    }

    @Benchmark
    fun run_Point2D_WriteInRandomOrder_PerType() {
        writeIntPoint2DPerType(intPoint2DPerType, randOrderTwo, IntSource())
    }


    @Benchmark
    fun run_Point2D_WriteInRandomOrder_PerSize() {
        writeIntPoint2DPerSize(intPoint2DPerSize, randOrderTwo, IntSource())
    }

    @Benchmark
    fun run_Point2D_WriteInRandomOrder_TwoArrays() {
        writeIntPoint2DTwoArrays(intPoint2DTwoArrays, randOrderTwo, IntSource())
    }

    // Start IntPoint2D complex benchmarks

    @Benchmark
    fun run_Point2D_ComplexScenario_Boxed(bh: Blackhole) {
        val arr = createIntPoint2DBoxed(IntSource()) // create
        bh.consume(calcSumIntPoint2DBoxed(arr, naturalOrder)) // read in natural order
        writeIntPoint2DBoxed(arr, naturalOrder, IntSource()) // write in natural order
        writeIntPoint2DBoxed(arr, randOrderOne, IntSource()) // write in random order
        bh.consume(calcSumIntPoint2DBoxed(arr, randOrderTwo)) // read in random order
    }

    @Benchmark
    fun run_Point2D_ComplexScenario_PerType(bh: Blackhole) {
        val arr = createIntPoint2DPerType(IntSource()) // create
        bh.consume(calcSumIntPoint2DPerType(arr, naturalOrder)) // read in natural order
        writeIntPoint2DPerType(arr, naturalOrder, IntSource()) // write in natural order
        writeIntPoint2DPerType(arr, randOrderOne, IntSource()) // write in random order
        bh.consume(calcSumIntPoint2DPerType(arr, randOrderTwo)) // read in random order
    }

    @Benchmark
    fun run_Point2D_ComplexScenario_PerSize(bh: Blackhole) {
        val arr = createIntPoint2DPerSize(IntSource()) // create
        bh.consume(calcSumIntPoint2DPerSize(arr, naturalOrder)) // read in natural order
        writeIntPoint2DPerSize(arr, naturalOrder, IntSource()) // write in natural order
        writeIntPoint2DPerSize(arr, randOrderOne, IntSource()) // write in random order
        bh.consume(calcSumIntPoint2DPerSize(arr, randOrderTwo)) // read in random order
    }

    @Benchmark
    fun run_Point2D_ComplexScenario_TwoArrays(bh: Blackhole) {
        val arr = createIntPoint2DTwoArrays(IntSource()) // create
        bh.consume(calcSumIntPoint2DTwoArrays(arr, naturalOrder)) // read in natural order
        writeIntPoint2DTwoArrays(arr, naturalOrder, IntSource()) // write in natural order
        writeIntPoint2DTwoArrays(arr, randOrderOne, IntSource()) // write in random order
        bh.consume(calcSumIntPoint2DTwoArrays(arr, randOrderTwo)) // read in random order
    }

    // Start Triangle initialization benchmark

    @Benchmark
    fun run_Triangle_Create_Boxed(bh: Blackhole) {
        bh.consume(createTriangleArrayBoxed(DoubleSource(), FloatSource(), BoolSource()))
    }

    @Benchmark
    fun run_Triangle_Create_PerType(bh: Blackhole) {
        bh.consume(createTriangleArrayPerType(DoubleSource(), FloatSource(), BoolSource()))
    }

    @Benchmark
    fun run_Triangle_Create_PerSize(bh: Blackhole) {
        bh.consume(createTriangleArrayPerSize(DoubleSource(), FloatSource(), BoolSource()))
    }

    @Benchmark
    fun run_Triangle_Create_TwoArrays(bh: Blackhole) {
        bh.consume(createTriangleArrayTwoArrays(DoubleSource(), FloatSource(), BoolSource()))
    }


    // Start Triangle mean calculation in natural order benchmark

    @Benchmark
    fun run_Triangle_CalcMeanInNaturalOrder_Boxed(bh: Blackhole) {
        bh.consume(calcWeightedMeanTriangleBoxed(triangleBoxed, naturalOrder))
    }

    @Benchmark
    fun run_Triangle_CalcMeanInNaturalOrder_PerType(bh: Blackhole) {
        bh.consume(calcWeightedMeanTrianglePerType(trianglePerType, naturalOrder))
    }

    @Benchmark
    fun run_Triangle_CalcMeanInNaturalOrder_PerSize(bh: Blackhole) {
        bh.consume(calcWeightedMeanTrianglePerSize(trianglePerSize, naturalOrder))
    }

    @Benchmark
    fun run_Triangle_CalcMeanInNaturalOrder_TwoArrays(bh: Blackhole) {
        bh.consume(calcWeightedMeanTriangleTwoArrays(triangleTwoArrays, naturalOrder))
    }

    // Start Triangle mean calculation in random order benchmark

    @Benchmark
    fun run_Triangle_CalcMeanInRandomOrder_Boxed(bh: Blackhole) {
        bh.consume(calcWeightedMeanTriangleBoxed(triangleBoxed, randOrderOne))
    }

    @Benchmark
    fun run_Triangle_CalcMeanInRandomOrder_PerType(bh: Blackhole) {
        bh.consume(calcWeightedMeanTrianglePerType(trianglePerType, randOrderOne))
    }

    @Benchmark
    fun run_Triangle_CalcMeanInRandomOrder_PerSize(bh: Blackhole) {
        bh.consume(calcWeightedMeanTrianglePerSize(trianglePerSize, randOrderOne))
    }

    @Benchmark
    fun run_Triangle_CalcMeanInRandomOrder_TwoArrays(bh: Blackhole) {
        bh.consume(calcWeightedMeanTriangleTwoArrays(triangleTwoArrays, randOrderOne))
    }

    // Start Triangle write in natural order benchmarks

    @Benchmark
    fun run_Triangle_WriteInNaturalOrder_Boxed() {
        writeXAndFlagTriangleBoxed(triangleBoxed, naturalOrder, DoubleSource(), BoolSource())
    }

    @Benchmark
    fun run_Triangle_WriteInNaturalOrder_PerType() {
        writeXAndFlagTrianglePerType(trianglePerType, naturalOrder, DoubleSource(), BoolSource())
    }

    @Benchmark
    fun run_Triangle_WriteInNaturalOrder_PerSize() {
        writeXAndFlagTrianglePerSize(trianglePerSize, naturalOrder, DoubleSource(), BoolSource())
    }

    @Benchmark
    fun run_Triangle_WriteInNaturalOrder_TwoArrays() {
        writeXAndFlagTriangleTwoArrays(triangleTwoArrays, naturalOrder, DoubleSource(), BoolSource())
    }

    // Start Triangle write in random order benchmarks

    @Benchmark
    fun run_Triangle_WriteInRandomOrder_Boxed() {
        writeXAndFlagTriangleBoxed(triangleBoxed, randOrderOne, DoubleSource(), BoolSource())
    }

    @Benchmark
    fun run_Triangle_WriteInRandomOrder_PerType() {
        writeXAndFlagTrianglePerType(trianglePerType, randOrderOne, DoubleSource(), BoolSource())
    }

    @Benchmark
    fun run_Triangle_WriteInRandomOrder_PerSize() {
        writeXAndFlagTrianglePerSize(trianglePerSize, randOrderOne, DoubleSource(), BoolSource())
    }

    @Benchmark
    fun run_Triangle_WriteInRandomOrder_TwoArrays() {
        writeXAndFlagTriangleTwoArrays(triangleTwoArrays, randOrderOne, DoubleSource(), BoolSource())
    }

    // Start Triangle complex benchmarks

    @Benchmark
    fun run_Triangle_ComplexScenario_Boxed(bh: Blackhole) {
        val arr = createTriangleArrayBoxed(DoubleSource(), FloatSource(), BoolSource()) // create
        bh.consume(calcWeightedMeanTriangleBoxed(arr, naturalOrder)) // read in natural order
        writeXAndFlagTriangleBoxed(arr, naturalOrder, DoubleSource(), BoolSource()) // write in natural order
        writeXAndFlagTriangleBoxed(arr, randOrderOne, DoubleSource(), BoolSource()) // write in random order
        bh.consume(calcWeightedMeanTriangleBoxed(arr, randOrderTwo)) // read in random order
    }

    @Benchmark
    fun run_Triangle_ComplexScenario_PerType(bh: Blackhole) {
        val arr = createTriangleArrayPerType(DoubleSource(), FloatSource(), BoolSource()) // create
        bh.consume(calcWeightedMeanTrianglePerType(arr, naturalOrder)) // read in natural order
        writeXAndFlagTrianglePerType(arr, naturalOrder, DoubleSource(), BoolSource()) // write in natural order
        writeXAndFlagTrianglePerType(arr, randOrderOne, DoubleSource(), BoolSource()) // write in random order
        bh.consume(calcWeightedMeanTrianglePerType(arr, randOrderTwo)) // read in random order
    }

    @Benchmark
    fun run_Triangle_ComplexScenario_PerSize(bh: Blackhole) {
        val arr = createTriangleArrayPerSize(DoubleSource(), FloatSource(), BoolSource()) // create
        bh.consume(calcWeightedMeanTrianglePerSize(arr, naturalOrder)) // read in natural order
        writeXAndFlagTrianglePerSize(arr, naturalOrder, DoubleSource(), BoolSource()) // write in natural order
        writeXAndFlagTrianglePerSize(arr, randOrderOne, DoubleSource(), BoolSource()) // write in random order
        bh.consume(calcWeightedMeanTrianglePerSize(arr, randOrderTwo)) // read in random order
    }

    @Benchmark
    fun run_Triangle_ComplexScenario_TwoArrays(bh: Blackhole) {
        val arr = createTriangleArrayTwoArrays(DoubleSource(), FloatSource(), BoolSource()) // create
        bh.consume(calcWeightedMeanTriangleTwoArrays(arr, naturalOrder)) // read in natural order
        writeXAndFlagTriangleTwoArrays(arr, naturalOrder, DoubleSource(), BoolSource()) // write in natural order
        writeXAndFlagTriangleTwoArrays(arr, randOrderOne, DoubleSource(), BoolSource()) // write in random order
        bh.consume(calcWeightedMeanTriangleTwoArrays(arr, randOrderTwo)) // read in random order
    }

    // Start Rectangle initialization benchmarks

    @Benchmark
    fun run_Rectangle_Create_Boxed(bh: Blackhole) {
        bh.consume(createRectangleArrayBoxed(IntSource()))
    }

    @Benchmark
    fun run_Rectangle_Create_PerType(bh: Blackhole) {
        bh.consume(createRectangleArrayPerType(IntSource()))
    }

    @Benchmark
    fun run_Rectangle_Create_PerSize(bh: Blackhole) {
        bh.consume(createRectangleArrayPerSize(IntSource()))
    }

    @Benchmark
    fun run_Rectangle_Create_TwoArrays(bh: Blackhole) {
        bh.consume(createRectangleArrayTwoArrays(IntSource()))
    }

    // Start Rectangle calc x-mean in natural order benchmarks

    @Benchmark
    fun run_Rectangle_CalcXMeanInNaturalOrder_Boxed(bh: Blackhole) {
        bh.consume(calcXMeanRectangleBoxed(rectangleBoxed, naturalOrder))
    }

    @Benchmark
    fun run_Rectangle_CalcXMeanInNaturalOrder_PerType(bh: Blackhole) {
        bh.consume(calcXMeanRectanglePerType(rectanglePerType, naturalOrder))
    }

    @Benchmark
    fun run_Rectangle_CalcXMeanInNaturalOrder_PerSize(bh: Blackhole) {
        bh.consume(calcXMeanRectanglePerSize(rectanglePerSize, naturalOrder))
    }

    @Benchmark
    fun run_Rectangle_CalcXMeanInNaturalOrder_TwoArrays(bh: Blackhole) {
        bh.consume(calcXMeanRectangleTwoArrays(rectangleTwoArrays, naturalOrder))
    }

    // Start Rectangle calc x-mean in random order benchmarks

    @Benchmark
    fun run_Rectangle_CalcXMeanInRandomOrder_Boxed(bh: Blackhole) {
        bh.consume(calcXMeanRectangleBoxed(rectangleBoxed, randOrderOne))
    }

    @Benchmark
    fun run_Rectangle_CalcXMeanInRandomOrder_PerType(bh: Blackhole) {
        bh.consume(calcXMeanRectanglePerType(rectanglePerType, randOrderOne))
    }

    @Benchmark
    fun run_Rectangle_CalcXMeanInRandomOrder_PerSize(bh: Blackhole) {
        bh.consume(calcXMeanRectanglePerSize(rectanglePerSize, randOrderOne))
    }

    @Benchmark
    fun run_Rectangle_CalcXMeanInRandomOrder_TwoArrays(bh: Blackhole) {
        bh.consume(calcXMeanRectangleTwoArrays(rectangleTwoArrays, randOrderOne))
    }

    // Start Rectangle write a.x coordinates in natural order benchmarks

    @Benchmark
    fun run_Rectangle_WriteAXCoordinateInNaturalOrder_Boxed() {
        writeAXCoordinateRectangleBoxed(rectangleBoxed, naturalOrder, IntSource())
    }

    @Benchmark
    fun run_Rectangle_WriteAXCoordinateInNaturalOrder_PerType() {
        writeAXCoordinateRectanglePerType(rectanglePerType, naturalOrder, IntSource())
    }

    @Benchmark
    fun run_Rectangle_WriteAXCoordinateInNaturalOrder_PerSize() {
        writeAXCoordinateRectanglePerSize(rectanglePerSize, naturalOrder, IntSource())
    }

    @Benchmark
    fun run_Rectangle_WriteAXCoordinateInNaturalOrder_TwoArrays() {
        writeAXCoordinateRectangleTwoArrays(rectangleTwoArrays, naturalOrder, IntSource())
    }

    // Start Rectangle write a.x coordinates in random order benchmarks

    @Benchmark
    fun run_Rectangle_WriteAXCoordinateInRandomOrder_Boxed() {
        writeAXCoordinateRectangleBoxed(rectangleBoxed, randOrderTwo, IntSource())
    }

    @Benchmark
    fun run_Rectangle_WriteAXCoordinateInRandomOrder_PerType() {
        writeAXCoordinateRectanglePerType(rectanglePerType, randOrderTwo, IntSource())
    }

    @Benchmark
    fun run_Rectangle_WriteAXCoordinateInRandomOrder_PerSize() {
        writeAXCoordinateRectanglePerSize(rectanglePerSize, randOrderTwo, IntSource())
    }

    @Benchmark
    fun run_Rectangle_WriteAXCoordinateInRandomOrder_TwoArrays() {
        writeAXCoordinateRectangleTwoArrays(rectangleTwoArrays, randOrderTwo, IntSource())
    }

    // Start Rectangle complex scenario benchmarks

    @Benchmark
    fun run_Rectangle_ComplexScenario_Boxed(bh: Blackhole) {
        val arr = createRectangleArrayBoxed(IntSource()) // create
        bh.consume(calcXMeanRectangleBoxed(arr, naturalOrder)) // read in natural order
        writeAXCoordinateRectangleBoxed(arr, naturalOrder, IntSource()) // write in natural order
        writeAXCoordinateRectangleBoxed(arr, randOrderOne, IntSource()) // write in random order
        bh.consume(calcXMeanRectangleBoxed(arr, randOrderTwo)) // read in random order
    }

    @Benchmark
    fun run_Rectangle_ComplexScenario_PerType(bh: Blackhole) {
        val arr = createRectangleArrayPerType(IntSource()) // create
        bh.consume(calcXMeanRectanglePerType(arr, naturalOrder)) // read in natural order
        writeAXCoordinateRectanglePerType(arr, naturalOrder, IntSource()) // write in natural order
        writeAXCoordinateRectanglePerType(arr, randOrderOne, IntSource()) // write in random order
        bh.consume(calcXMeanRectanglePerType(arr, randOrderTwo)) // read in random order
    }

    @Benchmark
    fun run_Rectangle_ComplexScenario_PerSize(bh: Blackhole) {
        val arr = createRectangleArrayPerSize(IntSource()) // create
        bh.consume(calcXMeanRectanglePerSize(arr, naturalOrder)) // read in natural order
        writeAXCoordinateRectanglePerSize(arr, naturalOrder, IntSource()) // write in natural order
        writeAXCoordinateRectanglePerSize(arr, randOrderOne, IntSource()) // write in random order
        bh.consume(calcXMeanRectanglePerSize(arr, randOrderTwo)) // read in random order
    }

    @Benchmark
    fun run_Rectangle_ComplexScenario_TwoArrays(bh: Blackhole) {
        val arr = createRectangleArrayTwoArrays(IntSource()) // create
        bh.consume(calcXMeanRectangleTwoArrays(arr, naturalOrder)) // read in natural order
        writeAXCoordinateRectangleTwoArrays(arr, naturalOrder, IntSource()) // write in natural order
        writeAXCoordinateRectangleTwoArrays(arr, randOrderOne, IntSource()) // write in random order
        bh.consume(calcXMeanRectangleTwoArrays(arr, randOrderTwo)) // read in random order
    }
}