package org.jetbrains

import java.util.NoSuchElementException

public trait Sequence<T> {
    fun hasNext(): Boolean
    fun next(): T
}

inline public fun <T> Sequence<T>.iterator(): Sequence<T> = this

// Commented because already contained inside Kotlin package
//public fun <T> Iterable<T>.sequence(): Sequence<T> = object : Sequence<T> {
//    val iterator = this@sequence.iterator()
//    override fun hasNext(): Boolean = iterator.hasNext()
//    override fun next(): T = iterator.next()
//}

inline public fun <T> Sequence<T>.filter(noinline predicate: (T) -> Boolean): Sequence<T> {
    return FilteringSequence(this, predicate)
}

inline public fun <T> Sequence<T>.toList(): List<T> {
    val list = arrayListOf<T>()
    for (it in this) list.add(it)
    return list
}

inline public fun <T, R> Sequence<T>.map(noinline transform: (T) -> R): Sequence<R> {
    return TransformingSequence(this, transform)
}

public class FilteringSequence<T>(private val stream: Sequence<T>,
                                  private val predicate: (T) -> Boolean) : Sequence<T> {

    var nextState: Int = -1 // -1 for unknown, 0 for done, 1 for continue
    var nextItem: T = null

    fun calcNext() {
        while (stream.hasNext()) {
            val item = stream.next()
            if (predicate(item)) {
                nextItem = item
                nextState = 1
                return
            }
        }
        nextState = 0
    }

    override fun next(): T {
        if (nextState == -1)
            calcNext()
        if (nextState == 0)
            throw NoSuchElementException()
        val result = nextItem
        nextItem = null
        nextState = -1
        return result
    }

    override fun hasNext(): Boolean {
        if (nextState == -1)
            calcNext()
        return nextState == 1
    }
}

public class TransformingSequence<T, R>(private val stream: Sequence<T>, private val transformer: (T) -> R) : Sequence<R> {
    override fun next(): R = transformer(stream.next())
    override fun hasNext(): Boolean = stream.hasNext()
}
