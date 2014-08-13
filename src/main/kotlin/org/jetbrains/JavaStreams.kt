package org.jetbrains

import java.util.function.*

fun predicate<T : Any>(f: (T) -> Boolean) = object : Predicate<T> {
    override fun test(t: T?): Boolean {
        return f(t!!)
    }
}

fun longFunction<T : Any>(f: (T) -> Long) = object : ToLongFunction<T> {
    override fun applyAsLong(t: T?): Long {
        return f(t!!)
    }
}

fun function<T : Any, R : Any>(f: (T) -> R) = object : Function<T, R> {
    override fun apply(t: T?): R? {
        return f(t!!)
    }
}

fun consume<T : Any>(f: (T) -> Unit) = object : Consumer<T> {
    override fun accept(t: T?) {
        return f(t!!)
    }
}