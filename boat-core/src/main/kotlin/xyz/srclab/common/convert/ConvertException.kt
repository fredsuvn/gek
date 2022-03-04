package xyz.srclab.common.convert

import xyz.srclab.common.base.defaultSerialVersion
import java.io.Serializable
import java.lang.reflect.Type

open class ConvertException : RuntimeException, Serializable {
    constructor(fromType: Type, toType: Type) : super("Unsupported convert: $fromType -> $toType")
    constructor(from: Any?, toType: Type) : super("Unsupported convert: ${from?.javaClass} -> $toType")

    companion object {
        private val serialVersionUID: Long = defaultSerialVersion()
    }
}