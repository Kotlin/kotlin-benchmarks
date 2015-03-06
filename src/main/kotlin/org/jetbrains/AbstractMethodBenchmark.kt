package org.jetbrains

import org.openjdk.jmh.annotations.*
import java.io.IOException
import java.nio.charset.Charset
import java.nio.file.FileSystems
import java.nio.file.Files
import java.util.Arrays
import java.util.HashMap
import java.util.SortedSet
import java.util.TreeSet
import java.util.concurrent.TimeUnit

/**
 * Created by Mikhail.Glukhikh on 06/03/2015.
 *
 * A benchmark for a single abstract method based on a string comparison
 */

BenchmarkMode(Mode.AverageTime)
OutputTimeUnit(TimeUnit.NANOSECONDS)
open class AbstractMethodBenchmark : SizedBenchmark() {

    private val arr: List<String> = Files.readAllLines(FileSystems.getDefault().getPath("zdf-win.txt"),
            Charset.forName("CP1251"))

    // Produces a compiler crash see KT-6940
//    private val arr: List<String> = try {
//        Files.readAllLines(FileSystems.getDefault().getPath("zdf-win.txt"))
//    } catch (e: IOException) {
//        listOf("ёлка", "автобус", "beta", "жизнь", "удод", "колбаса")
//    }

//    private val arr = listOf("ёлка", "автобус", "beta", "жизнь", "удод", "колбаса")

    private val sequence = "абвгдеёжзийклмнопрстуфхцчшщъыьэюя"

    private val sequenceMap = HashMap<Char, Int>(); {
        var i = 0;
        for (ch in sequence) {
            sequenceMap[ch] = i++;
        }
    }

    Benchmark
    CompilerControl(CompilerControl.Mode.DONT_INLINE)
    public fun sortStrings(): SortedSet<String> {
        val res = TreeSet<String>(arr.subList(0, if (size < arr.size()) size else arr.size()))
        return res
    }

    public fun compare(a: String, b: String): Int {
        val al = a.toLowerCase()
        val bl = b.toLowerCase()
        for (i in 0..al.length()-1) {
            if (i >= bl.length()) return 1
            val ai = sequenceMap[al[i]] ?: 100
            val bi = sequenceMap[bl[i]] ?: 100
            if (ai < bi)
                return -1;
            if (ai > bi)
                return 1;
        }
        return if (al.length() < bl.length()) -1 else 0
    }

    Benchmark
    CompilerControl(CompilerControl.Mode.DONT_INLINE)
    public fun sortStringsWithComparator(): SortedSet<String> {
        val res = TreeSet<String>(comparator {a, b -> compare(a, b)})
        res.addAll(arr.subList(0, if (size < arr.size()) size else arr.size()))
        return res
    }
}

