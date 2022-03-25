package xyz.srclab.common.base

/**
 * [Val] represents a value wrapper, of which [value] is a final reference cannot be reassigned,
 * just like the kotlin keyword: `val`.
 */
interface Val<T> {

    /**
     * Value of this [Val].
     */
    val value: T

    companion object {

        /**
         * Returns a [Val] with [this].
         */
        @JvmName("of")
        @JvmStatic
        fun <T> T.toVal(): Val<T> {
            return ValImpl(this)
        }

        private data class ValImpl<T>(override val value: T) : Val<T>
    }
}

/**
 * Boolean version of [Val].
 * @see Val
 */
interface BooleanVal {

    /**
     * Value of this [Val].
     */
    val value: Boolean

    /**
     * Returns this as [Val], they are equivalent and have same status, any operation will affect each other.
     */
    fun asVar(): Val<Boolean> {
        return object : Val<Boolean> {
            override val value: Boolean
                get() = this@BooleanVal.value
        }
    }

    companion object {

        /**
         * Returns a [BooleanVal] with [this].
         */
        @JvmName("of")
        @JvmStatic
        fun Boolean.toBooleanVal(): BooleanVal {
            return BooleanValImpl(this)
        }

        private data class BooleanValImpl(override val value: Boolean) : BooleanVal
    }
}

/**
 * Byte version of [Val].
 * @see Val
 */
interface ByteVal {

    /**
     * Value of this [Val].
     */
    val value: Byte

    /**
     * Returns this as [Val], they are equivalent and have same status, any operation will affect each other.
     */
    fun asVar(): Val<Byte> {
        return object : Val<Byte> {
            override val value: Byte
                get() = this@ByteVal.value
        }
    }

    companion object {

        /**
         * Returns a [ByteVal] with [this].
         */
        @JvmName("of")
        @JvmStatic
        fun Byte.toByteVal(): ByteVal {
            return ByteValImpl(this)
        }

        private data class ByteValImpl(override val value: Byte) : ByteVal
    }
}

/**
 * Short version of [Val].
 * @see Val
 */
interface ShortVal {

    /**
     * Value of this [Val].
     */
    val value: Short

    /**
     * Returns this as [Val], they are equivalent and have same status, any operation will affect each other.
     */
    fun asVar(): Val<Short> {
        return object : Val<Short> {
            override val value: Short
                get() = this@ShortVal.value
        }
    }

    companion object {

        /**
         * Returns a [ShortVal] with [this].
         */
        @JvmName("of")
        @JvmStatic
        fun Short.toShortVal(): ShortVal {
            return ShortValImpl(this)
        }

        private data class ShortValImpl(override val value: Short) : ShortVal
    }
}

/**
 * Char version of [Val].
 * @see Val
 */
interface CharVal {

    /**
     * Value of this [Val].
     */
    val value: Char

    /**
     * Returns this as [Val], they are equivalent and have same status, any operation will affect each other.
     */
    fun asVar(): Val<Char> {
        return object : Val<Char> {
            override val value: Char
                get() = this@CharVal.value
        }
    }

    companion object {

        /**
         * Returns a [CharVal] with [this].
         */
        @JvmName("of")
        @JvmStatic
        fun Char.toCharVal(): CharVal {
            return CharValImpl(this)
        }

        private data class CharValImpl(override val value: Char) : CharVal
    }
}

/**
 * Int version of [Val].
 * @see Val
 */
interface IntVal {

    /**
     * Value of this [Val].
     */
    val value: Int

    /**
     * Returns this as [Val], they are equivalent and have same status, any operation will affect each other.
     */
    fun asVar(): Val<Int> {
        return object : Val<Int> {
            override val value: Int
                get() = this@IntVal.value
        }
    }

    companion object {

        /**
         * Returns a [IntVal] with [this].
         */
        @JvmName("of")
        @JvmStatic
        fun Int.toIntVal(): IntVal {
            return IntValImpl(this)
        }

        private data class IntValImpl(override val value: Int) : IntVal
    }
}

/**
 * Long version of [Val].
 * @see Val
 */
interface LongVal {

    /**
     * Value of this [Val].
     */
    val value: Long

    /**
     * Returns this as [Val], they are equivalent and have same status, any operation will affect each other.
     */
    fun asVar(): Val<Long> {
        return object : Val<Long> {
            override val value: Long
                get() = this@LongVal.value
        }
    }

    companion object {

        /**
         * Returns a [LongVal] with [this].
         */
        @JvmName("of")
        @JvmStatic
        fun Long.toLongVal(): LongVal {
            return LongValImpl(this)
        }

        private data class LongValImpl(override val value: Long) : LongVal
    }
}

/**
 * Float version of [Val].
 * @see Val
 */
interface FloatVal {

    /**
     * Value of this [Val].
     */
    val value: Float

    /**
     * Returns this as [Val], they are equivalent and have same status, any operation will affect each other.
     */
    fun asVar(): Val<Float> {
        return object : Val<Float> {
            override val value: Float
                get() = this@FloatVal.value
        }
    }

    companion object {

        /**
         * Returns a [FloatVal] with [this].
         */
        @JvmName("of")
        @JvmStatic
        fun Float.toFloatVal(): FloatVal {
            return FloatValImpl(this)
        }

        private data class FloatValImpl(override val value: Float) : FloatVal
    }
}

/**
 * Double version of [Val].
 * @see Val
 */
interface DoubleVal {

    /**
     * Value of this [Val].
     */
    val value: Double

    /**
     * Returns this as [Val], they are equivalent and have same status, any operation will affect each other.
     */
    fun asVar(): Val<Double> {
        return object : Val<Double> {
            override val value: Double
                get() = this@DoubleVal.value
        }
    }

    companion object {

        /**
         * Returns a [DoubleVal] with [this].
         */
        @JvmName("of")
        @JvmStatic
        fun Double.toDoubleVal(): DoubleVal {
            return DoubleValImpl(this)
        }

        private data class DoubleValImpl(override val value: Double) : DoubleVal
    }
}