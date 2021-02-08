package xyz.srclab.common.state

import xyz.srclab.common.base.INAPPLICABLE_JVM_NAME
import xyz.srclab.common.base.hash

import xyz.srclab.common.exception.ExceptionStatus
import xyz.srclab.common.exception.StatusException

/**
 * @param C code type
 * @param D description type
 * @param T state type
 *
 * @author sunqian
 *
 * @see ExceptionStatus
 * @see StatusException
 */
interface State<C, D, T : State<C, D, T>> {

    @Suppress(INAPPLICABLE_JVM_NAME)
    val code: C
        @JvmName("code") get

    @Suppress(INAPPLICABLE_JVM_NAME)
    val description: D?
        @JvmName("description") get

    fun withNewDescription(newDescription: D?): T

    fun withMoreDescription(moreDescription: D?): T

    companion object {

        @JvmStatic
        @JvmName("equals")
        fun State<*, *, *>.stateEquals(other: Any?): Boolean {
            if (this === other) {
                return true
            }
            if (other !is State<*, *, *>) {
                return false
            }
            return (this.code == other.code && this.description == other.description)
        }

        @JvmStatic
        @JvmName("hashCode")
        fun State<*, *, *>.stateHashCode(): Int {
            return hash(this.code, this.description)
        }

        @JvmStatic
        @JvmName("toString")
        fun State<*, *, *>.stateToString(): String {
            val code = this.code
            val description = this.description
            return if (description === null) code.toString() else "$code-$description"
        }

        @JvmStatic
        @JvmName("moreDescription")
        fun CharSequence?.stateMoreDescription(moreDescription: CharSequence?): String? {
            return when {
                this === null -> moreDescription?.toString()
                moreDescription === null -> this.toString()
                else -> "$this[$moreDescription]"
            }
        }
    }
}