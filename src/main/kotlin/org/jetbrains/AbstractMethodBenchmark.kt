package org.jetbrains

import org.openjdk.jmh.annotations.*
import java.io.*
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by Mikhail.Glukhikh on 06/03/2015.
 *
 * A benchmark for a single abstract method based on a string comparison
 */

BenchmarkMode(Mode.AverageTime)
OutputTimeUnit(TimeUnit.NANOSECONDS)
open class AbstractMethodBenchmark : SizedBenchmark() {

    private fun readAllLines(fileName: String): List<String> {
        val fin: InputStream = FileInputStream(fileName)
        val reader = InputStreamReader(fin)
        val buffered = BufferedReader(reader)
        val result = ArrayList<String>()
        var line = buffered.readLine()
        while (line != null) {
            result.add(line)
            line = buffered.readLine()
        }
        return result
    }

    private val arr: List<String> = readAllLines("zdf-win.txt")
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

    inner class StringComparator: Comparator<String> {
        override fun compare(o1: String?, o2: String?): Int {
            return this@AbstractMethodBenchmark.compare(o1 ?: "", o2 ?: "")
        }
    }

    Benchmark
    CompilerControl(CompilerControl.Mode.DONT_INLINE)
    public fun sortStringsWithComparator(): SortedSet<String> {
        val res = TreeSet(StringComparator())
        res.addAll(arr.subList(0, if (size < arr.size()) size else arr.size()))
        return res
    }

    Benchmark
    CompilerControl(CompilerControl.Mode.DONT_INLINE)
    public fun sortStringsWithComparatorLambda(): SortedSet<String> {
        val res = TreeSet<String>(comparator {a, b -> compare(a, b)})
        res.addAll(arr.subList(0, if (size < arr.size()) size else arr.size()))
        return res
    }

    Benchmark
    CompilerControl(CompilerControl.Mode.DONT_INLINE)
    public fun sortStringsWithComparatorSAM(): SortedSet<String> {
        val res = TreeSet<String>(Comparator { a, b -> compare(a,b)})
        res.addAll(arr.subList(0, if (size < arr.size()) size else arr.size()))
        return res
    }
}

