package xyz.srclab.common.proxy

/**
 * To invoke source method (proxied method).
 */
interface SourceInvoke {

    /**
     * Invokes source method.
     */
    fun invoke(args: Array<out Any?>?): Any?
}