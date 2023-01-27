package org.jetbrains

import org.openjdk.jmh.annotations.*
import org.openjdk.jmh.infra.Blackhole
import java.util.concurrent.TimeUnit

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@CompilerControl(CompilerControl.Mode.DONT_INLINE)
@State(Scope.Thread)
open class VArrays: SizedBenchmark() {

    private data class Point2D(val x: Int, val y: Int)
    private data class Point3D(val x: Int, val y: Int, val z: Int)
    
    private var order = intArrayOf()
    private var readyRegularArray2D = emptyArray<Point2D>()
    private var readyDenseArray2D = intArrayOf()
    private var readySparseArrayX2D = intArrayOf()
    private var readySparseArrayY2D = intArrayOf()
    private var readyRegularArray3D = emptyArray<Point3D>()
    private var readyDenseArray3D = intArrayOf()
    private var readySparseArrayX3D = intArrayOf()
    private var readySparseArrayY3D = intArrayOf()
    private var readySparseArrayZ3D = intArrayOf()
    
    @Setup(Level.Iteration)
    fun setup() {
        order = IntArray(size) { it }
        order.shuffle()
        var n = 0
        readyRegularArray2D = Array(size) { Point2D(n++, n++) }
        readyDenseArray2D = IntArray(2 * size) { it }
        readySparseArrayX2D = IntArray(size) { it * 2 }
        readySparseArrayY2D = IntArray(size) { it * 2 + 1 }
        n = 0
        readyRegularArray3D = Array(size) { Point3D(n++, n++, n++) }
        readyDenseArray3D = IntArray(3 * size) { it }
        readySparseArrayX3D = IntArray(size) { it * 3 }
        readySparseArrayY3D = IntArray(size) { it * 3 + 1 }
        readySparseArrayZ3D = IntArray(size) { it * 3 + 2 }
    }
    
    @Benchmark
    fun generateRegular2D(bh: Blackhole) {
        val array = Array(size) { Point2D(it, -it) }
        bh.consume(array)
    }
    
    @Benchmark
    fun generateDense2D(bh: Blackhole) {
        val array = IntArray(size * 2)
        for (i in 0 until size) {
            array[2 * i] = i
            array[2 * i + 1] = -i
        }
        bh.consume(array)
    }
    
    @Benchmark
    fun generateSparse2D(bh: Blackhole) {
        val array1 = IntArray(size)
        val array2 = IntArray(size)
        for (i in 0 until size) {
            array1[i] = i
            array2[i] = -i
        }
        bh.consume(array1)
        bh.consume(array2)
    }
    
    @Benchmark
    fun sequentialReadRegular2D(bh: Blackhole) {
        for (point in readyRegularArray2D) {
            bh.consume(point.x)
            bh.consume(point.y)
        }
    }
    
    @Benchmark
    fun sequentialReadDense2D(bh: Blackhole) {
        for (i in 0 until size) {
            bh.consume(readyDenseArray2D[2 * i])
            bh.consume(readyDenseArray2D[2 * i + 1])
        }
    }
    
    @Benchmark
    fun sequentialReadSparse2D(bh: Blackhole) {
        for (i in 0 until size) {
            bh.consume(readySparseArrayX2D[i])
            bh.consume(readySparseArrayY2D[i])
        }
    }
    
    @Benchmark
    fun randomAccessReadRegular2D(bh: Blackhole) {
        for (i in order) {
            bh.consume(readyRegularArray2D[i].x)
            bh.consume(readyRegularArray2D[i].y)
        }
    }
    
    @Benchmark
    fun randomAccessReadDense2D(bh: Blackhole) {
        for (i in order) {
            bh.consume(readyDenseArray2D[2 * i])
            bh.consume(readyDenseArray2D[2 * i + 1])
        }
    }
    
    @Benchmark
    fun randomAccessReadSparse2D(bh: Blackhole) {
        for (i in order) {
            bh.consume(readySparseArrayX2D[i])
            bh.consume(readySparseArrayY2D[i])
        }
    }
    
    @Benchmark
    fun generateRegular3D(bh: Blackhole) {
        val array = Array(size) { Point3D(it, -it, it) }
        bh.consume(array)
    }
    
    @Benchmark
    fun generateDense3D(bh: Blackhole) {
        val array = IntArray(size * 3)
        for (i in 0 until size) {
            array[3 * i] = i
            array[3 * i + 1] = -i
            array[3 * i + 2] = i
        }
        bh.consume(array)
    }
    
    @Benchmark
    fun generateSparse3D(bh: Blackhole) {
        val array1 = IntArray(size)
        val array2 = IntArray(size)
        val array3 = IntArray(size)
        for (i in 0 until size) {
            array1[i] = i
            array2[i] = -i
            array3[i] = i
        }
        bh.consume(array1)
        bh.consume(array2)
        bh.consume(array3)
    }
    
    @Benchmark
    fun sequentialReadRegular3D(bh: Blackhole) {
        for (point in readyRegularArray3D) {
            bh.consume(point.x)
            bh.consume(point.y)
            bh.consume(point.z)
        }
    }
    
    @Benchmark
    fun sequentialReadDense3D(bh: Blackhole) {
        for (i in 0 until size) {
            bh.consume(readyDenseArray3D[3 * i])
            bh.consume(readyDenseArray3D[3 * i + 1])
            bh.consume(readyDenseArray3D[3 * i + 2])
        }
    }
    
    @Benchmark
    fun sequentialReadSparse3D(bh: Blackhole) {
        for (i in 0 until size) {
            bh.consume(readySparseArrayX3D[i])
            bh.consume(readySparseArrayY3D[i])
            bh.consume(readySparseArrayZ3D[i])
        }
    }
    
    @Benchmark
    fun randomAccessReadRegular3D(bh: Blackhole) {
        for (i in order) {
            bh.consume(readyRegularArray3D[i].x)
            bh.consume(readyRegularArray3D[i].y)
            bh.consume(readyRegularArray3D[i].z)
        }
    }
    
    @Benchmark
    fun randomAccessReadDense3D(bh: Blackhole) {
        for (i in order) {
            bh.consume(readyDenseArray3D[3 * i])
            bh.consume(readyDenseArray3D[3 * i + 1])
            bh.consume(readyDenseArray3D[3 * i + 2])
        }
    }
    
    @Benchmark
    fun randomAccessReadSparse3D(bh: Blackhole) {
        for (i in order) {
            bh.consume(readySparseArrayX3D[i])
            bh.consume(readySparseArrayY3D[i])
            bh.consume(readySparseArrayZ3D[i])
        }
    }
}