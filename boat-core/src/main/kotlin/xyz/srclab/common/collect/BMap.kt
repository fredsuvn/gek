@file:JvmName("BMap")

package xyz.srclab.common.collect

import xyz.srclab.common.base.asTyped
import xyz.srclab.common.base.castComparableComparator
import xyz.srclab.common.base.toKotlinFun
import java.util.*
import java.util.function.BiFunction
import java.util.function.Function
import java.util.function.Predicate
import kotlin.collections.associateTo as associateToKt
import kotlin.collections.filter as filterKt
import kotlin.collections.filterTo as filterToKt
import kotlin.collections.flatMap as flatMapKt
import kotlin.collections.flatMapTo as flatMapToKt
import kotlin.collections.map as mapKt
import kotlin.collections.mapTo as mapToKt
import kotlin.collections.minus as minusKt
import kotlin.collections.plus as plusKt
import kotlin.collections.sortedWith as sortedWithKt

fun <K, V> newMap(vararg keyValues: Any?): LinkedHashMap<K, V> {
    return LinkedHashMap<K, V>().collect(*keyValues)
}

fun <K, V> newMap(keyValues: Iterable<Any?>): LinkedHashMap<K, V> {
    return LinkedHashMap<K, V>().collect(keyValues)
}

fun <K, V, C : MutableMap<in K, in V>> C.collect(vararg keyValues: Any?): C {
    return collect(keyValues.asList())
}

fun <K, V, C : MutableMap<in K, in V>> C.collect(keyValues: Iterable<Any?>): C {
    val iterator = keyValues.iterator()
    while (iterator.hasNext()) {
        val key = iterator.next()
        if (iterator.hasNext()) {
            val value = iterator.next()
            this[key.asTyped()] = value.asTyped()
        } else {
            break
        }
    }
    return this
}

fun <K, V> Map<K, V>.filter(predicate: Predicate<in Map.Entry<K, V>>): Map<K, V> {
    return this.filterKt(predicate.toKotlinFun())
}

fun <K, V, M : MutableMap<in K, in V>> Map<K, V>.filterTo(
    destination: M,
    predicate: Predicate<in Map.Entry<K, V>>
): M {
    return this.filterToKt(destination, predicate.toKotlinFun())
}

fun <K, V, RK, RV> Map<K, V>.map(
    keySelector: Function<in K, RK>,
    valueTransform: Function<in V, RV>
): Map<RK, RV> {
    return mapTo(LinkedHashMap(), keySelector, valueTransform)
}

fun <K, V, RK, RV> Map<K, V>.map(transform: BiFunction<in K, in V, Pair<RK, RV>>): Map<RK, RV> {
    return mapTo(LinkedHashMap(), transform)
}

@JvmSynthetic
inline fun <K, V, RK, RV> Map<K, V>.map(
    crossinline keySelector: (K) -> RK,
    crossinline valueTransform: (V) -> RV
): Map<RK, RV> {
    return mapTo(LinkedHashMap(), keySelector, valueTransform)
}

@JvmSynthetic
inline fun <K, V, RK, RV> Map<K, V>.map(transform: (K, V) -> Pair<RK, RV>): Map<RK, RV> {
    return mapTo(LinkedHashMap(), transform)
}

fun <K, V, RK, RV, C : MutableMap<in RK, in RV>> Map<K, V>.mapTo(
    destination: C,
    keySelector: Function<in K, RK>,
    valueTransform: Function<in V, RV>
): C {
    return mapTo(destination, keySelector.toKotlinFun(), valueTransform.toKotlinFun())
}

fun <K, V, RK, RV, C : MutableMap<in RK, in RV>> Map<K, V>.mapTo(
    destination: C,
    transform: BiFunction<in K, in V, Pair<RK, RV>>
): C {
    return mapTo(destination, transform.toKotlinFun())
}

@JvmSynthetic
inline fun <K, V, RK, RV, C : MutableMap<in RK, in RV>> Map<K, V>.mapTo(
    destination: C,
    crossinline keySelector: (K) -> RK,
    crossinline valueTransform: (V) -> RV
): C {
    this.forEach { (k, v) ->
        val rk = keySelector(k)
        val rv = valueTransform(v)
        destination.put(rk, rv)
    }
    return destination
}

@JvmSynthetic
inline fun <K, V, RK, RV, C : MutableMap<in RK, in RV>> Map<K, V>.mapTo(
    destination: C,
    transform: (K, V) -> Pair<RK, RV>
): C {
    this.forEach { (k, v) ->
        val pair = transform(k, v)
        destination.put(pair.first, pair.second)
    }
    return destination
}

fun <K, V, R> Map<K, V>.toList(transform: Function<in Map.Entry<K, V>, R>): List<R> {
    return this.mapKt(transform.toKotlinFun())
}

fun <K, V, R, C : MutableCollection<in R>> Map<K, V>.toList(
    destination: C,
    transform: Function<in Map.Entry<K, V>, R>
): C {
    return this.mapToKt(destination, transform.toKotlinFun())
}

@JvmSynthetic
inline fun <K, V, R> Map<K, V>.toList(transform: (Map.Entry<K, V>) -> R): List<R> {
    return this.mapKt(transform)
}

@JvmSynthetic
inline fun <K, V, R, C : MutableCollection<in R>> Map<K, V>.toList(
    destination: C,
    transform: (Map.Entry<K, V>) -> R
): C {
    return this.mapToKt(destination, transform)
}

fun <K, V, R> Map<K, V>.flatToList(transform: Function<in Map.Entry<K, V>, Iterable<R>>): List<R> {
    return this.flatMapKt(transform.toKotlinFun())
}

fun <K, V, R, C : MutableCollection<in R>> Map<K, V>.flatToList(
    destination: C,
    transform: Function<in Map.Entry<K, V>, Iterable<R>>
): C {
    return this.flatMapToKt(destination, transform.toKotlinFun())
}

@JvmSynthetic
inline fun <K, V, R> Map<K, V>.flatToList(transform: (Map.Entry<K, V>) -> Iterable<R>): List<R> {
    return this.flatMapKt(transform)
}

@JvmSynthetic
inline fun <K, V, R, C : MutableCollection<in R>> Map<K, V>.flatToList(
    destination: C,
    transform: (Map.Entry<K, V>) -> Iterable<R>
): C {
    return this.flatMapToKt(destination, transform)
}

@JvmOverloads
fun <K, V> Map<K, V>.sorted(comparator: Comparator<in Map.Entry<K, V>> = castComparableComparator()): Map<K, V> {
    return this.entries.sortedWithKt(comparator).associateToKt(LinkedHashMap()) { it.key to it.value }
}

fun <K, V> Map<K, V>.toHashMap(): HashMap<K, V> {
    return HashMap(this)
}

fun <K, V> Map<K, V>.toLinkedHashMap(): LinkedHashMap<K, V> {
    return LinkedHashMap(this)
}

fun <K, V> Map<K, V>.toTreeMap(comparator: Comparator<in K>): TreeMap<K, V> {
    val result: TreeMap<K, V> = TreeMap(comparator)
    result.putAll(this)
    return result
}

fun <K, V> Map<K, V>.plus(other: Map<out K, V>): Map<K, V> {
    return this.plusKt(other)
}

fun <K, V> Map<K, V>.minus(key: K): Map<K, V> {
    return this.minusKt(key)
}

fun <K, V> Map<K, V>.minus(keys: Array<out K>): Map<K, V> {
    return this.minusKt(keys)
}

fun <K, V> Map<K, V>.minus(keys: Iterable<K>): Map<K, V> {
    return this.minusKt(keys)
}

fun <K, V> MutableMap<K, V>.removeAll(keys: Array<out K>) {
    for (key in keys) {
        this.remove(key)
    }
}

fun <K, V> MutableMap<K, V>.removeAll(keys: Iterable<K>) {
    for (key in keys) {
        this.remove(key)
    }
}

fun <K, V> copyOnWriteMap(
    newMap: Function<in Map<out K, V>, MutableMap<K, V>>
): CopyOnWriteMap<K, V> {
    return copyOnWriteMap(emptyMap(), newMap)
}

@JvmOverloads
fun <K, V> copyOnWriteMap(
    initMap: Map<out K, V> = emptyMap(),
    newMap: Function<in Map<out K, V>, MutableMap<K, V>> = Function { HashMap() }
): CopyOnWriteMap<K, V> {
    return copyOnWriteMap(initMap, newMap.toKotlinFun())
}

@JvmSynthetic
fun <K, V> copyOnWriteMap(
    initMap: Map<out K, V> = emptyMap(),
    newMap: (Map<out K, V>) -> MutableMap<K, V> = { HashMap() }
): CopyOnWriteMap<K, V> {
    return CopyOnWriteMap(initMap, newMap)
}

fun <K, V> setMap(
    valueSet: Function<K, MutableSet<V>>
): SetMap<K, V> {
    return setMap(LinkedHashMap(), valueSet)
}

@JvmOverloads
fun <K, V> setMap(
    map: MutableMap<K, MutableSet<V>> = LinkedHashMap(),
    valueSet: Function<K, MutableSet<V>> = Function { LinkedHashSet() }
): SetMap<K, V> {
    return setMap(map, valueSet.toKotlinFun())
}

@JvmSynthetic
fun <K, V> setMap(
    map: MutableMap<K, MutableSet<V>> = LinkedHashMap(),
    valueSet: (K) -> MutableSet<V> = { LinkedHashSet() }
): SetMap<K, V> {
    return SetMap(map, valueSet)
}

fun <K, V> listMap(
    valueList: Function<K, MutableList<V>>
): ListMap<K, V> {
    return listMap(LinkedHashMap(), valueList)
}

@JvmOverloads
fun <K, V> listMap(
    map: MutableMap<K, MutableList<V>> = LinkedHashMap(),
    valueList: Function<K, MutableList<V>> = Function { LinkedList() }
): ListMap<K, V> {
    return listMap(map, valueList.toKotlinFun())
}

@JvmSynthetic
fun <K, V> listMap(
    map: MutableMap<K, MutableList<V>> = LinkedHashMap(),
    valueList: (K) -> MutableList<V> = { LinkedList() }
): ListMap<K, V> {
    return ListMap(map, valueList)
}