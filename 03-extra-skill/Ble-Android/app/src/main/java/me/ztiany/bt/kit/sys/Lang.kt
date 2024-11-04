@file:JvmName("Lang")

package me.ztiany.bt.kit.sys

import java.io.Closeable
import java.io.IOException

sealed class Ext<out T>(val boolean: Boolean)

data object Otherwise : Ext<Nothing>(true)

class WithData<out T>(val data: T) : Ext<T>(false)

/** 如果该对象不是 null，则执行 action。 */
inline infix fun <T, E> T?.ifNonNull(action: T.() -> E): Ext<E> {
    if (this != null) {
        return WithData(action())
    }
    return Otherwise
}

/** 如果该对象是 null，则执行 action。 */
inline infix fun <T, E> T?.ifNull(action: () -> E) = if (this == null) {
    WithData(action())
} else {
    Otherwise
}

inline infix fun <T> Boolean.yes(block: () -> T): Ext<T> = when {
    this -> {
        WithData(block())
    }

    else -> Otherwise
}

inline infix fun <T> Boolean.no(block: () -> T) = when {
    this -> Otherwise
    else -> {
        WithData(block())
    }
}

inline infix fun <T> Ext<T>.otherwise(block: () -> T): T {
    return when (this) {
        is Otherwise -> block()
        is WithData<T> -> this.data
    }
}

inline operator fun <T> Boolean.invoke(block: () -> T) = yes(block)

fun Any?.javaClassName(): String {
    return if (this == null) {
        ""
    } else {
        this::class.java.name
    }
}

inline fun ignoreCrash(code: () -> Unit) {
    try {
        code()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

/**
 * 关闭 IO。
 */
fun closeIO(closeable: Closeable?) {
    if (closeable != null) {
        try {
            closeable.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}

/**
 * 安静地关闭 IO。
 */
fun closeIOQuietly(closeable: Closeable?) {
    if (closeable != null) {
        try {
            closeable.close()
        } catch (ignored: IOException) {
        }
    }
}

/**
 * 关闭 IO。
 *
 * @param closeables closeable
 */
fun closeIOs(vararg closeables: Closeable?) {
    for (closeable in closeables) {
        closeIO(closeable)
    }
}

/**
 * 安静地关闭 IO。
 *
 * @param closeables closeable
 */
fun closeIOsQuietly(vararg closeables: Closeable?) {
    for (closeable in closeables) {
        closeIOQuietly(closeable)
    }
}