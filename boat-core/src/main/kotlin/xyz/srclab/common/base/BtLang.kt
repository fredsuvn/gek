/**
 * Boat language utilities.
 */
@file:JvmName("BtLang")

package xyz.srclab.common.base

import xyz.srclab.common.base.JumpPolicy.*
import java.util.concurrent.Callable
import java.util.function.*

/**
 * Argument to suppress kotlin compile warn.
 */
const val INAPPLICABLE_JVM_NAME = "INAPPLICABLE_JVM_NAME"

/*
 * --------------------------------------------------------------------------------
 *  Type alias start:
 * --------------------------------------------------------------------------------
 */

typealias JavaString = java.lang.String
typealias JavaBoolean = java.lang.Boolean
typealias JavaByte = java.lang.Byte
typealias JavaShort = java.lang.Short
typealias JavaChar = java.lang.Character
typealias JavaInt = java.lang.Integer
typealias JavaLong = java.lang.Long
typealias JavaFloat = java.lang.Float
typealias JavaDouble = java.lang.Double
typealias JavaVoid = java.lang.Void
typealias JavaEnum<T> = java.lang.Enum<T>
typealias JavaFunction<T, R> = java.util.function.Function<T, R>

/*
 * --------------------------------------------------------------------------------
 * Type alias end.
 * --------------------------------------------------------------------------------
 */

/*
 * --------------------------------------------------------------------------------
 *  Java functional interfaces as Kotlin functions start:
 * --------------------------------------------------------------------------------
 */

fun <T> Supplier<T>.asKotlinFun(): (() -> T) = { this.get() }
fun IntSupplier.asKotlinFun(): (() -> Int) = { this.asInt }
fun LongSupplier.asKotlinFun(): (() -> Long) = { this.asLong }
fun DoubleSupplier.asKotlinFun(): (() -> Double) = { this.asDouble }
fun <T> Predicate<T>.asKotlinFun(): (T) -> Boolean = { this.test(it) }
fun <T, R> JavaFunction<T, R>.asKotlinFun(): (T) -> R = { this.apply(it) }
fun <T> Consumer<T>.asKotlinFun(): (T) -> Unit = { this.accept(it) }
fun <T, U> BiPredicate<T, U>.asKotlinFun(): (T, U) -> Boolean = { t, u -> this.test(t, u) }
fun <T, U, R> BiFunction<T, U, R>.asKotlinFun(): (T, U) -> R = { t, u -> this.apply(t, u) }
fun <T, U> BiConsumer<T, U>.asKotlinFun(): (T, U) -> Unit = { t, u -> this.accept(t, u) }
fun <R> IntFunction<R>.asKotlinFun(): (Int) -> R = { this.apply(it) }
fun IntToLongFunction.asKotlinFun(): (Int) -> Long = { this.applyAsLong(it) }
fun IntToDoubleFunction.asKotlinFun(): (Int) -> Double = { this.applyAsDouble(it) }
fun <R> LongFunction<R>.asKotlinFun(): (Long) -> R = { this.apply(it) }
fun LongToIntFunction.asKotlinFun(): (Long) -> Int = { this.applyAsInt(it) }
fun LongToDoubleFunction.asKotlinFun(): (Long) -> Double = { this.applyAsDouble(it) }
fun <R> DoubleFunction<R>.asKotlinFun(): (Double) -> R = { this.apply(it) }
fun DoubleToIntFunction.asKotlinFun(): (Double) -> Int = { this.applyAsInt(it) }
fun DoubleToLongFunction.asKotlinFun(): (Double) -> Long = { this.applyAsLong(it) }
fun <T> IndexedPredicate<T>.asKotlinFun(): (Int, T) -> Boolean = { i, t -> this.test(i, t) }
fun <T, R> IndexedFunction<T, R>.asKotlinFun(): (Int, T) -> R = { i, t -> this.apply(i, t) }
fun <T> IndexedConsumer<T>.asKotlinFun(): (Int, T) -> Unit = { i, t -> this.accept(i, t) }
fun <T, U> IndexedBiPredicate<T, U>.asKotlinFun(): (Int, T, U) -> Boolean = { i, t, u -> this.test(i, t, u) }
fun <T, U, R> IndexedBiFunction<T, U, R>.asKotlinFun(): (Int, T, U) -> R = { i, t, u -> this.apply(i, t, u) }
fun <T, U> IndexedBiConsumer<T, U>.asKotlinFun(): (Int, T, U) -> Unit = { i, t, u -> this.accept(i, t, u) }
fun Runnable.asKotlinFun(): () -> Unit = { this.run() }
fun <V> Callable<V>.asKotlinFun(): () -> V = { this.call() }

/*
 * --------------------------------------------------------------------------------
 *  Java functional interfaces as Kotlin functions end:
 * --------------------------------------------------------------------------------
 */

/*
 * --------------------------------------------------------------------------------
 *  Kotlin functions as Java functional interfaces start:
 * --------------------------------------------------------------------------------
 */

fun <T> (() -> T).asJavaFun(): Supplier<T> = Supplier { this() }
fun <T> (() -> Int).asJavaFun(): IntSupplier = IntSupplier { this() }
fun <T> (() -> Long).asJavaFun(): LongSupplier = LongSupplier { this() }
fun <T> (() -> Double).asJavaFun(): DoubleSupplier = DoubleSupplier { this() }
fun <T> ((T) -> Boolean).asJavaFun(): Predicate<T> = Predicate { this(it) }
fun <T, R> ((T) -> R).asJavaFun(): JavaFunction<T, R> = Function { this(it) }
fun <T> ((T) -> Unit).asJavaFun(): Consumer<T> = Consumer { this(it) }
fun <T, U> ((T, U) -> Boolean).asJavaFun(): BiPredicate<T, U> = BiPredicate { t, u -> this(t, u) }
fun <T, U, R> ((T, U) -> R).asJavaFun(): BiFunction<T, U, R> = BiFunction { t, u -> this(t, u) }
fun <T, U> ((T, U) -> Unit).asJavaFun(): BiConsumer<T, U> = BiConsumer { t, u -> this(t, u) }
fun <R> ((Int) -> R).asJavaFun(): IntFunction<R> = IntFunction { this(it) }
fun ((Int) -> Long).asJavaFun(): IntToLongFunction = IntToLongFunction { this(it) }
fun ((Int) -> Double).asJavaFun(): IntToDoubleFunction = IntToDoubleFunction { this(it) }
fun <R> ((Long) -> R).asJavaFun(): LongFunction<R> = LongFunction { this(it) }
fun ((Long) -> Int).asJavaFun(): LongToIntFunction = LongToIntFunction { this(it) }
fun ((Long) -> Double).asJavaFun(): LongToDoubleFunction = LongToDoubleFunction { this(it) }
fun <R> ((Double) -> R).asJavaFun(): DoubleFunction<R> = DoubleFunction { this(it) }
fun ((Double) -> Int).asJavaFun(): DoubleToIntFunction = DoubleToIntFunction { this(it) }
fun ((Double) -> Long).asJavaFun(): DoubleToLongFunction = DoubleToLongFunction { this(it) }
fun <T> ((Int, T) -> Boolean).asJavaFun(): IndexedPredicate<T> = IndexedPredicate { i, it -> this(i, it) }
fun <T, R> ((Int, T) -> R).asJavaFun(): IndexedFunction<T, R> = IndexedFunction { i, it -> this(i, it) }
fun <T> ((Int, T) -> Unit).asJavaFun(): IndexedConsumer<T> = IndexedConsumer { i, t -> this(i, t) }
fun <T, U> ((Int, T, U) -> Boolean).asJavaFun(): IndexedBiPredicate<T, U> = IndexedBiPredicate() { i, t, u -> this(i, t, u) }
fun <T, U, R> ((Int, T, U) -> R).asJavaFun(): IndexedBiFunction<T, U, R> = IndexedBiFunction { i, t, u -> this(i, t, u) }
fun <T, U> ((Int, T, U) -> Unit).asJavaFun(): IndexedBiConsumer<T, U> = IndexedBiConsumer { i, t, u -> this(i, t, u) }
fun (() -> Any?).asRunnable(): Runnable = Runnable { this() }
fun <R> (() -> R).asCallable(): Callable<R> = Callable { this() }

/*
 * --------------------------------------------------------------------------------
 *  Kotlin functions as Java functional interfaces end:
 * --------------------------------------------------------------------------------
 */

/*
 * --------------------------------------------------------------------------------
 *  Extension Java functional interfaces start:
 * --------------------------------------------------------------------------------
 */

/**
 * Functional interface represents [java.util.function.Predicate] with index.
 */
fun interface IndexedPredicate<T> {
    /**
     * Tests [t] with [index].
     */
    fun test(index: Int, t: T): Boolean
}

/**
 * Functional interface represents [java.util.function.Function] with index.
 */
fun interface IndexedFunction<T, R> {
    /**
     * Applies [t] with [index].
     */
    fun apply(index: Int, t: T): R
}

/**
 * Functional interface represents [java.util.function.Consumer] with index.
 */
fun interface IndexedConsumer<T> {
    /**
     * Accepts [t] with [index].
     */
    fun accept(index: Int, t: T)
}

/**
 * Functional interface represents [java.util.function.BiPredicate] with index.
 */
fun interface IndexedBiPredicate<T, U> {
    /**
     * Tests [t], [u] with [index].
     */
    fun test(index: Int, t: T, u: U): Boolean
}

/**
 * Functional interface represents [java.util.function.BiFunction] with index.
 */
fun interface IndexedBiFunction<T, U, R> {
    /**
     * Applies [t], [u] with [index].
     */
    fun apply(index: Int, t: T, u: U): R
}

/**
 * Functional interface represents [java.util.function.BiConsumer] with index.
 */
fun interface IndexedBiConsumer<T, U> {
    /**
     * Accepts [t], [u] with [index].
     */
    fun accept(index: Int, t: T, u: U)
}

/*
 * --------------------------------------------------------------------------------
 *  Extension Java functional interfaces end:
 * --------------------------------------------------------------------------------
 */

/*
 * --------------------------------------------------------------------------------
 *  Policies start:
 * --------------------------------------------------------------------------------
 */

/**
 * Policy of jump statement for process control: [CONTINUE], [BREAK], [RETURN] and [GO_ON].
 */
enum class JumpPolicy {

    /**
     * Stops the current execution of the iteration and proceeds to the next iteration in the loop.
     */
    CONTINUE,

    /**
     * Stops the current loop and breaks out.
     */
    BREAK,

    /**
     * Stops the current execution of the method and returns.
     */
    RETURN,

    /**
     * Goes on the current execution, without stopping.
     */
    GO_ON,
    ;

    fun isContinue(): Boolean {
        return this == CONTINUE
    }

    fun isBreak(): Boolean {
        return this == BREAK
    }

    fun isReturn(): Boolean {
        lazy { }
        return this == RETURN
    }
}

/**
 * Policy for thread-safe.
 */
enum class ThreadSafePolicy {

    /**
     * Synchronized.
     */
    SYNCHRONIZED,

    /**
     * Concurrent.
     */
    CONCURRENT,

    /**
     * Thread-local.
     */
    THREAD_LOCAL,

    /**
     * Copy-on-write.
     */
    COPY_ON_WRITE,

    /**
     * No thread-safe.
     */
    NONE,
    ;
}

/*
 * --------------------------------------------------------------------------------
 *  Policies end.
 * --------------------------------------------------------------------------------
 */