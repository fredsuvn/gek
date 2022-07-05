package xyz.srclab.common.cache

import xyz.srclab.common.base.Val

/**
 * [Val] for cache value.
 */
interface CacheVal<V> : Val<V> {

    companion object {

        /**
         * Returns a [CacheVal] with [v].
         */
        @JvmStatic
        fun <V> of(v: V): CacheVal<V> {
            return CacheValImpl(v)
        }

        private data class CacheValImpl<V>(override val value: V) : CacheVal<V>
    }
}