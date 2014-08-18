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

open class Value(var value: Int)

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