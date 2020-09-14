package xyz.srclab.common.collection

import xyz.srclab.common.base.Check
import xyz.srclab.common.base.Require
import xyz.srclab.common.base.Sort
import xyz.srclab.common.base.To
import java.math.BigDecimal
import java.math.BigInteger
import java.util.*
import java.util.stream.Stream
import java.util.stream.StreamSupport
import kotlin.collections.LinkedHashMap
import kotlin.random.Random

abstract class BaseIterableOps<T, CX : Iterable<T>, O : BaseIterableOps<T, CX, O>> 
protected constructor(operated: CX) {

    protected  var iterable: CX? = operated
    protected  var sequence: Sequence<T>? = null
    
    inline fun find( predicate: (T) -> Boolean): T? {
        return getC().find(predicate)
    }

    
    inline fun findLast( predicate: (T) -> Boolean): T? {
        return getC().findLast(predicate)
    }

    
    fun first(): T {
        return getC().first()
    }

    
    inline fun first( predicate: (T) -> Boolean): T {
        return getC().first(predicate)
    }

    
    fun firstOrNull(): T? {
        return getC().firstOrNull()
    }

    
    inline fun firstOrNull( predicate: (T) -> Boolean): T? {
        return getC().firstOrNull(predicate)
    }

    
    fun last(): T {
        return getC().last()
    }

    
    inline fun last( predicate: (T) -> Boolean): T {
        return getC().last(predicate)
    }

    
    fun lastOrNull(): T? {
        return getC().lastOrNull()
    }

    
    inline fun lastOrNull( predicate: (T) -> Boolean): T? {
        return getC().lastOrNull(predicate)
    }

    
    inline fun all( predicate: (T) -> Boolean): Boolean {
        return getC().all(predicate)
    }

    
    fun any(): Boolean {
        return getC().any()
    }

    
    inline fun any( predicate: (T) -> Boolean): Boolean {
        return getC().any(predicate)
    }

    
    fun none(): Boolean {
        return getC().none()
    }

    
    inline fun none( predicate: (T) -> Boolean): Boolean {
        return getC().none(predicate)
    }

    
    fun single(): T {
        return getC().single()
    }

    
    inline fun single( predicate: (T) -> Boolean): T {
        return getC().single(predicate)
    }

    
    fun singleOrNull(): T? {
        return getC().singleOrNull()
    }

    
    inline fun singleOrNull( predicate: (T) -> Boolean): T? {
        return getC().singleOrNull(predicate)
    }

    
    fun contains( element: T): Boolean {
        return getC().contains(element)
    }

    
    fun count(): Int {
        return getC().count()
    }

    
    inline fun count( predicate: (T) -> Boolean): Int {
        return getC().count(predicate)
    }

    
    fun elementAt( index: Int): T {
        return getC().elementAt(index)
    }

    
    fun elementAtOrElse(
        
        index: Int,
        defaultValue: (index: Int) -> T
    ): T {
        return getC().elementAtOrElse(index, defaultValue)
    }

    
    fun elementAtOrNull( index: Int): T? {
        return getC().elementAtOrNull(index)
    }

    
    fun indexOf( element: T): Int {
        return getC().indexOf(element)
    }

    
    inline fun indexOf( predicate: (T) -> Boolean): Int {
        return getC().indexOfFirst(predicate)
    }

    
    fun lastIndexOf( element: T): Int {
        return getC().lastIndexOf(element)
    }

    
    inline fun lastIndexOf( predicate: (T) -> Boolean): Int {
        return getC().indexOfLast(predicate)
    }

    
    fun drop( n: Int): List<T> {
        return getC().drop(n)
    }

    
    inline fun dropWhile( predicate: (T) -> Boolean): List<T> {
        return getC().dropWhile(predicate)
    }

    
    fun take( n: Int): List<T> {
        return getC().take(n)
    }

    
    inline fun takeWhile( predicate: (T) -> Boolean): List<T> {
        return getC().takeWhile(predicate)
    }

    
    inline fun filter( predicate: (T) -> Boolean): List<T> {
        return getC().filter(predicate)
    }

    
    inline fun filterIndexed( predicate: (index: Int, T) -> Boolean): List<T> {
        return getC().filterIndexed(predicate)
    }

    
//    fun <T : Any> filterNotNull(iterable: Iterable<T?>): List<T> {
//        return getC().filterNotNull()
//    }

    
    inline fun <C : MutableCollection<in T>> filterTo(
        
        destination: C,
        predicate: (T) -> Boolean
    ): C {
        return getC().filterTo(destination, predicate)
    }

    
    inline fun <C : MutableCollection<in T>> filterIndexedTo(
        
        destination: C,
        predicate: (index: Int, T) -> Boolean
    ): C {
        return getC().filterIndexedTo(destination, predicate)
    }

    
//    fun <C : MutableCollection<in T>, T : Any> filterNotNullTo(
//        iterable: Iterable<T?>,
//        destination: C
//    ): C {
//        return getC().filterNotNullTo(destination)
//    }

    
    inline fun <R> map( transform: (T) -> R): List<R> {
        return getC().map(transform)
    }

    
    inline fun <R> mapIndexed( transform: (index: Int, T) -> R): List<R> {
        return getC().mapIndexed(transform)
    }

    
    inline fun <R> mapNotNull( transform: (T) -> R): List<R> {
        return getC().mapNotNull(transform)
    }

    
    inline fun <R> mapIndexedNotNull( transform: (index: Int, T) -> R): List<R> {
        return getC().mapIndexedNotNull(transform)
    }

    
    inline fun <R, C : MutableCollection<in R>> mapTo(
        
        destination: C,
        transform: (T) -> R
    ): C {
        return getC().mapTo(destination, transform)
    }

    
    inline fun <R, C : MutableCollection<in R>> mapIndexedTo(
        
        destination: C,
        transform: (index: Int, T) -> R
    ): C {
        return getC().mapIndexedTo(destination, transform)
    }

    
    inline fun <R : Any, C : MutableCollection<in R>> mapNotNullTo(
        
        destination: C,
        transform: (T) -> R?
    ): C {
        return getC().mapNotNullTo(destination, transform)
    }

    
    inline fun <R : Any, C : MutableCollection<in R>> mapIndexedNotNullTo(
        
        destination: C,
        transform: (index: Int, T) -> R?
    ): C {
        return getC().mapIndexedNotNullTo(destination, transform)
    }

    
    inline fun <R> flatMap( transform: (T) -> Iterable<R>): List<R> {
        return getC().flatMap(transform)
    }

    
    inline fun <R> flatMapIndexed( transform: (index: Int, T) -> Iterable<R>): List<R> {
        return getC().flatMapIndexed(transform)
    }

    
    inline fun <R, C : MutableCollection<in R>> flatMapTo(
        
        destination: C,
        transform: (T) -> Iterable<R>
    ): C {
        return getC().flatMapTo(destination, transform)
    }

    
    inline fun <R, C : MutableCollection<in R>> flatMapIndexedTo(
        
        destination: C,
        transform: (index: Int, T) -> Iterable<R>
    ): C {
        return getC().flatMapIndexedTo(destination, transform)
    }

    
    inline fun <S, T : S> reduce( operation: (S, T) -> S): S {
        return getC().reduce(operation)
    }

    
    inline fun <S, T : S> reduceIndexed( operation: (index: Int, S, T) -> S): S {
        return getC().reduceIndexed(operation)
    }

    
    inline fun <S, T : S> reduceOrNull( operation: (S, T) -> S): S? {
        return getC().reduceOrNull(operation)
    }

    
    inline fun <S, T : S> reduceIndexedOrNull( operation: (index: Int, S, T) -> S): S? {
        return getC().reduceIndexedOrNull(operation)
    }

    
    inline fun <R> reduce(
        
        initial: R,
        operation: (R, T) -> R
    ): R {
        return getC().fold(initial, operation)
    }

    
    inline fun <R> reduceIndexed(
        
        initial: R,
        operation: (index: Int, R, T) -> R
    ): R {
        return getC().foldIndexed(initial, operation)
    }

    
    inline fun <K, V> associate(
        
        keySelector: (T) -> K,
        valueTransform: (T) -> V
    ): Map<K, V> {
        return getC().associateBy(keySelector, valueTransform)
    }

    
    inline fun <K, V> associate( transform: (T) -> Pair<K, V>): Map<K, V> {
        return getC().associate(transform)
    }

    
    inline fun <K> associateKey( keySelector: (T) -> K): Map<K, T> {
        return getC().associateBy(keySelector)
    }

    
    inline fun <V> associateValue( valueSelector: (T) -> V): Map<V> {
        return getC().associateWith(valueSelector)
    }

    
    inline fun <K, V> associateWithNext(
        
        keySelector: (T) -> K,
        valueTransform: (T?) -> V
    ): Map<K, V> {
        return associateWithNextTo(iterable, LinkedHashMap(), keySelector, valueTransform)
    }

    
    inline fun <K, V> associateWithNext( transform: (T, T?) -> Pair<K, V>): Map<K, V> {
        return associateWithNextTo(iterable, LinkedHashMap(), transform)
    }

    
    inline fun <K, V, M : MutableMap<in K, in V>> associateTo(
        
        destination: M,
        keySelector: (T) -> K,
        valueTransform: (T) -> V
    ): M {
        return getC().associateByTo(destination, keySelector, valueTransform)
    }

    
    inline fun <K, V, M : MutableMap<in K, in V>> associateTo(
        
        destination: M,
        transform: (T) -> Pair<K, V>
    ): M {
        return getC().associateTo(destination, transform)
    }

    
    inline fun <K, M : MutableMap<in K, in T>> associateKeyTo(
        
        destination: M,
        keySelector: (T) -> K
    ): M {
        return getC().associateByTo(destination, keySelector)
    }

    
    inline fun <V, M : MutableMap<in T, in V>> associateValueTo(
        
        destination: M,
        valueSelector: (T) -> V
    ): M {
        return getC().associateWithTo(destination, valueSelector)
    }

    
    inline fun <K, V, M : MutableMap<in K, in V>> associateWithNextTo(
        
        destination: M,
        keySelector: (T) -> K,
        valueTransform: (T?) -> V
    ): M {
        val iterator = getC().iterator()
        while (iterator.hasNext()) {
            val tk = iterator.next()
            val k = keySelector(tk)
            if (iterator.hasNext()) {
                val tv = iterator.next()
                val v = valueTransform(tv)
                destination.put(k, v)
            } else {
                val v = valueTransform(null)
                destination.put(k, v)
                break
            }
        }
        return destination
    }

    
    inline fun <K, V, M : MutableMap<in K, in V>> associateWithNextTo(
        
        destination: M,
        transform: (T, T?) -> Pair<K, V>
    ): M {
        val iterator = getC().iterator()
        while (iterator.hasNext()) {
            val tk = iterator.next()
            if (iterator.hasNext()) {
                val tv = iterator.next()
                val pair = transform(tk, tv)
                destination.put(pair.first, pair.second)
            } else {
                val pair = transform(tk, null)
                destination.put(pair.first, pair.second)
                break
            }
        }
        return destination
    }

    
    inline fun <K> groupBy( keySelector: (T) -> K): Map<K, List<T>> {
        return getC().groupBy(keySelector)
    }

    
    inline fun <K, V> groupBy(
        
        keySelector: (T) -> K,
        valueTransform: (T) -> V
    ): Map<K, List<V>> {
        return getC().groupBy(keySelector, valueTransform)
    }

    
    inline fun <K, M : MutableMap<in K, MutableList<T>>> groupByTo(
        
        destination: M,
        keySelector: (T) -> K
    ): M {
        return getC().groupByTo(destination, keySelector)
    }

    
    inline fun <K, V, M : MutableMap<in K, MutableList<V>>> groupByTo(
        
        destination: M,
        keySelector: (T) -> K,
        valueTransform: (T) -> V
    ): M {
        return getC().groupByTo(destination, keySelector, valueTransform)
    }

    
    fun chunked( size: Int): List<List<T>> {
        return getC().chunked(size)
    }

    
    fun <R> chunked(
        
        size: Int,
        transform: (List<T>) -> R
    ): List<R> {
        return getC().chunked(size, transform)
    }

    
    fun windowed(
        
        size: Int,
        step: Int = 1,
        partialWindows: Boolean = false
    ): List<List<T>> {
        return getC().windowed(size, step, partialWindows)
    }

    
    fun <R> windowed(
        
        size: Int,
        step: Int = 1,
        partialWindows: Boolean = false,
        transform: (List<T>) -> R
    ): List<R> {
        return getC().windowed(size, step, partialWindows, transform)
    }

    
    inline fun <R, V> zip(
        
        other: Array<out R>,
        transform: (T, R) -> V
    ): List<V> {
        return getC().zip(other, transform)
    }

    
    inline fun <R, V> zip(
        
        other: Iterable<R>,
        transform: (T, R) -> V
    ): List<V> {
        return getC().zip(other, transform)
    }

    
    inline fun <R> zipWithNext( transform: (T, T) -> R): List<R> {
        return getC().zipWithNext(transform)
    }

    
    fun <T : Comparable<T>> max(): T {
        return Require.notNull(maxOrNull(iterable))
    }

    
    fun <T : Comparable<T>> maxOrNull(): T? {
        return getC().maxOrNull()
    }

    
    fun max( comparator: Comparator<in T>): T {
        return Require.notNull(maxOrNull(iterable, comparator))
    }

    
    fun maxOrNull( comparator: Comparator<in T>): T? {
        return getC().maxWithOrNull(comparator)
    }

    
    fun <T : Comparable<T>> min(): T {
        return Require.notNull(minOrNull(iterable))
    }

    
    fun <T : Comparable<T>> minOrNull(): T? {
        return getC().minOrNull()
    }

    
    fun min( comparator: Comparator<in T>): T {
        return Require.notNull(minOrNull(iterable, comparator))
    }

    
    fun minOrNull( comparator: Comparator<in T>): T? {
        return getC().minWithOrNull(comparator)
    }

    
    fun sumInt(): Int {
        return sumInt(iterable) { To.toInt(it) }
    }

    
    fun sumLong(): Long {
        return sumLong(iterable) { To.toLong(it) }
    }

    
    fun sumDouble(): Double {
        return sumDouble(iterable) { To.toDouble(it) }
    }

    
    fun sumBigInteger(): BigInteger {
        return sumBigInteger(iterable) { To.toBigInteger(it) }
    }

    
    fun sumBigDecimal(): BigDecimal {
        return sumBigDecimal(iterable) { To.toBigDecimal(it) }
    }

    
    inline fun sumInt( selector: (T) -> Int): Int {
        return getC().sumOf(selector)
    }

    
    inline fun sumLong( selector: (T) -> Long): Long {
        return getC().sumOf(selector)
    }

    
    fun sumDouble( selector: (T) -> Double): Double {
        return getC().sumOf(selector)
    }

    
    fun sumBigInteger( selector: (T) -> BigInteger): BigInteger {
        return getC().sumOf(selector)
    }

    
    fun sumBigDecimal( selector: (T) -> BigDecimal): BigDecimal {
        return getC().sumOf(selector)
    }

    
    fun intersect( other: Iterable<T>): Set<T> {
        return getC().intersect(other)
    }

    
    fun union( other: Iterable<T>): Set<T> {
        return getC().union(other)
    }

    
    fun subtract( other: Iterable<T>): Set<T> {
        return getC().subtract(other)
    }

    
    fun reversed(): List<T> {
        return getC().reversed()
    }

    
    fun <T : Comparable<T>> sorted(): List<T> {
        return getC().sorted()
    }

    
    fun sorted( comparator: Comparator<in T>): List<T> {
        return getC().sortedWith(comparator)
    }

    
    fun shuffled(): List<T> {
        return getC().shuffled()
    }

    
    fun shuffled( random: Random): List<T> {
        return getC().shuffled(random)
    }

    
    fun distinct(): List<T> {
        return getC().distinct()
    }

    
    inline fun <K> distinct( selector: (T) -> K): List<T> {
        return getC().distinctBy(selector)
    }

    
    inline fun forEachIndexed( action: (index: Int, T) -> Unit) {
        return getC().forEachIndexed(action)
    }

    
    fun removeAll(iterable: MutableIterable<T>, predicate: (T) -> Boolean): Boolean {
        return getC().removeAll(predicate)
    }

    
    fun removeFirst(iterable: MutableIterable<T>): T {
        val iterator = getC().iterator()
        Check.checkElement(iterator.hasNext(), "Iterable is empty.")
        val first = iterator.next()
        iterator.remove()
        return first
    }

    
    fun removeFirstOrNull(iterable: MutableIterable<T>): T? {
        val iterator = getC().iterator()
        if (!iterator.hasNext()) {
            return null
        }
        val first = iterator.next()
        iterator.remove()
        return first
    }

    
    fun removeLast(iterable: MutableIterable<T>): T {
        val iterator = getC().iterator()
        Check.checkElement(iterator.hasNext(), "Iterable is empty.")
        var last = iterator.next()
        while (iterator.hasNext()) {
            last = iterator.next()
        }
        iterator.remove()
        return last
    }

    
    fun removeLastOrNull(iterable: MutableIterable<T>): T? {
        val iterator = getC().iterator()
        if (!iterator.hasNext()) {
            return null
        }
        var last = iterator.next()
        while (iterator.hasNext()) {
            last = iterator.next()
        }
        iterator.remove()
        return last
    }

    
    fun retainAll(iterable: MutableIterable<T>, predicate: (T) -> Boolean): Boolean {
        return getC().retainAll(predicate)
    }

    
    fun plus( element: T): List<T> {
        return getC().plus(element)
    }

    
    fun plus( elements: Array<out T>): List<T> {
        return getC().plus(elements)
    }

    
    fun plus( elements: Iterable<T>): List<T> {
        return getC().plus(elements)
    }

    
    fun minus( element: T): List<T> {
        return getC().minus(element)
    }

    
    fun minus( elements: Array<out T>): List<T> {
        return getC().minus(elements)
    }

    
    fun minus( elements: Iterable<T>): List<T> {
        return getC().minus(elements)
    }

    
    fun <C : MutableCollection<in T>> toCollection( destination: C): C {
        return getC().toCollection(destination)
    }

    
    fun toSet(): Set<T> {
        return getC().toSet()
    }

    
    fun toMutableSet(): MutableSet<T> {
        return getC().toMutableSet()
    }

    
    fun toHashSet(): HashSet<T> {
        return getC().toHashSet()
    }

    
    fun toSortedSet(): SortedSet<T> {
        return toSortedSet(iterable, Sort.selfComparableComparator())
    }

    
    fun toSortedSet( comparator: Comparator<in T>): SortedSet<T> {
        return getC().toSortedSet(comparator)
    }

    
    fun toList(): List<T> {
        return getC().toList()
    }

    
    fun toMutableList(): MutableList<T> {
        return getC().toMutableList()
    }

    
    fun toStream(): Stream<T> {
        return toStream(iterable, false)
    }

    
    fun toStream( parallel: Boolean): Stream<T> {
        return StreamSupport.stream(getC().spliterator(), parallel)
    }

    
    fun toArray(): Array<Any?> {
        val list = toList(iterable)
        val result = arrayOfNulls<Any?>(list.size)
        list.forEachIndexed { i, t -> result[i] = t }
        return result
    }

    
    inline fun toArray( generator: (size: Int) -> Array<T>): Array<T> {
        val list = toList(iterable)
        val result = generator(list.size)
        list.forEachIndexed { i, t -> result[i] = t }
        return result
    }

    
    fun toBooleanArray(): BooleanArray {
        return toBooleanArray(iterable) { To.toBoolean(it) }
    }

    
    inline fun toBooleanArray( selector: (T) -> Boolean): BooleanArray {
        val list = toList(iterable)
        val result = BooleanArray(list.size)
        list.forEachIndexed { i, t -> result[i] = selector(t) }
        return result
    }

    
    fun toByteArray(): ByteArray {
        return toByteArray(iterable) { To.toByte(it) }
    }

    
    inline fun toByteArray( selector: (T) -> Byte): ByteArray {
        val list = toList(iterable)
        val result = ByteArray(list.size)
        list.forEachIndexed { i, t -> result[i] = selector(t) }
        return result
    }

    
    fun toShortArray(): ShortArray {
        return toShortArray(iterable) { To.toShort(it) }
    }

    
    inline fun toShortArray( selector: (T) -> Short): ShortArray {
        val list = toList(iterable)
        val result = ShortArray(list.size)
        list.forEachIndexed { i, t -> result[i] = selector(t) }
        return result
    }

    
    fun toCharArray(): CharArray {
        return toCharArray(iterable) { To.toChar(it) }
    }

    
    inline fun toCharArray( selector: (T) -> Char): CharArray {
        val list = toList(iterable)
        val result = CharArray(list.size)
        list.forEachIndexed { i, t -> result[i] = selector(t) }
        return result
    }

    
    fun toIntArray(): IntArray {
        return toIntArray(iterable) { To.toInt(it) }
    }

    
    inline fun toIntArray( selector: (T) -> Int): IntArray {
        val list = toList(iterable)
        val result = IntArray(list.size)
        list.forEachIndexed { i, t -> result[i] = selector(t) }
        return result
    }

    
    fun toLongArray(): LongArray {
        return toLongArray(iterable) { To.toLong(it) }
    }

    
    inline fun toLongArray( selector: (T) -> Long): LongArray {
        val list = toList(iterable)
        val result = LongArray(list.size)
        list.forEachIndexed { i, t -> result[i] = selector(t) }
        return result
    }

    
    fun toFloatArray(): FloatArray {
        return toFloatArray(iterable) { To.toFloat(it) }
    }

    
    inline fun toFloatArray( selector: (T) -> Float): FloatArray {
        val list = toList(iterable)
        val result = FloatArray(list.size)
        list.forEachIndexed { i, t -> result[i] = selector(t) }
        return result
    }

    
    fun toDoubleArray(): DoubleArray {
        return toDoubleArray(iterable) { To.toDouble(it) }
    }

    
    inline fun toDoubleArray( selector: (T) -> Double): DoubleArray {
        val list = toList(iterable)
        val result = DoubleArray(list.size)
        list.forEachIndexed { i, t -> result[i] = selector(t) }
        return result
    }
    
    abstract fun getC(): CX

    companion object {

        @JvmStatic
        inline fun <T> find(iterable: Iterable<T>, predicate: (T) -> Boolean): T? {
            return iterable.find(predicate)
        }

        @JvmStatic
        inline fun <T> findLast(iterable: Iterable<T>, predicate: (T) -> Boolean): T? {
            return iterable.findLast(predicate)
        }

        @JvmStatic
        fun <T> first(iterable: Iterable<T>): T {
            return iterable.first()
        }

        @JvmStatic
        inline fun <T> first(iterable: Iterable<T>, predicate: (T) -> Boolean): T {
            return iterable.first(predicate)
        }

        @JvmStatic
        fun <T> firstOrNull(iterable: Iterable<T>): T? {
            return iterable.firstOrNull()
        }

        @JvmStatic
        inline fun <T> firstOrNull(iterable: Iterable<T>, predicate: (T) -> Boolean): T? {
            return iterable.firstOrNull(predicate)
        }

        @JvmStatic
        fun <T> last(iterable: Iterable<T>): T {
            return iterable.last()
        }

        @JvmStatic
        inline fun <T> last(iterable: Iterable<T>, predicate: (T) -> Boolean): T {
            return iterable.last(predicate)
        }

        @JvmStatic
        fun <T> lastOrNull(iterable: Iterable<T>): T? {
            return iterable.lastOrNull()
        }

        @JvmStatic
        inline fun <T> lastOrNull(iterable: Iterable<T>, predicate: (T) -> Boolean): T? {
            return iterable.lastOrNull(predicate)
        }

        @JvmStatic
        inline fun <T> all(iterable: Iterable<T>, predicate: (T) -> Boolean): Boolean {
            return iterable.all(predicate)
        }

        @JvmStatic
        fun <T> any(iterable: Iterable<T>): Boolean {
            return iterable.any()
        }

        @JvmStatic
        inline fun <T> any(iterable: Iterable<T>, predicate: (T) -> Boolean): Boolean {
            return iterable.any(predicate)
        }

        @JvmStatic
        fun <T> none(iterable: Iterable<T>): Boolean {
            return iterable.none()
        }

        @JvmStatic
        inline fun <T> none(iterable: Iterable<T>, predicate: (T) -> Boolean): Boolean {
            return iterable.none(predicate)
        }

        @JvmStatic
        fun <T> single(iterable: Iterable<T>): T {
            return iterable.single()
        }

        @JvmStatic
        inline fun <T> single(iterable: Iterable<T>, predicate: (T) -> Boolean): T {
            return iterable.single(predicate)
        }

        @JvmStatic
        fun <T> singleOrNull(iterable: Iterable<T>): T? {
            return iterable.singleOrNull()
        }

        @JvmStatic
        inline fun <T> singleOrNull(iterable: Iterable<T>, predicate: (T) -> Boolean): T? {
            return iterable.singleOrNull(predicate)
        }

        @JvmStatic
        fun <T> contains(iterable: Iterable<T>, element: T): Boolean {
            return iterable.contains(element)
        }

        @JvmStatic
        fun <T> count(iterable: Iterable<T>): Int {
            return iterable.count()
        }

        @JvmStatic
        inline fun <T> count(iterable: Iterable<T>, predicate: (T) -> Boolean): Int {
            return iterable.count(predicate)
        }

        @JvmStatic
        fun <T> elementAt(iterable: Iterable<T>, index: Int): T {
            return iterable.elementAt(index)
        }

        @JvmStatic
        fun <T> elementAtOrElse(
            iterable: Iterable<T>,
            index: Int,
            defaultValue: (index: Int) -> T
        ): T {
            return iterable.elementAtOrElse(index, defaultValue)
        }

        @JvmStatic
        fun <T> elementAtOrNull(iterable: Iterable<T>, index: Int): T? {
            return iterable.elementAtOrNull(index)
        }

        @JvmStatic
        fun <T> indexOf(iterable: Iterable<T>, element: T): Int {
            return iterable.indexOf(element)
        }

        @JvmStatic
        inline fun <T> indexOf(iterable: Iterable<T>, predicate: (T) -> Boolean): Int {
            return iterable.indexOfFirst(predicate)
        }

        @JvmStatic
        fun <T> lastIndexOf(iterable: Iterable<T>, element: T): Int {
            return iterable.lastIndexOf(element)
        }

        @JvmStatic
        inline fun <T> lastIndexOf(iterable: Iterable<T>, predicate: (T) -> Boolean): Int {
            return iterable.indexOfLast(predicate)
        }

        @JvmStatic
        fun <T> drop(iterable: Iterable<T>, n: Int): List<T> {
            return iterable.drop(n)
        }

        @JvmStatic
        inline fun <T> dropWhile(iterable: Iterable<T>, predicate: (T) -> Boolean): List<T> {
            return iterable.dropWhile(predicate)
        }

        @JvmStatic
        fun <T> take(iterable: Iterable<T>, n: Int): List<T> {
            return iterable.take(n)
        }

        @JvmStatic
        inline fun <T> takeWhile(iterable: Iterable<T>, predicate: (T) -> Boolean): List<T> {
            return iterable.takeWhile(predicate)
        }

        @JvmStatic
        inline fun <T> filter(iterable: Iterable<T>, predicate: (T) -> Boolean): List<T> {
            return iterable.filter(predicate)
        }

        @JvmStatic
        inline fun <T> filterIndexed(iterable: Iterable<T>, predicate: (index: Int, T) -> Boolean): List<T> {
            return iterable.filterIndexed(predicate)
        }

        @JvmStatic
        fun <T : Any> filterNotNull(iterable: Iterable<T?>): List<T> {
            return iterable.filterNotNull()
        }

        @JvmStatic
        inline fun <T, C : MutableCollection<in T>> filterTo(
            iterable: Iterable<T>,
            destination: C,
            predicate: (T) -> Boolean
        ): C {
            return iterable.filterTo(destination, predicate)
        }

        @JvmStatic
        inline fun <T, C : MutableCollection<in T>> filterIndexedTo(
            iterable: Iterable<T>,
            destination: C,
            predicate: (index: Int, T) -> Boolean
        ): C {
            return iterable.filterIndexedTo(destination, predicate)
        }

        @JvmStatic
        fun <C : MutableCollection<in T>, T : Any> filterNotNullTo(
            iterable: Iterable<T?>,
            destination: C
        ): C {
            return iterable.filterNotNullTo(destination)
        }

        @JvmStatic
        inline fun <T, R> map(iterable: Iterable<T>, transform: (T) -> R): List<R> {
            return iterable.map(transform)
        }

        @JvmStatic
        inline fun <T, R> mapIndexed(iterable: Iterable<T>, transform: (index: Int, T) -> R): List<R> {
            return iterable.mapIndexed(transform)
        }

        @JvmStatic
        inline fun <T, R> mapNotNull(iterable: Iterable<T>, transform: (T) -> R): List<R> {
            return iterable.mapNotNull(transform)
        }

        @JvmStatic
        inline fun <T, R> mapIndexedNotNull(iterable: Iterable<T>, transform: (index: Int, T) -> R): List<R> {
            return iterable.mapIndexedNotNull(transform)
        }

        @JvmStatic
        inline fun <T, R, C : MutableCollection<in R>> mapTo(
            iterable: Iterable<T>,
            destination: C,
            transform: (T) -> R
        ): C {
            return iterable.mapTo(destination, transform)
        }

        @JvmStatic
        inline fun <T, R, C : MutableCollection<in R>> mapIndexedTo(
            iterable: Iterable<T>,
            destination: C,
            transform: (index: Int, T) -> R
        ): C {
            return iterable.mapIndexedTo(destination, transform)
        }

        @JvmStatic
        inline fun <T, R : Any, C : MutableCollection<in R>> mapNotNullTo(
            iterable: Iterable<T>,
            destination: C,
            transform: (T) -> R?
        ): C {
            return iterable.mapNotNullTo(destination, transform)
        }

        @JvmStatic
        inline fun <T, R : Any, C : MutableCollection<in R>> mapIndexedNotNullTo(
            iterable: Iterable<T>,
            destination: C,
            transform: (index: Int, T) -> R?
        ): C {
            return iterable.mapIndexedNotNullTo(destination, transform)
        }

        @JvmStatic
        inline fun <T, R> flatMap(iterable: Iterable<T>, transform: (T) -> Iterable<R>): List<R> {
            return iterable.flatMap(transform)
        }

        @JvmStatic
        inline fun <T, R> flatMapIndexed(iterable: Iterable<T>, transform: (index: Int, T) -> Iterable<R>): List<R> {
            return iterable.flatMapIndexed(transform)
        }

        @JvmStatic
        inline fun <T, R, C : MutableCollection<in R>> flatMapTo(
            iterable: Iterable<T>,
            destination: C,
            transform: (T) -> Iterable<R>
        ): C {
            return iterable.flatMapTo(destination, transform)
        }

        @JvmStatic
        inline fun <T, R, C : MutableCollection<in R>> flatMapIndexedTo(
            iterable: Iterable<T>,
            destination: C,
            transform: (index: Int, T) -> Iterable<R>
        ): C {
            return iterable.flatMapIndexedTo(destination, transform)
        }

        @JvmStatic
        inline fun <S, T : S> reduce(iterable: Iterable<T>, operation: (S, T) -> S): S {
            return iterable.reduce(operation)
        }

        @JvmStatic
        inline fun <S, T : S> reduceIndexed(iterable: Iterable<T>, operation: (index: Int, S, T) -> S): S {
            return iterable.reduceIndexed(operation)
        }

        @JvmStatic
        inline fun <S, T : S> reduceOrNull(iterable: Iterable<T>, operation: (S, T) -> S): S? {
            return iterable.reduceOrNull(operation)
        }

        @JvmStatic
        inline fun <S, T : S> reduceIndexedOrNull(iterable: Iterable<T>, operation: (index: Int, S, T) -> S): S? {
            return iterable.reduceIndexedOrNull(operation)
        }

        @JvmStatic
        inline fun <T, R> reduce(
            iterable: Iterable<T>,
            initial: R,
            operation: (R, T) -> R
        ): R {
            return iterable.fold(initial, operation)
        }

        @JvmStatic
        inline fun <T, R> reduceIndexed(
            iterable: Iterable<T>,
            initial: R,
            operation: (index: Int, R, T) -> R
        ): R {
            return iterable.foldIndexed(initial, operation)
        }

        @JvmStatic
        inline fun <T, K, V> associate(
            iterable: Iterable<T>,
            keySelector: (T) -> K,
            valueTransform: (T) -> V
        ): Map<K, V> {
            return iterable.associateBy(keySelector, valueTransform)
        }

        @JvmStatic
        inline fun <T, K, V> associate(iterable: Iterable<T>, transform: (T) -> Pair<K, V>): Map<K, V> {
            return iterable.associate(transform)
        }

        @JvmStatic
        inline fun <T, K> associateKey(iterable: Iterable<T>, keySelector: (T) -> K): Map<K, T> {
            return iterable.associateBy(keySelector)
        }

        @JvmStatic
        inline fun <T, V> associateValue(iterable: Iterable<T>, valueSelector: (T) -> V): Map<T, V> {
            return iterable.associateWith(valueSelector)
        }

        @JvmStatic
        inline fun <T, K, V> associateWithNext(
            iterable: Iterable<T>,
            keySelector: (T) -> K,
            valueTransform: (T?) -> V
        ): Map<K, V> {
            return associateWithNextTo(iterable, LinkedHashMap(), keySelector, valueTransform)
        }

        @JvmStatic
        inline fun <T, K, V> associateWithNext(iterable: Iterable<T>, transform: (T, T?) -> Pair<K, V>): Map<K, V> {
            return associateWithNextTo(iterable, LinkedHashMap(), transform)
        }

        @JvmStatic
        inline fun <T, K, V, M : MutableMap<in K, in V>> associateTo(
            iterable: Iterable<T>,
            destination: M,
            keySelector: (T) -> K,
            valueTransform: (T) -> V
        ): M {
            return iterable.associateByTo(destination, keySelector, valueTransform)
        }

        @JvmStatic
        inline fun <T, K, V, M : MutableMap<in K, in V>> associateTo(
            iterable: Iterable<T>,
            destination: M,
            transform: (T) -> Pair<K, V>
        ): M {
            return iterable.associateTo(destination, transform)
        }

        @JvmStatic
        inline fun <T, K, M : MutableMap<in K, in T>> associateKeyTo(
            iterable: Iterable<T>,
            destination: M,
            keySelector: (T) -> K
        ): M {
            return iterable.associateByTo(destination, keySelector)
        }

        @JvmStatic
        inline fun <T, V, M : MutableMap<in T, in V>> associateValueTo(
            iterable: Iterable<T>,
            destination: M,
            valueSelector: (T) -> V
        ): M {
            return iterable.associateWithTo(destination, valueSelector)
        }

        @JvmStatic
        inline fun <T, K, V, M : MutableMap<in K, in V>> associateWithNextTo(
            iterable: Iterable<T>,
            destination: M,
            keySelector: (T) -> K,
            valueTransform: (T?) -> V
        ): M {
            val iterator = iterable.iterator()
            while (iterator.hasNext()) {
                val tk = iterator.next()
                val k = keySelector(tk)
                if (iterator.hasNext()) {
                    val tv = iterator.next()
                    val v = valueTransform(tv)
                    destination.put(k, v)
                } else {
                    val v = valueTransform(null)
                    destination.put(k, v)
                    break
                }
            }
            return destination
        }

        @JvmStatic
        inline fun <T, K, V, M : MutableMap<in K, in V>> associateWithNextTo(
            iterable: Iterable<T>,
            destination: M,
            transform: (T, T?) -> Pair<K, V>
        ): M {
            val iterator = iterable.iterator()
            while (iterator.hasNext()) {
                val tk = iterator.next()
                if (iterator.hasNext()) {
                    val tv = iterator.next()
                    val pair = transform(tk, tv)
                    destination.put(pair.first, pair.second)
                } else {
                    val pair = transform(tk, null)
                    destination.put(pair.first, pair.second)
                    break
                }
            }
            return destination
        }

        @JvmStatic
        inline fun <T, K> groupBy(iterable: Iterable<T>, keySelector: (T) -> K): Map<K, List<T>> {
            return iterable.groupBy(keySelector)
        }

        @JvmStatic
        inline fun <T, K, V> groupBy(
            iterable: Iterable<T>,
            keySelector: (T) -> K,
            valueTransform: (T) -> V
        ): Map<K, List<V>> {
            return iterable.groupBy(keySelector, valueTransform)
        }

        @JvmStatic
        inline fun <T, K, M : MutableMap<in K, MutableList<T>>> groupByTo(
            iterable: Iterable<T>,
            destination: M,
            keySelector: (T) -> K
        ): M {
            return iterable.groupByTo(destination, keySelector)
        }

        @JvmStatic
        inline fun <T, K, V, M : MutableMap<in K, MutableList<V>>> groupByTo(
            iterable: Iterable<T>,
            destination: M,
            keySelector: (T) -> K,
            valueTransform: (T) -> V
        ): M {
            return iterable.groupByTo(destination, keySelector, valueTransform)
        }

        @JvmStatic
        fun <T> chunked(iterable: Iterable<T>, size: Int): List<List<T>> {
            return iterable.chunked(size)
        }

        @JvmStatic
        fun <T, R> chunked(
            iterable: Iterable<T>,
            size: Int,
            transform: (List<T>) -> R
        ): List<R> {
            return iterable.chunked(size, transform)
        }

        @JvmStatic
        fun <T> windowed(
            iterable: Iterable<T>,
            size: Int,
            step: Int = 1,
            partialWindows: Boolean = false
        ): List<List<T>> {
            return iterable.windowed(size, step, partialWindows)
        }

        @JvmStatic
        fun <T, R> windowed(
            iterable: Iterable<T>,
            size: Int,
            step: Int = 1,
            partialWindows: Boolean = false,
            transform: (List<T>) -> R
        ): List<R> {
            return iterable.windowed(size, step, partialWindows, transform)
        }

        @JvmStatic
        inline fun <T, R, V> zip(
            iterable: Iterable<T>,
            other: Array<out R>,
            transform: (T, R) -> V
        ): List<V> {
            return iterable.zip(other, transform)
        }

        @JvmStatic
        inline fun <T, R, V> zip(
            iterable: Iterable<T>,
            other: Iterable<R>,
            transform: (T, R) -> V
        ): List<V> {
            return iterable.zip(other, transform)
        }

        @JvmStatic
        inline fun <T, R> zipWithNext(iterable: Iterable<T>, transform: (T, T) -> R): List<R> {
            return iterable.zipWithNext(transform)
        }

        @JvmStatic
        fun <T : Comparable<T>> max(iterable: Iterable<T>): T {
            return Require.notNull(maxOrNull(iterable))
        }

        @JvmStatic
        fun <T : Comparable<T>> maxOrNull(iterable: Iterable<T>): T? {
            return iterable.maxOrNull()
        }

        @JvmStatic
        fun <T> max(iterable: Iterable<T>, comparator: Comparator<in T>): T {
            return Require.notNull(maxOrNull(iterable, comparator))
        }

        @JvmStatic
        fun <T> maxOrNull(iterable: Iterable<T>, comparator: Comparator<in T>): T? {
            return iterable.maxWithOrNull(comparator)
        }

        @JvmStatic
        fun <T : Comparable<T>> min(iterable: Iterable<T>): T {
            return Require.notNull(minOrNull(iterable))
        }

        @JvmStatic
        fun <T : Comparable<T>> minOrNull(iterable: Iterable<T>): T? {
            return iterable.minOrNull()
        }

        @JvmStatic
        fun <T> min(iterable: Iterable<T>, comparator: Comparator<in T>): T {
            return Require.notNull(minOrNull(iterable, comparator))
        }

        @JvmStatic
        fun <T> minOrNull(iterable: Iterable<T>, comparator: Comparator<in T>): T? {
            return iterable.minWithOrNull(comparator)
        }

        @JvmStatic
        fun <T> sumInt(iterable: Iterable<T>): Int {
            return sumInt(iterable) { To.toInt(it) }
        }

        @JvmStatic
        fun <T> sumLong(iterable: Iterable<T>): Long {
            return sumLong(iterable) { To.toLong(it) }
        }

        @JvmStatic
        fun <T> sumDouble(iterable: Iterable<T>): Double {
            return sumDouble(iterable) { To.toDouble(it) }
        }

        @JvmStatic
        fun <T> sumBigInteger(iterable: Iterable<T>): BigInteger {
            return sumBigInteger(iterable) { To.toBigInteger(it) }
        }

        @JvmStatic
        fun <T> sumBigDecimal(iterable: Iterable<T>): BigDecimal {
            return sumBigDecimal(iterable) { To.toBigDecimal(it) }
        }

        @JvmStatic
        inline fun <T> sumInt(iterable: Iterable<T>, selector: (T) -> Int): Int {
            return iterable.sumOf(selector)
        }

        @JvmStatic
        inline fun <T> sumLong(iterable: Iterable<T>, selector: (T) -> Long): Long {
            return iterable.sumOf(selector)
        }

        @JvmStatic
        fun <T> sumDouble(iterable: Iterable<T>, selector: (T) -> Double): Double {
            return iterable.sumOf(selector)
        }

        @JvmStatic
        fun <T> sumBigInteger(iterable: Iterable<T>, selector: (T) -> BigInteger): BigInteger {
            return iterable.sumOf(selector)
        }

        @JvmStatic
        fun <T> sumBigDecimal(iterable: Iterable<T>, selector: (T) -> BigDecimal): BigDecimal {
            return iterable.sumOf(selector)
        }

        @JvmStatic
        fun <T> intersect(iterable: Iterable<T>, other: Iterable<T>): Set<T> {
            return iterable.intersect(other)
        }

        @JvmStatic
        fun <T> union(iterable: Iterable<T>, other: Iterable<T>): Set<T> {
            return iterable.union(other)
        }

        @JvmStatic
        fun <T> subtract(iterable: Iterable<T>, other: Iterable<T>): Set<T> {
            return iterable.subtract(other)
        }

        @JvmStatic
        fun <T> reversed(iterable: Iterable<T>): List<T> {
            return iterable.reversed()
        }

        @JvmStatic
        fun <T : Comparable<T>> sorted(iterable: Iterable<T>): List<T> {
            return iterable.sorted()
        }

        @JvmStatic
        fun <T> sorted(iterable: Iterable<T>, comparator: Comparator<in T>): List<T> {
            return iterable.sortedWith(comparator)
        }

        @JvmStatic
        fun <T> shuffled(iterable: Iterable<T>): List<T> {
            return iterable.shuffled()
        }

        @JvmStatic
        fun <T> shuffled(iterable: Iterable<T>, random: Random): List<T> {
            return iterable.shuffled(random)
        }

        @JvmStatic
        fun <T> distinct(iterable: Iterable<T>): List<T> {
            return iterable.distinct()
        }

        @JvmStatic
        inline fun <T, K> distinct(iterable: Iterable<T>, selector: (T) -> K): List<T> {
            return iterable.distinctBy(selector)
        }

        @JvmStatic
        inline fun <T> forEachIndexed(iterable: Iterable<T>, action: (index: Int, T) -> Unit) {
            return iterable.forEachIndexed(action)
        }

        @JvmStatic
        fun <T> removeAll(iterable: MutableIterable<T>, predicate: (T) -> Boolean): Boolean {
            return iterable.removeAll(predicate)
        }

        @JvmStatic
        fun <T> removeFirst(iterable: MutableIterable<T>): T {
            val iterator = iterable.iterator()
            Check.checkElement(iterator.hasNext(), "Iterable is empty.")
            val first = iterator.next()
            iterator.remove()
            return first
        }

        @JvmStatic
        fun <T> removeFirstOrNull(iterable: MutableIterable<T>): T? {
            val iterator = iterable.iterator()
            if (!iterator.hasNext()) {
                return null
            }
            val first = iterator.next()
            iterator.remove()
            return first
        }

        @JvmStatic
        fun <T> removeLast(iterable: MutableIterable<T>): T {
            val iterator = iterable.iterator()
            Check.checkElement(iterator.hasNext(), "Iterable is empty.")
            var last = iterator.next()
            while (iterator.hasNext()) {
                last = iterator.next()
            }
            iterator.remove()
            return last
        }

        @JvmStatic
        fun <T> removeLastOrNull(iterable: MutableIterable<T>): T? {
            val iterator = iterable.iterator()
            if (!iterator.hasNext()) {
                return null
            }
            var last = iterator.next()
            while (iterator.hasNext()) {
                last = iterator.next()
            }
            iterator.remove()
            return last
        }

        @JvmStatic
        fun <T> retainAll(iterable: MutableIterable<T>, predicate: (T) -> Boolean): Boolean {
            return iterable.retainAll(predicate)
        }

        @JvmStatic
        fun <T> plus(iterable: Iterable<T>, element: T): List<T> {
            return iterable.plus(element)
        }

        @JvmStatic
        fun <T> plus(iterable: Iterable<T>, elements: Array<out T>): List<T> {
            return iterable.plus(elements)
        }

        @JvmStatic
        fun <T> plus(iterable: Iterable<T>, elements: Iterable<T>): List<T> {
            return iterable.plus(elements)
        }

        @JvmStatic
        fun <T> minus(iterable: Iterable<T>, element: T): List<T> {
            return iterable.minus(element)
        }

        @JvmStatic
        fun <T> minus(iterable: Iterable<T>, elements: Array<out T>): List<T> {
            return iterable.minus(elements)
        }

        @JvmStatic
        fun <T> minus(iterable: Iterable<T>, elements: Iterable<T>): List<T> {
            return iterable.minus(elements)
        }

        @JvmStatic
        fun <T, C : MutableCollection<in T>> toCollection(iterable: Iterable<T>, destination: C): C {
            return iterable.toCollection(destination)
        }

        @JvmStatic
        fun <T> toSet(iterable: Iterable<T>): Set<T> {
            return iterable.toSet()
        }

        @JvmStatic
        fun <T> toMutableSet(iterable: Iterable<T>): MutableSet<T> {
            return iterable.toMutableSet()
        }

        @JvmStatic
        fun <T> toHashSet(iterable: Iterable<T>): HashSet<T> {
            return iterable.toHashSet()
        }

        @JvmStatic
        fun <T> toSortedSet(iterable: Iterable<T>): SortedSet<T> {
            return toSortedSet(iterable, Sort.selfComparableComparator())
        }

        @JvmStatic
        fun <T> toSortedSet(iterable: Iterable<T>, comparator: Comparator<in T>): SortedSet<T> {
            return iterable.toSortedSet(comparator)
        }

        @JvmStatic
        fun <T> toList(iterable: Iterable<T>): List<T> {
            return iterable.toList()
        }

        @JvmStatic
        fun <T> toMutableList(iterable: Iterable<T>): MutableList<T> {
            return iterable.toMutableList()
        }

        @JvmStatic
        fun <T> toStream(iterable: Iterable<T>): Stream<T> {
            return toStream(iterable, false)
        }

        @JvmStatic
        fun <T> toStream(iterable: Iterable<T>, parallel: Boolean): Stream<T> {
            return StreamSupport.stream(iterable.spliterator(), parallel)
        }

        @JvmStatic
        fun <T> toArray(iterable: Iterable<T>): Array<Any?> {
            val list = toList(iterable)
            val result = arrayOfNulls<Any?>(list.size)
            list.forEachIndexed { i, t -> result[i] = t }
            return result
        }

        @JvmStatic
        inline fun <T> toArray(iterable: Iterable<T>, generator: (size: Int) -> Array<T>): Array<T> {
            val list = toList(iterable)
            val result = generator(list.size)
            list.forEachIndexed { i, t -> result[i] = t }
            return result
        }

        @JvmStatic
        fun <T> toBooleanArray(iterable: Iterable<T>): BooleanArray {
            return toBooleanArray(iterable) { To.toBoolean(it) }
        }

        @JvmStatic
        inline fun <T> toBooleanArray(iterable: Iterable<T>, selector: (T) -> Boolean): BooleanArray {
            val list = toList(iterable)
            val result = BooleanArray(list.size)
            list.forEachIndexed { i, t -> result[i] = selector(t) }
            return result
        }

        @JvmStatic
        fun <T> toByteArray(iterable: Iterable<T>): ByteArray {
            return toByteArray(iterable) { To.toByte(it) }
        }

        @JvmStatic
        inline fun <T> toByteArray(iterable: Iterable<T>, selector: (T) -> Byte): ByteArray {
            val list = toList(iterable)
            val result = ByteArray(list.size)
            list.forEachIndexed { i, t -> result[i] = selector(t) }
            return result
        }

        @JvmStatic
        fun <T> toShortArray(iterable: Iterable<T>): ShortArray {
            return toShortArray(iterable) { To.toShort(it) }
        }

        @JvmStatic
        inline fun <T> toShortArray(iterable: Iterable<T>, selector: (T) -> Short): ShortArray {
            val list = toList(iterable)
            val result = ShortArray(list.size)
            list.forEachIndexed { i, t -> result[i] = selector(t) }
            return result
        }

        @JvmStatic
        fun <T> toCharArray(iterable: Iterable<T>): CharArray {
            return toCharArray(iterable) { To.toChar(it) }
        }

        @JvmStatic
        inline fun <T> toCharArray(iterable: Iterable<T>, selector: (T) -> Char): CharArray {
            val list = toList(iterable)
            val result = CharArray(list.size)
            list.forEachIndexed { i, t -> result[i] = selector(t) }
            return result
        }

        @JvmStatic
        fun <T> toIntArray(iterable: Iterable<T>): IntArray {
            return toIntArray(iterable) { To.toInt(it) }
        }

        @JvmStatic
        inline fun <T> toIntArray(iterable: Iterable<T>, selector: (T) -> Int): IntArray {
            val list = toList(iterable)
            val result = IntArray(list.size)
            list.forEachIndexed { i, t -> result[i] = selector(t) }
            return result
        }

        @JvmStatic
        fun <T> toLongArray(iterable: Iterable<T>): LongArray {
            return toLongArray(iterable) { To.toLong(it) }
        }

        @JvmStatic
        inline fun <T> toLongArray(iterable: Iterable<T>, selector: (T) -> Long): LongArray {
            val list = toList(iterable)
            val result = LongArray(list.size)
            list.forEachIndexed { i, t -> result[i] = selector(t) }
            return result
        }

        @JvmStatic
        fun <T> toFloatArray(iterable: Iterable<T>): FloatArray {
            return toFloatArray(iterable) { To.toFloat(it) }
        }

        @JvmStatic
        inline fun <T> toFloatArray(iterable: Iterable<T>, selector: (T) -> Float): FloatArray {
            val list = toList(iterable)
            val result = FloatArray(list.size)
            list.forEachIndexed { i, t -> result[i] = selector(t) }
            return result
        }

        @JvmStatic
        fun <T> toDoubleArray(iterable: Iterable<T>): DoubleArray {
            return toDoubleArray(iterable) { To.toDouble(it) }
        }

        @JvmStatic
        inline fun <T> toDoubleArray(iterable: Iterable<T>, selector: (T) -> Double): DoubleArray {
            val list = toList(iterable)
            val result = DoubleArray(list.size)
            list.forEachIndexed { i, t -> result[i] = selector(t) }
            return result
        }
    }
}