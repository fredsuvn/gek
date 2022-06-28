package xyz.srclab.common.base

import xyz.srclab.common.base.ByteArrayRef.Companion.arrayRef
import xyz.srclab.common.base.CharArrayRef.Companion.arrayRef
import java.io.OutputStream
import java.io.Writer
import java.nio.ByteBuffer

/**
 * String appender, used to append objects and finally join them into a [String].
 *
 * This class is lazy, it stores the given appended object without any other computation.
 * It computes `join-to-string` operation only when the [toString] method is called,
 * so if content of object previously appended is modified, the final result value will be changed accordingly.
 *
 * Note, **DO NOT** modify content of appended object in `join-to-string` processing,
 * it may cause [ArrayIndexOutOfBoundsException].
 */
open class StringAppender : SegmentAppender<StringAppender, CharSequence>, Appendable, Writer() {

    private var head = SNode<Any>()
    private var cur = head

    override fun append(t: CharSequence?): StringAppender {
        cur.value = t
        newTail()
        return this
    }

    override fun append(t: CharSequence, startIndex: Int): StringAppender {
        cur.value = t.charsRef(startIndex)
        newTail()
        return this
    }

    override fun append(t: CharSequence, startIndex: Int, endIndex: Int): StringAppender {
        cur.value = t.charsRef(startIndex, endIndex)
        newTail()
        return this
    }

    override fun append(c: Char): StringAppender {
        cur.value = c
        newTail()
        return this
    }

    open fun append(obj: Any?): StringAppender {
        cur.value = obj
        newTail()
        return this
    }

    open fun append(chars: CharArray, startIndex: Int): StringAppender {
        cur.value = chars.arrayRef(startIndex)
        newTail()
        return this
    }

    open fun append(chars: CharArray, startIndex: Int, endIndex: Int): StringAppender {
        cur.value = chars.arrayRef(startIndex, endIndex)
        newTail()
        return this
    }

    override fun write(c: Int) {
        append(c.toChar())
    }

    override fun write(cbuf: CharArray) {
        append(cbuf)
    }

    override fun write(str: String) {
        append(str)
    }

    override fun write(str: String, off: Int, len: Int) {
        append(str, off, len)
    }

    override fun write(cbuf: CharArray, off: Int, len: Int) {
        append(cbuf, off, len)
    }

    /**
     * Clears content of this appender.
     */
    open fun clear() {
        val newNode = SNode<Any>()
        head = newNode
        cur = newNode
    }

    /**
     * Joins appended objects into a [String].
     */
    override fun toString(): String {
        // Computes size:
        var size = 0
        var n = head
        while (n.next !== null) {
            when (val v = n.value) {
                null -> size += defaultNullString().length
                is CharSequence -> size += v.length
                is CharArray -> size += v.size
                is CharArrayRef -> size += v.length
                is Char -> size += 1
                else -> {
                    val s = v.toString()
                    n.value = s
                    size += s.length
                }
            }
            n = n.next!!
        }
        // Fills buffer
        val buffer = CharArray(size)
        var i = 0
        n = head
        while (n.next !== null) {
            val s: Int = when (val v = n.value) {
                null -> {
                    defaultNullString().asJavaString().getChars(0, defaultNullString().length, buffer, i)
                    defaultNullString().length
                }
                is String -> {
                    v.asJavaString().getChars(0, v.length, buffer, i)
                    v.length
                }
                is CharSequence -> {
                    v.copyTo(buffer, i, v.length)
                    v.length
                }
                is CharArray -> {
                    System.arraycopy(v, 0, buffer, i, v.size)
                    v.size
                }
                is CharArrayRef -> {
                    v.copyTo(buffer, i)
                }
                is Char -> {
                    buffer[i] = v
                    1
                }
                else -> throw IllegalStateException("Unknown node value type: ${v.javaClass}")
            }
            i += s
            n = n.next!!
        }
        return String(buffer)
    }

    override fun close() {
    }

    override fun flush() {
    }

    private fun newTail() {
        val newNode = SNode<Any>()
        cur.next = newNode
        cur = newNode
    }
}

/**
 * Bytes appender, used to append objects and finally join them into a [ByteArray].
 *
 * This class is lazy, it stores the given appended object without any other computation.
 * It computes `join-to-bytes` operation only when the [toByteArray] method is called,
 * so if content of object previously appended is modified, the final result value will be changed accordingly.
 *
 * Note, **DO NOT** modify content of appended object in `join-to-bytes` processing,
 * it may cause [ArrayIndexOutOfBoundsException].
 */
open class BytesAppender : SegmentAppender<BytesAppender, ByteArray>, OutputStream() {

    private var head = SNode<Any>()
    private var cur = head

    /**
     * No operation. Appending null is unsupported by [BytesAppender].
     */
    override fun append(t: ByteArray?): BytesAppender {
        return this
    }

    override fun append(t: ByteArray, startIndex: Int): BytesAppender {
        cur.value = t.arrayRef(startIndex)
        newTail()
        return this
    }

    override fun append(t: ByteArray, startIndex: Int, endIndex: Int): BytesAppender {
        cur.value = t.arrayRef(startIndex, endIndex)
        newTail()
        return this
    }

    fun append(b: Byte): BytesAppender {
        cur.value = b
        newTail()
        return this
    }

    fun append(buf: ByteBuffer): BytesAppender {
        cur.value = buf
        newTail()
        return this
    }

    override fun write(b: Int) {
        append(b.toByte())
    }

    override fun write(buf: ByteArray) {
        append(buf)
    }

    override fun write(buf: ByteArray, off: Int, len: Int) {
        append(buf, off, len)
    }

    /**
     * Clears content of this appender.
     */
    open fun clear() {
        val newNode = SNode<Any>()
        head = newNode
        cur = newNode
    }

    /**
     * Joins appended objects into a [String].
     */
    fun toByteArray(): ByteArray {
        // Computes size:
        var size = 0
        var n = head
        while (n.next !== null) {
            size += when (val v = n.value) {
                is ByteArray -> v.size
                is ByteArrayRef -> v.length
                is ByteBuffer -> v.remaining()
                is Byte -> 1
                else -> throw IllegalStateException("Unknown node value type: ${v?.javaClass}")
            }
            n = n.next!!
        }
        // Fills buffer
        val buffer = ByteArray(size)
        var i = 0
        n = head
        while (n.next !== null) {
            val s: Int = when (val v = n.value) {
                is ByteArray -> {
                    System.arraycopy(v, 0, buffer, i, v.size)
                    v.size
                }
                is ByteArrayRef -> {
                    v.copyTo(buffer, i)
                }
                is ByteBuffer -> {
                    val remaining = v.remaining()
                    v.get(buffer, i, remaining)
                    remaining
                }
                is Byte -> {
                    buffer[i] = v
                    1
                }
                else -> throw IllegalStateException("Unknown node value type: ${v?.javaClass}")
            }
            i += s
            n = n.next!!
        }
        return buffer
    }

    override fun close() {
    }

    override fun flush() {
    }

    private fun newTail() {
        val newNode = SNode<Any>()
        cur.next = newNode
        cur = newNode
    }
}

/**
 * Appender interface, used for appending.
 *
 * @param A type of this appender
 * @param T type of appended object
 */
interface Appender<A : Appender<A, T>, T : Any> {

    /**
     * Appends and returns this.
     */
    fun append(t: T?): A
}

/**
 * [Appender] of which appended type [T] is segmented.
 *
 * @param A type of this appender
 * @param T type of appended object
 */
interface SegmentAppender<A : SegmentAppender<A, T>, T : Any> : Appender<A, T> {

    /**
     * Appends with [startIndex] and returns this.
     */
    fun append(t: T, startIndex: Int): A

    /**
     * Appends with [startIndex] and [endIndex] and returns this.
     */
    fun append(t: T, startIndex: Int, endIndex: Int): A
}