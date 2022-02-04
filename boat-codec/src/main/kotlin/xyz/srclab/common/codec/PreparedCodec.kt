package xyz.srclab.common.codec

import xyz.srclab.common.base.remainingLength
import java.io.OutputStream
import java.nio.ByteBuffer

/**
 * Codec operation with prepared data.
 */
interface PreparedCodec {

    fun doFinal(): ByteArray

    fun doFinal(dest: ByteArray): Int {
        return doFinal(dest, 0)
    }

    fun doFinal(dest: ByteArray, offset: Int): Int {
        return doFinal(dest, offset, remainingLength(dest.size, offset))
    }

    fun doFinal(dest: ByteArray, offset: Int, length: Int): Int

    fun doFinal(dest: ByteBuffer): Int {
        if (dest.hasArray()) {
            val startPos = dest.position()
            val array = dest.array()
            val arrayOffset = dest.arrayOffset() + startPos
            val codecLength = doFinal(array, arrayOffset, dest.remaining())
            dest.position(startPos + codecLength)
            return codecLength
        }
        val array = doFinal()
        dest.put(array)
        return array.size
    }

    fun doFinal(dest: OutputStream): Int {
        val array = doFinal()
        dest.write(array)
        return array.size
    }

    companion object {

        @JvmStatic
        fun PreparedCodec.toSync(lock: Any): PreparedCodec {
            return SyncPreparedCodec(this, lock)
        }

        private class SyncPreparedCodec(
            private val preparedCodec: PreparedCodec,
            private val lock: Any
        ) : PreparedCodec {

            override fun doFinal(): ByteArray {
                return synchronized(lock) {
                    preparedCodec.doFinal()
                }
            }

            override fun doFinal(dest: ByteArray): Int {
                return synchronized(lock) {
                    preparedCodec.doFinal(dest)
                }
            }

            override fun doFinal(dest: ByteArray, offset: Int): Int {
                return synchronized(lock) {
                    preparedCodec.doFinal(dest, offset)
                }
            }

            override fun doFinal(dest: ByteArray, offset: Int, length: Int): Int {
                return synchronized(lock) {
                    preparedCodec.doFinal(dest, offset, length)
                }
            }

            override fun doFinal(dest: ByteBuffer): Int {
                return synchronized(lock) {
                    preparedCodec.doFinal(dest)
                }
            }

            override fun doFinal(dest: OutputStream): Int {
                return synchronized(lock) {
                    preparedCodec.doFinal(dest)
                }
            }
        }
    }
}