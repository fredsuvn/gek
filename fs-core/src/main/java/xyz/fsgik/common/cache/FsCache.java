package xyz.fsgik.common.cache;

import xyz.fsgik.annotations.Nullable;
import xyz.fsgik.annotations.ThreadSafe;

import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.function.BiPredicate;
import java.util.function.Function;

/**
 * Cache interface, is expected to thread-safe and doesn't support null value.
 *
 * @author fresduvn
 */
@ThreadSafe
public interface FsCache<K, V> {

    /**
     * Creates a new thread-safe Cache based by {@link SoftReference}.
     */
    static <K, V> FsCache<K, V> softCache() {
        return new CacheImpl<>(true);
    }

    /**
     * Creates a new thread-safe Cache based by {@link SoftReference} with remove listener,
     * the listener will be called <b>after</b> removing from the cache.
     *
     * @param removeListener remove listener called <b>after</b> removing from the cache,
     *                       the first argument is current cache and second is the key.
     */
    static <K, V> FsCache<K, V> softCache(RemoveListener<K, V> removeListener) {
        return new CacheImpl<>(true, removeListener);
    }

    /**
     * Creates a new thread-safe Cache based by {@link SoftReference} with initial capacity.
     *
     * @param initialCapacity initial capacity
     */
    static <K, V> FsCache<K, V> softCache(int initialCapacity) {
        return new CacheImpl<>(true, initialCapacity);
    }

    /**
     * Creates a new thread-safe Cache based by {@link SoftReference} with initial capacity and remove listener,
     * the listener will be called <b>after</b> removing from the cache.
     *
     * @param initialCapacity initial capacity
     * @param removeListener  remove listener called <b>after</b> removing from the cache,
     *                        the first argument is current cache and second is the key.
     */
    static <K, V> FsCache<K, V> softCache(int initialCapacity, RemoveListener<K, V> removeListener) {
        return new CacheImpl<>(true, initialCapacity, removeListener);
    }

    /**
     * Creates a new thread-safe Cache based by {@link WeakReference}.
     */
    static <K, V> FsCache<K, V> weakCache() {
        return new CacheImpl<>(false);
    }

    /**
     * Creates a new thread-safe Cache based by {@link WeakReference} with remove listener,
     * the listener will be called <b>after</b> removing from the cache.
     *
     * @param removeListener remove listener called <b>after</b> removing from the cache,
     *                       the first argument is current cache and second is the key.
     */
    static <K, V> FsCache<K, V> weakCache(RemoveListener<K, V> removeListener) {
        return new CacheImpl<>(false, removeListener);
    }

    /**
     * Creates a new thread-safe Cache based by {@link WeakReference} with initial capacity.
     *
     * @param initialCapacity initial capacity
     */
    static <K, V> FsCache<K, V> weakCache(int initialCapacity) {
        return new CacheImpl<>(false, initialCapacity);
    }

    /**
     * Creates a new thread-safe Cache based by {@link WeakReference} with initial capacity and remove listener,
     * the listener will be called <b>after</b> removing from the cache.
     *
     * @param initialCapacity initial capacity
     * @param removeListener  remove listener called <b>after</b> removing from the cache,
     *                        the first argument is current cache and second is the key.
     */
    static <K, V> FsCache<K, V> weakCache(int initialCapacity, RemoveListener<K, V> removeListener) {
        return new CacheImpl<>(false, initialCapacity, removeListener);
    }

    /**
     * Returns value associating with given key from this cache,
     * return null if there is no entry for given key.
     *
     * @param key given key
     */
    @Nullable
    V get(K key);

    /**
     * Returns value associating with given key from this cache.
     * If there is no entry for given key, a new value will be created by given loader.
     * <p>
     * If the loader returns null, the null value will not be put and return null.
     *
     * @param key    given key
     * @param loader given loader
     */
    @Nullable
    V get(K key, Function<? super K, @Nullable ? extends V> loader);

    /**
     * Sets the value associated with given key, return old value or null if there is no old value.
     *
     * @param key   given key
     * @param value the value
     */
    V put(K key, V value);

    /**
     * Removes the value associated with given key.
     *
     * @param key given key
     */
    void remove(K key);

    /**
     * Removes values of which key and value (first and second param) pass given predicate.
     *
     * @param predicate given predicate
     */
    void removeIf(BiPredicate<K, V> predicate);

    /**
     * Returns current size.
     */
    int size();

    /**
     * Removes all values in this cache.
     */
    void clear();

    /**
     * Removes all expired values.
     */
    void cleanUp();

    /**
     * Removing listener of {@link FsCache}.
     */
    interface RemoveListener<K, V> {

        /**
         * This method will be called after removing a key and its value.
         *
         * @param cache current cache
         * @param key   key of removed value
         */
        void onRemove(FsCache<K, V> cache, K key);
    }
}