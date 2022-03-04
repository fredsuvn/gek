@file:JvmName("BEnum")

package xyz.srclab.common.base

import java.io.Serializable

@JvmName("getValue")
@Throws(NoSuchEnumException::class)
@JvmOverloads
fun <T : Enum<T>> Class<*>.enumValue(name: CharSequence, ignoreCase: Boolean = false): T {
    return enumValueOrNull(name, ignoreCase) ?: throw NoSuchEnumException("$this.$name")
}

@JvmName("getValueOrNull")
@JvmOverloads
fun <T> Class<*>.enumValueOrNull(name: CharSequence, ignoreCase: Boolean = false): T? {
    if (!ignoreCase) {
        return name.toString().toEnumOrNull(this)?.asTyped()
    }
    return try {
        val values: Array<out Enum<*>>? = this.enumConstants?.asTyped()
        if (values.isNullOrEmpty()) {
            return null
        }
        for (value in values) {
            if (value.name.contentEquals(name, true)) {
                return value.asTyped()
            }
        }
        null
    } catch (e: Exception) {
        null
    }
}

@JvmName("getValue")
@Throws(NoSuchEnumException::class)
fun <T> Class<*>.enumValue(index: Int): T {
    return enumValueOrNull(index) ?: throw NoSuchEnumException("$this[$index]")
}

@JvmName("getValueOrNull")
fun <T> Class<*>.enumValueOrNull(index: Int): T? {
    val values = this.enumConstants
    if (values.isNullOrEmpty()) {
        return null
    }
    if (index.isIndexInBounds(0, values.size)) {
        return values[index].asTyped()
    }
    return null
}

private fun String.toEnumOrNull(type: Class<*>): Any? {
    return try {
        java.lang.Enum.valueOf(type.asTyped<Class<Enum<*>>>(), this)
    } catch (e: Exception) {
        null
    }
}

open class NoSuchEnumException(message: String?) : RuntimeException(message), Serializable {
    companion object {
        private val serialVersionUID: Long = defaultSerialVersion()
    }
}