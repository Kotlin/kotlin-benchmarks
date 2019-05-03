package org.jetbrains

import org.openjdk.jmh.annotations.*

fun classValues(size: Int): Iterable<Value> {
    return intValues(size).map { Value(it) }
}

fun stringValues(size: Int): Iterable<String> {
    return intValues(size).map { it.toString() }
}

fun intValues(size: Int): Iterable<Int> {
    return 1..size
}

@State(Scope.Thread)
open class SizedBenchmark {
    @Param("10", "1000", "100000") var size: Int = 0
}

@State(Scope.Thread)
open class SmallSizedBenchmark {
    @Param("10", "1000") var size: Int = 0
}

const val CONST_SIZE = 100000
const val CONST_SIZE_LONG = 100000L

open class Value(var value: Int) {
    val text = value.toString().reversed()
}

fun filterLoad(v: Value): Boolean {
    return v.value.toString() in v.text
}

fun mapLoad(v: Value): String = v.text.reversed()

fun filterLoad(v: Int): Boolean {
    return v.toString() in "0123456789"
}

fun mapLoad(v: Int): String = v.toString()

fun filterSome(v: Int): Boolean = v % 7 == 0 || v % 11 == 0

fun filterPrime(v: Int): Boolean {
    if (v <= 1)
        return false
    if (v <= 3)
        return true
    if (v % 2 == 0)
        return false
    var i = 3
    while (i*i <= v) {
        if (v % i == 0)
            return false
        i += 2
    }
    return true
}

inline fun Array<Value>.cnt(predicate: (Value) -> Boolean): Int {
    var count = 0
    for (element in this) {
        if (predicate(element))
            count++
    }
    return count
}

inline fun Iterable<Value>.cnt(predicate: (Value) -> Boolean): Int {
    var count = 0
    for (element in this) {
        if (predicate(element))
            count++
    }
    return count
}

inline fun Sequence<Value>.cnt(predicate: (Value) -> Boolean): Int {
    var count = 0
    for (element in this) {
        if (predicate(element))
            count++
    }
    return count
}

@JvmName("countInt")
inline fun IntArray.cnt(predicate: (Int) -> Boolean): Int {
    var count = 0
    for (element in this) {
        if (predicate(element))
            count++
    }
    return count
}

@JvmName("countInt")
inline fun Iterable<Int>.cnt(predicate: (Int) -> Boolean): Int {
    var count = 0
    for (element in this) {
        if (predicate(element))
            count++
    }
    return count
}

@JvmName("countInt")
inline fun Sequence<Int>.cnt(predicate: (Int) -> Boolean): Int {
    var count = 0
    for (element in this) {
        if (predicate(element))
            count++
    }
    return count
}

