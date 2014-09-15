package org.jetbrains

import kotlin.platform.platformName
import org.openjdk.jmh.annotations.*

fun classValues(size: Int): Iterable<Value> {
    return (1..size).map { Value(it) }
}
fun intValues(size: Int): Iterable<Int> {
    return 1..size
}

State(Scope.Thread)
open class SizedBenchmark {
    Param("10", "1000", "100000")
    public var size: Int = 0
}

open class Value(var value: Int) {
    val text = value.toString().reverse()
}

fun filterLoad(v: Value): Boolean {
    return v.value.toString() in v.text
}

fun mapLoad(v: Value): String = v.text.reverse()

fun filterLoad(v: Int): Boolean {
    return v.toString() in "0123456789"
}

fun mapLoad(v: Int): String = v.toString()

public inline fun Array<Value>.cnt(predicate: (Value) -> Boolean): Int {
    var count = 0
    for (element in this) {
        if (predicate(element))
            count++
    }
    return count
}

public inline fun Iterable<Value>.cnt(predicate: (Value) -> Boolean): Int {
    var count = 0
    for (element in this) {
        if (predicate(element))
            count++
    }
    return count
}

public inline fun Stream<Value>.cnt(predicate: (Value) -> Boolean): Int {
    var count = 0
    for (element in this) {
        if (predicate(element))
            count++
    }
    return count
}

platformName("countInt")
public inline fun IntArray.cnt(predicate: (Int) -> Boolean): Int {
    var count = 0
    for (element in this) {
        if (predicate(element))
            count++
    }
    return count
}

platformName("countInt")
public inline fun Iterable<Int>.cnt(predicate: (Int) -> Boolean): Int {
    var count = 0
    for (element in this) {
        if (predicate(element))
            count++
    }
    return count
}

platformName("countInt")
public inline fun Stream<Int>.cnt(predicate: (Int) -> Boolean): Int {
    var count = 0
    for (element in this) {
        if (predicate(element))
            count++
    }
    return count
}

