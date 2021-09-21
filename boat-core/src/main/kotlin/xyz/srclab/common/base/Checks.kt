@file:JvmName("Checks")

package xyz.srclab.common.base

@Throws(IllegalArgumentException::class)
fun checkArgument(expression: Boolean) {
    if (!expression) {
        throw IllegalArgumentException()
    }
}

@Throws(IllegalArgumentException::class)
fun checkArgument(expression: Boolean, message: String?) {
    if (!expression) {
        throw IllegalArgumentException(message)
    }
}

@Throws(IllegalArgumentException::class)
fun checkArgument(expression: Boolean, messagePattern: String?, vararg messageArgs: Any?) {
    if (!expression) {
        throw IllegalArgumentException(messagePattern?.fastFormat(*messageArgs))
    }
}

@Throws(IllegalStateException::class)
fun checkState(expression: Boolean) {
    if (!expression) {
        throw IllegalStateException()
    }
}

@Throws(IllegalStateException::class)
fun checkState(expression: Boolean, message: String?) {
    if (!expression) {
        throw IllegalStateException(message)
    }
}

@Throws(IllegalStateException::class)
fun checkState(expression: Boolean, messagePattern: String?, vararg messageArgs: Any?) {
    if (!expression) {
        throw IllegalStateException(messagePattern?.fastFormat(*messageArgs))
    }
}

@Throws(NullPointerException::class)
fun checkNull(expression: Boolean) {
    if (!expression) {
        throw NullPointerException()
    }
}

@Throws(NullPointerException::class)
fun checkNull(expression: Boolean, message: String?) {
    if (!expression) {
        throw NullPointerException(message)
    }
}

@Throws(NullPointerException::class)
fun checkNull(expression: Boolean, messagePattern: String?, vararg messageArgs: Any?) {
    if (!expression) {
        throw NullPointerException(messagePattern?.fastFormat(*messageArgs))
    }
}

@Throws(UnsupportedOperationException::class)
fun checkSupported(expression: Boolean) {
    if (!expression) {
        throw UnsupportedOperationException()
    }
}

@Throws(UnsupportedOperationException::class)
fun checkSupported(expression: Boolean, message: String?) {
    if (!expression) {
        throw UnsupportedOperationException(message)
    }
}

@Throws(UnsupportedOperationException::class)
fun checkSupported(expression: Boolean, messagePattern: String?, vararg messageArgs: Any?) {
    if (!expression) {
        throw UnsupportedOperationException(messagePattern?.fastFormat(*messageArgs))
    }
}

@Throws(NoSuchElementException::class)
fun checkElement(expression: Boolean) {
    if (!expression) {
        throw NoSuchElementException()
    }
}

@Throws(NoSuchElementException::class)
fun checkElement(expression: Boolean, message: String?) {
    if (!expression) {
        throw NoSuchElementException(message)
    }
}

@Throws(NoSuchElementException::class)
fun checkElement(expression: Boolean, messagePattern: String?, vararg messageArgs: Any?) {
    if (!expression) {
        throw NoSuchElementException(messagePattern?.fastFormat(*messageArgs))
    }
}

@Throws(IndexOutOfBoundsException::class)
fun checkBounds(expression: Boolean) {
    if (!expression) {
        throw IndexOutOfBoundsException()
    }
}

@Throws(IndexOutOfBoundsException::class)
fun checkBounds(expression: Boolean, message: String?) {
    if (!expression) {
        throw IndexOutOfBoundsException(message)
    }
}

@Throws(IndexOutOfBoundsException::class)
fun checkBounds(expression: Boolean, messagePattern: String?, vararg messageArgs: Any?) {
    if (!expression) {
        throw IndexOutOfBoundsException(messagePattern?.fastFormat(*messageArgs))
    }
}

@Throws(IndexOutOfBoundsException::class)
fun isIndexInBounds(index: Int, startInclusive: Int, endExclusive: Int): Boolean {
    return index in startInclusive until endExclusive
}

@Throws(IndexOutOfBoundsException::class)
fun isIndexInBounds(index: Long, startInclusive: Long, endExclusive: Long): Boolean {
    return index in startInclusive until endExclusive
}

@Throws(IndexOutOfBoundsException::class)
fun checkIndexInBounds(index: Int, startInclusive: Int, endExclusive: Int) {
    if (!isIndexInBounds(index, startInclusive, endExclusive)) {
        throw IndexOutOfBoundsException(
            "Index out of bounds[" +
                "index: $index, startInclusive: $startInclusive, endExclusive: $endExclusive" +
                "]."
        )
    }
}

@Throws(IndexOutOfBoundsException::class)
fun checkIndexInBounds(index: Long, startInclusive: Long, endExclusive: Long) {
    if (!isIndexInBounds(index, startInclusive, endExclusive)) {
        throw IndexOutOfBoundsException(
            "Index out of bounds[" +
                "index: $index, startInclusive: $startInclusive, endExclusive: $endExclusive" +
                "]."
        )
    }
}

@Throws(IndexOutOfBoundsException::class)
fun isRangeInLength(startInclusive: Int, endExclusive: Int, length: Int): Boolean {
    return startInclusive >= 0 && endExclusive <= length && startInclusive <= endExclusive
}

@Throws(IndexOutOfBoundsException::class)
fun isRangeInLength(startInclusive: Long, endExclusive: Long, length: Long): Boolean {
    return startInclusive >= 0 && endExclusive <= length && startInclusive <= endExclusive
}

@Throws(IndexOutOfBoundsException::class)
fun isRangeInBounds(
    startInclusive: Int, endExclusive: Int, startBoundInclusive: Int, endBoundExclusive: Int
): Boolean {
    return startInclusive >= startBoundInclusive
        && endExclusive <= endBoundExclusive
        && startInclusive <= endExclusive
}

@Throws(IndexOutOfBoundsException::class)
fun isRangeInBounds(
    startInclusive: Long, endExclusive: Long, startBoundInclusive: Long, endBoundExclusive: Long
): Boolean {
    return startInclusive >= startBoundInclusive
        && endExclusive <= endBoundExclusive
        && startInclusive <= endExclusive
}

@Throws(IndexOutOfBoundsException::class)
fun checkRangeInLength(startInclusive: Int, endExclusive: Int, length: Int) {
    if (!isRangeInLength(startInclusive, endExclusive, length)) {
        throw IndexOutOfBoundsException(
            "Range out of bounds[" +
                "startInclusive: $startInclusive, endExclusive: $endExclusive, length: $length" +
                "]."
        )
    }
}

@Throws(IndexOutOfBoundsException::class)
fun checkRangeInLength(startInclusive: Long, endExclusive: Long, length: Long) {
    if (!isRangeInLength(startInclusive, endExclusive, length)) {
        throw IndexOutOfBoundsException(
            "Range out of bounds[" +
                "startInclusive: $startInclusive, endExclusive: $endExclusive, length: $length" +
                "]."
        )
    }
}

@Throws(IndexOutOfBoundsException::class)
fun checkRangeInBounds(startInclusive: Int, endExclusive: Int, startBoundInclusive: Int, endBoundExclusive: Int) {
    if (!isRangeInBounds(startInclusive, endExclusive, startBoundInclusive, endBoundExclusive)) {
        throw IndexOutOfBoundsException(
            "Range out of bounds[" +
                "startInclusive: $startInclusive, endExclusive: $endExclusive, " +
                "startBoundInclusive: $startBoundInclusive, endBoundExclusive: $endBoundExclusive" +
                "]."
        )
    }
}

@Throws(IndexOutOfBoundsException::class)
fun checkRangeInBounds(
    startInclusive: Long,
    endExclusive: Long,
    startBoundInclusive: Long,
    endBoundExclusive: Long
) {
    if (!isRangeInBounds(startInclusive, endExclusive, startBoundInclusive, endBoundExclusive)) {
        throw IndexOutOfBoundsException(
            "Range out of bounds[" +
                "startInclusive: $startInclusive, endExclusive: $endExclusive, " +
                "startBoundInclusive: $startBoundInclusive, endBoundExclusive: $endBoundExclusive" +
                "]."
        )
    }
}

private fun String.fastFormat(vararg messageArgs: Any?): String {
    return FastCharsFormat.format(this, *messageArgs)
}