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

public fun <T> Stream<T>.filterLocal(predicate: (T) -> Boolean): Stream<T> {
    return FilteringStreamLocal(this, true, predicate)
}

public fun <T, R> Stream<T>.mapLocal(transform: (T) -> R): Stream<R> {
    return TransformingStreamLocal(this, transform)
}

public class FilteringStreamLocal<T>(
        private val stream: Stream<T>, private val sendWhen: Boolean = true, private val predicate: (T) -> Boolean
                                    ) : Stream<T> {

    override fun iterator(): Iterator<T> = object : Iterator<T> {
        val iterator = stream.iterator()
        var next: T? = null

        override fun next(): T {
            return next!!
        }

        override fun hasNext(): Boolean {
            while (iterator.hasNext()) {
                val item = iterator.next()
                if (predicate(item) == sendWhen) {
                    next = item
                    return true
                }
            }
            return false
        }
    }
}

public class TransformingStreamLocal<T, R>(private val stream: Stream<T>, private val transformer: (T) -> R) : Stream<R> {
    override fun iterator(): Iterator<R> = object : Iterator<R> {
        val iterator = stream.iterator()
        override fun next(): R {
            return transformer(iterator.next())
        }
        override fun hasNext(): Boolean {
            return iterator.hasNext()
        }
    }
}
