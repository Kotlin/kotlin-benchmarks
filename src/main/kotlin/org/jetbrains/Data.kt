package org.jetbrains

import kotlin.platform.platformName

val SIZE = 1000000
fun stringValues(): Iterable<String> = (1..SIZE).map { it.toString() }
fun intValues(): Iterable<Int> = 1..SIZE

public inline fun Array<String>.cnt(predicate: (String) -> Boolean): Int {
    var count = 0
    for (element in this) {
        if (predicate(element))
            count++
    }
    return count
}

public inline fun Iterable<String>.cnt(predicate: (String) -> Boolean): Int {
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