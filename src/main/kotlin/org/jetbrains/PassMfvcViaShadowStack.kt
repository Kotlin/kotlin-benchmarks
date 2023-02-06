package org.jetbrains

import org.openjdk.jmh.annotations.*
import org.openjdk.jmh.infra.Blackhole
import java.util.Arrays
import java.util.concurrent.TimeUnit

private class ShadowStack(initialCapacity: Int = 2) {
    init {
        require(initialCapacity > 0)
    }
    
    private var storage = LongArray(initialCapacity)
    fun setLong(index: Int, value: Long) {
        if (index >= storage.size) {
            val oldStorage = storage
            storage = LongArray((index + 1) * 2)
            for (i in oldStorage.indices) {
                storage[i] = oldStorage[i]
            }
        }
        storage[index] = value
    }
    fun getLong(index: Int): Long = storage[index]
}

@Suppress("unused")
private class ShadowStackWithCache {
    private var primitiveBuffer: LongArray? = null
    private var objectBuffer: Array<Any?>? = null
    inline var i0: Int
        get() = l0.toInt()
        set(value) { l0 = value.toLong() }
    inline var i1: Int
        get() = l1.toInt()
        set(value) { l1 = value.toLong() }
    inline var i2: Int
        get() = l2.toInt()
        set(value) { l2 = value.toLong() }
    inline var i3: Int
        get() = l3.toInt()
        set(value) { l3 = value.toLong() }

    @JvmField
    var l0: Long = 0
//        get() = i0.toLong() shl 32 or (i1.toLong() and 0xFFFFFFFFL)
//        set(value) {
//            i0 = (value shr 32).toInt()
//            i1 = (value and 0xFFFFFFFFL).toInt()
//        }
    @JvmField
    var l1: Long = 0
//        get() = i2.toLong() shl 32 or (i3.toLong() and 0xFFFFFFFFL)
//        set(value) {
//            i2 = (value shr 32).toInt()
//            i3 = (value and 0xFFFFFFFFL).toInt()
//        }
    @JvmField
    var l2: Long = 0
    @JvmField
    var l3: Long = 0
    
    inline var d0: Double
        get() = Double.fromBits(l0)
        set(value) { l0 = value.toRawBits() }

    inline var d1: Double
        get() = Double.fromBits(l1)
        set(value) { l1 = value.toRawBits() }
    
    inline var d2: Double
        get() = Double.fromBits(l2)
        set(value) { l2 = value.toRawBits() }

    inline var d3: Double
        get() = Double.fromBits(l3)
        set(value) { l3 = value.toRawBits() }
    
    inline var f0: Float
        get() = Float.fromBits(i0)
        set(value) { i0 = value.toRawBits() }
    
    inline var f1: Float
        get() = Float.fromBits(i1)
        set(value) { i1 = value.toRawBits() }
    
    inline var f2: Float
        get() = Float.fromBits(i2)
        set(value) { i2 = value.toRawBits() }
    
    inline var f3: Float
        get() = Float.fromBits(i3)
        set(value) { i3 = value.toRawBits() }
    
    inline var s0: Short
        get() = i0.toShort()
        set(value) { i0 = value.toInt() }
    
    inline var s1: Short
        get() = i1.toShort()
        set(value) { i1 = value.toInt()}
    
    inline var s2: Short
        get() = i2.toShort()
        set(value) { i2 = value.toInt() }
    
    inline var s3: Short
        get() = i3.toShort()
        set(value) { i3 = value.toInt() }
    
    inline var b0: Byte
        get() = i0.toByte()
        set(value) { i0 = value.toInt() }
    
    inline var b1: Byte
        get() = i1.toByte()
        set(value) { i1 = value.toInt()}
    
    inline var b2: Byte
        get() = i2.toByte()
        set(value) { i2 = value.toInt() }
    
    inline var b3: Byte
        get() = i3.toByte()
        set(value) { i3 = value.toInt() }
    
    inline var bool0: Boolean
        get() = i0 == 1
        set(value) { i0 = if (value) 1 else 0 }
    
    inline var bool1: Boolean
        get() = i1 == 1
        set(value) { i1 = if (value) 1 else 0}
    
    inline var bool2: Boolean
        get() = i2 == 1
        set(value) { i2 = if (value) 1 else 0 }
    
    inline var bool3: Boolean
        get() = i3 == 1
        set(value) { i3 = if (value) 1 else 0 }
    
    fun requireByteBufferOfCapacity(length: Int) {
        if (length <= 0) return
        val oldBuffer = primitiveBuffer
        if (oldBuffer == null) {
            primitiveBuffer = LongArray(maxOf(8, length * 2))
        } else if (oldBuffer.size < length) {
            primitiveBuffer = Arrays.copyOf(oldBuffer, maxOf(8, length * 2))
        }
    }
    
    fun requireObjectBufferOfCapacity(length: Int) {
        if (length <= 0) return
        val oldBuffer = objectBuffer
        if (oldBuffer == null) {
            objectBuffer = arrayOfNulls(maxOf(8, length * 2))
        } else if (oldBuffer.size < length) {
            objectBuffer = Arrays.copyOf(oldBuffer, maxOf(8, length * 2))
        }
    }
    
    operator fun set(index: Int, value: Long) {
        primitiveBuffer!![index] = value
    }
    
    operator fun set(index: Int, value: Int) {
        primitiveBuffer!![index] = value.toLong()
    }
    
    operator fun set(index: Int, value: Short) {
        primitiveBuffer!![index] = value.toLong()
    }

    operator fun set(index: Int, value: Byte) {
        primitiveBuffer!![index] = value.toLong()
    }

    operator fun set(index: Int, value: Boolean) {
        primitiveBuffer!![index] = if (value) 1L else 0L
    }

    operator fun set(index: Int, value: Float) {
        primitiveBuffer!![index] = value.toRawBits().toLong()
    }

    operator fun set(index: Int, value: Double) {
        primitiveBuffer!![index] = value.toRawBits()
    }

    operator fun set(index: Int, value: Any?) {
        objectBuffer!![index] = value
    }

    fun getLong(index: Int): Long = primitiveBuffer!![index]

    fun getInt(index: Int): Int = primitiveBuffer!![index].toInt()

    fun getShort(index: Int): Short = primitiveBuffer!![index].toShort()

    fun getByte(index: Int): Byte = primitiveBuffer!![index].toByte()

    fun getBoolean(index: Int): Boolean = primitiveBuffer!![index] == 1L

    fun getFloat(index: Int): Float = Float.fromBits(primitiveBuffer!![index].toInt())

    fun getDouble(index: Int): Double = Double.fromBits(primitiveBuffer!![index])

    fun getObject(index: Int): Any? = objectBuffer!![index]
}

private data class LongPair(private var _x: Long, private var _y: Long) {
    val x get() = _x
    val y get() = _y
    fun copyFrom(x: Long, y: Long) {
        this._x = x
        this._y = y
    }

    override fun toString(): String = "($x, $y)"
}


@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Thread)
open class MfvcShadowStack {
    @Param("1", "10", "50")
    private var n = 0

    @Setup(Level.Iteration)
    fun setContent() {
        val shadowResult = fibonacciShadow(n)
        val shadowWithCacheResult = fibonacciShadowWithCache(n)
        val boxedWithStorageResult = fibonacciBoxWithStorage(n)
        val boxedResult = fibonacciBoxed(n)
        val results = listOf(
            shadowResult,
            shadowWithCacheResult,
            boxedWithStorageResult
        )
        require(results.all { it == boxedResult }) { "Shadow: $results\nBoxed: $boxedResult" }
    }
    
    private fun fibonacciShadowHelper(n: Int, shadowStack: ShadowStackWithCache) {
        if (n > 0) {
            fibonacciShadowHelper(n - 1, shadowStack)
            val x = shadowStack.l0
            val y = shadowStack.l1
            shadowStack.l0 = y
            shadowStack.l1 = x + y
        } else {
            shadowStack.l0 = 0
            shadowStack.l1 = 1
        }
    }
    
    private fun fibonacciBoxedWithStorageHelper(n: Int, returnedValue: LongPair) {
        if (n > 0) {
            fibonacciBoxedWithStorageHelper(n - 1, returnedValue)
            val x = returnedValue.x
            val y = returnedValue.y
            returnedValue.copyFrom(y, x + y)
        } else {
            returnedValue.copyFrom(0, 1)
        }
    }
    
    private fun fibonacciShadowHelper(n: Int, shadowStack: ShadowStack) {
        if (n > 0) {
            fibonacciShadowHelper(n - 1, shadowStack)
            val x = shadowStack.getLong(0)
            val y = shadowStack.getLong(1)
            shadowStack.setLong(0, y)
            shadowStack.setLong(1, x + y)
        } else {
            shadowStack.setLong(0, 0)
            shadowStack.setLong(1, 1)
        }
    }

    @Benchmark
    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    fun newShadow(bh: Blackhole) {
        bh.consume(fibonacciShadow(n))
    }

    private val shadowStack = ShadowStack()
    @Benchmark
    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    fun existingShadow(bh: Blackhole) {
        bh.consume(fibonacciShadow(n, shadowStack))
    }

    @Benchmark
    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    fun newShadowWithCache(bh: Blackhole) {
        bh.consume(fibonacciShadowWithCache(n))
    }

    private val shadowStackWithCache = ShadowStackWithCache()
    @Benchmark
    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    fun existingShadowWithCache(bh: Blackhole) {
        bh.consume(fibonacciShadowWithCache(n, shadowStackWithCache))
    }

    private fun fibonacciShadow(n: Int, shadowStack: ShadowStack = ShadowStack()): Long {
        fibonacciShadowHelper(n, shadowStack)
        return shadowStack.getLong(0)
    }

    private fun fibonacciShadowWithCache(n: Int, shadowStack: ShadowStackWithCache = ShadowStackWithCache()): Long { 
        fibonacciShadowHelper(n, shadowStack)
        return shadowStack.l0
    }

    private fun fibonacciBoxWithStorage(n: Int, storage: LongPair = LongPair(0, 0)): Long {
        fibonacciBoxedWithStorageHelper(n, storage)
        return storage.x
    }

    @Benchmark
    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    fun boxed(bh: Blackhole) {
        bh.consume(fibonacciBoxed(n))
    }

    @Benchmark
    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    fun boxedWithNewStorage(bh: Blackhole) {
        bh.consume(fibonacciBoxWithStorage(n))
    }

    private val storage = LongPair(0, 0)
    @Benchmark
    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    fun boxedWithExistingStorage(bh: Blackhole) {
        bh.consume(fibonacciBoxWithStorage(n, storage))
    }

    private fun fibonacciBoxed(n: Int) = fibonacciBoxedHelper(n).x

    private fun fibonacciBoxedHelper(n: Int): LongPair {
        if (n <= 0) return LongPair(0L, 1L)
        val p = fibonacciBoxedHelper(n - 1)
        return LongPair(p.y, p.x + p.y)
    }
}
