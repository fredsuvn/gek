package xyz.srclab.common.bean

import xyz.srclab.common.bean.BeanResolver.Companion.CachedBeanResolver
import xyz.srclab.common.cache.Cache
import xyz.srclab.common.collect.asToList
import xyz.srclab.common.collect.plusBefore
import java.lang.reflect.Type

/**
 * Resolver for bean.
 *
 * Note default methods implementations will not cache result, use [CachedBeanResolver] if you need caching.
 *
 * @see BeanResolveHandler
 * @see BeanStyleBeanResolveHandler
 * @see RecordStyleBeanResolveHandler
 * @see CachedBeanResolver
 */
interface BeanResolver {

    val resolveHandlers: List<BeanResolveHandler>

    fun resolve(type: Type): BeanType {
        val context = BeanResolveContext(type)
        val builder = BeanTypeBuilder(type)
        for (resolveHandler in resolveHandlers) {
            val jumpStatement = resolveHandler.resolve(context, builder)
            if (!jumpStatement.isContinue()) {
                break
            }
        }
        return builder.build()
    }

    companion object {

        @JvmField
        val DEFAULT: BeanResolver = newBeanResolver(BeanResolveHandler.DEFAULTS)

        /**
         * Return a new [BeanResolver] with given [resolveHandlers] and [cache] (default is [Cache.weakCache]).
         */
        @JvmOverloads
        @JvmStatic
        fun newBeanResolver(
            resolveHandlers: Iterable<BeanResolveHandler>,
            cache: Cache<Type, BeanType> = Cache.weakCache()
        ): BeanResolver {
            return CachedBeanResolver(resolveHandlers.asToList(), cache)
        }

        /**
         * Returns a new [BeanResolver] consists of given [handler] followed by existed [BeanResolveHandler]s.
         */
        @JvmStatic
        fun BeanResolver.extend(handler: BeanResolveHandler): BeanResolver {
            return newBeanResolver(this.resolveHandlers.plusBefore(0, handler))
        }

        /**
         * [BeanResolver] implementation which can cache resolved [BeanType].
         */
        private class CachedBeanResolver(
            override val resolveHandlers: List<BeanResolveHandler>,
            private val cache: Cache<Type, BeanType>
        ) : BeanResolver {

            override fun resolve(type: Type): BeanType {
                return cache.getOrLoad(type) {
                    super.resolve(type)
                }
            }
        }
    }
}