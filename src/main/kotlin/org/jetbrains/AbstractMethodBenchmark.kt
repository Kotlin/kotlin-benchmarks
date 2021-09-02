package org.jetbrains

import org.openjdk.jmh.annotations.*
import java.io.IOException
import java.nio.file.FileSystems
import java.nio.file.Files
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.min

/**
 * Created by Mikhail.Glukhikh on 06/03/2015.
 *
 * A benchmark for a single abstract method based on a string comparison
 */

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
open class AbstractMethodBenchmark : SizedBenchmark() {

    private val arr: List<String> = try {
        Files.readAllLines(FileSystems.getDefault().getPath("zdf-win.txt"))
    } catch (e: IOException) {
        listOf("ёлка", "автобус", "beta", "жизнь", "удод", "колбаса")
    }

    private val sequence = "абвгдеёжзийклмнопрстуфхцчшщъыьэюя"

    private val sequenceMap = HashMap<Char, Int>()

    init {
        var i = 0
        for (ch in sequence) {
            sequenceMap[ch] = i++
        }
    }

    @Benchmark
    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    fun sortStrings(): SortedSet<String> =
        TreeSet(arr.subList(0, min(size, arr.size)))

    fun compare(a: String, b: String): Int {
        val al = a.lowercase(Locale.getDefault())
        val bl = b.lowercase(Locale.getDefault())
        for (i in al.indices) {
            if (i >= bl.length) return 1
            val ai = sequenceMap[al[i]] ?: 100
            val bi = sequenceMap[bl[i]] ?: 100
            if (ai < bi)
                return -1
            if (ai > bi)
                return 1
        }
        return if (al.length < bl.length) -1 else 0
    }

    inner class StringComparator: Comparator<String> {
        override fun compare(o1: String?, o2: String?): Int {
            return this@AbstractMethodBenchmark.compare(o1 ?: "", o2 ?: "")
        }
    }

    @Benchmark
    @CompilerControl(CompilerControl.Mode.DONT_INLINE) fun sortStringsWithComparator(): SortedSet<String> {
        val res = TreeSet(StringComparator())
        res.addAll(arr.subList(0, if (size < arr.size) size else arr.size))
        return res
    }

    @Benchmark
    @CompilerControl(CompilerControl.Mode.DONT_INLINE) fun sortStringsWithComparatorLambda(): SortedSet<String> {
        val res = TreeSet<String> { a, b -> compare(a, b) }
        res.addAll(arr.subList(0, if (size < arr.size) size else arr.size))
        return res
    }

    @Benchmark
    @CompilerControl(CompilerControl.Mode.DONT_INLINE) fun sortStringsWithComparatorSAM(): SortedSet<String> {
        val res = TreeSet<String> { a, b -> compare(a, b) }
        res.addAll(arr.subList(0, if (size < arr.size) size else arr.size))
        return res
    }
}

