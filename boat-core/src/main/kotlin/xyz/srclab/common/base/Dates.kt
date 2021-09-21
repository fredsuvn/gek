@file:JvmName("Dates")

package xyz.srclab.common.base

import xyz.srclab.common.exception.ImpossibleException
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.*
import java.time.format.DateTimeFormatter
import java.time.temporal.Temporal
import java.util.*

const val TIMESTAMP_PATTERN = "yyyyMMddhhmmssSSS"

const val SIMPLE_LOCAL_DATE_TIME_PATTERN = "yyyy-MM-dd hh:mm:ss"

const val SIMPLE_OFFSET_DATE_TIME_PATTERN = "yyyy-MM-dd hh:mm:ss XXX"

const val ISO_LOCAL_DATE_TIME_PATTERN = "yyyy-MM-dd'T'hh:mm:ss"

const val ISO_OFFSET_DATE_TIME_PATTERN = "yyyy-MM-dd'T'hh:mm:ssXXX"

const val ISO_ZONED_DATE_TIME_PATTERN = "yyyy-MM-dd'T'hh:mm:ssXXX'['VV']'"

@JvmField
val TIMESTAMP_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern(TIMESTAMP_PATTERN)

@JvmField
val SIMPLE_LOCAL_DATE_TIME_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern(SIMPLE_LOCAL_DATE_TIME_PATTERN)

@JvmField
val SIMPLE_OFFSET_DATE_TIME_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern(SIMPLE_OFFSET_DATE_TIME_PATTERN)

@JvmField
val ISO_LOCAL_DATE_TIME_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern(ISO_LOCAL_DATE_TIME_PATTERN)

@JvmField
val ISO_OFFSET_DATE_TIME_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern(ISO_OFFSET_DATE_TIME_PATTERN)

@JvmField
val ISO_ZONED_DATE_TIME_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern(ISO_ZONED_DATE_TIME_PATTERN)

@JvmField
val EPOCH_DATE: Date = Date.from(Instant.EPOCH)

@JvmField
val EPOCH_LOCAL_DATE_TIME: LocalDateTime = LocalDateTime.ofInstant(Instant.EPOCH, ZoneOffset.UTC)

@JvmField
val EPOCH_LOCAL_DATE: LocalDate = EPOCH_LOCAL_DATE_TIME.toLocalDate()

@JvmField
val EPOCH_LOCAL_TIME: LocalTime = EPOCH_LOCAL_DATE_TIME.toLocalTime()

@JvmField
val EPOCH_OFFSET_DATE_TIME: OffsetDateTime = EPOCH_LOCAL_DATE_TIME.atOffset(ZoneOffset.UTC)

@JvmField
val EPOCH_ZONED_DATE_TIME: ZonedDateTime = EPOCH_LOCAL_DATE_TIME.atZone(ZoneOffset.UTC)

@JvmName("dateTimeFormatter")
fun CharSequence.toDateTimeFormatter(): DateTimeFormatter {
    return DateTimeFormatter.ofPattern(this.toString())
}

@JvmName("dateFormat")
fun CharSequence.toDateFormat(): DateFormat {
    return SimpleDateFormat(this.toString())
}

/**
 * Guesses given date time string's pattern:
 *
 * * [SIMPLE_LOCAL_DATE_TIME_PATTERN] if string like `2021-09-16 03:00:18`;
 * * [SIMPLE_OFFSET_DATE_TIME_PATTERN] else if string like `2021-09-16 03:00:18 +08:00`;
 * * [ISO_LOCAL_DATE_TIME_PATTERN] else if string like `2021-09-16T03:00:18`;
 * * [ISO_OFFSET_DATE_TIME_PATTERN] else if string like `2021-09-16T03:00:18+08:00`;
 * * [ISO_ZONED_DATE_TIME_PATTERN] else if string like `2011-12-03T10:15:30+01:00[Europe/Paris]`;
 * * [TIMESTAMP_PATTERN] else if string is numeric;
 *
 * If no matched, return null.
 */
fun CharSequence.guessDateTimePatternOrNull(): String? {
    val localPatternLength = "2021-09-16 03:00:18".length
    if (this.length == localPatternLength) {
        if (this.contains(' ')) {
            return SIMPLE_LOCAL_DATE_TIME_PATTERN
        }
        if (this.contains('T')) {
            return ISO_LOCAL_DATE_TIME_PATTERN
        }
    }
    val simpleOffsetPatternLength = "2021-09-16 03:00:18 +08:00".length
    if (this.length == simpleOffsetPatternLength && this.contains(' ')) {
        return SIMPLE_OFFSET_DATE_TIME_PATTERN
    }
    val isoOffsetPatternLength = "2021-09-16T03:00:18+08:00".length
    if (this.length == isoOffsetPatternLength && this.contains('T')) {
        return ISO_OFFSET_DATE_TIME_PATTERN
    }
    val isoZonedPatternLength = "2011-12-03T10:15:30+01:00[]".length
    if (this.length > isoZonedPatternLength && this.endsWith(']')) {
        return ISO_ZONED_DATE_TIME_PATTERN
    }
    if (this.isNumeric()) {
        return TIMESTAMP_PATTERN
    }
    return null
}

/**
 * Guesses given date time string's formatter:
 *
 * * [SIMPLE_LOCAL_DATE_TIME_FORMATTER] if string like `2021-09-16 03:00:18`;
 * * [SIMPLE_OFFSET_DATE_TIME_FORMATTER] else if string like `2021-09-16 03:00:18 +08:00`;
 * * [ISO_LOCAL_DATE_TIME_FORMATTER] else if string like `2021-09-16T03:00:18`;
 * * [ISO_OFFSET_DATE_TIME_FORMATTER] else if string like `2021-09-16T03:00:18+08:00`;
 * * [ISO_ZONED_DATE_TIME_FORMATTER] else if string like `2011-12-03T10:15:30+01:00[Europe/Paris]`;
 * * [TIMESTAMP_FORMATTER] else if string is numeric;
 *
 * If no matched, return null.
 */
fun CharSequence.guessDateTimeFormatterOrNull(): DateTimeFormatter? {
    val localPatternLength = "2021-09-16 03:00:18".length
    if (this.length == localPatternLength) {
        if (this.contains(' ')) {
            return SIMPLE_LOCAL_DATE_TIME_FORMATTER
        }
        if (this.contains('T')) {
            return ISO_LOCAL_DATE_TIME_FORMATTER
        }
    }
    val simpleOffsetPatternLength = "2021-09-16 03:00:18 +08:00".length
    if (this.length == simpleOffsetPatternLength && this.contains(' ')) {
        return SIMPLE_OFFSET_DATE_TIME_FORMATTER
    }
    val isoOffsetPatternLength = "2021-09-16T03:00:18+08:00".length
    if (this.length == isoOffsetPatternLength && this.contains('T')) {
        return ISO_OFFSET_DATE_TIME_FORMATTER
    }
    val isoZonedPatternLength = "2011-12-03T10:15:30+01:00[]".length
    if (this.length > isoZonedPatternLength && this.endsWith(']')) {
        return ISO_ZONED_DATE_TIME_FORMATTER
    }
    if (this.isNumeric()) {
        return TIMESTAMP_FORMATTER
    }
    return null
}

fun Any?.toDate(pattern: String): Date {
    return toDate(pattern.toDateFormat())
}

@JvmOverloads
fun Any?.toDate(dateFormat: DateFormat? = null): Date {

    fun String.getDateFormat(): DateFormat {
        if (dateFormat !== null) {
            return dateFormat
        }
        return (this.guessDateTimePatternOrNull()
            ?: throw IllegalArgumentException("Unknown datetime formatter: $this.")).toDateFormat()
    }

    return when (this) {
        null -> EPOCH_DATE
        is Date -> this
        is Instant -> Date.from(this)
        is ZonedDateTime -> Date.from(toInstant())
        is OffsetDateTime -> Date.from(toInstant())
        is LocalDateTime -> Date.from(toInstant(OffsetDateTime.now().offset))
        is LocalDate -> Date.from(toLocalDateTime().toInstant(OffsetDateTime.now().offset))
        is LocalTime -> Date.from(toLocalDateTime().toInstant(OffsetDateTime.now().offset))
        is Temporal -> Date.from(Instant.from(this))
        is Number -> when (val value = toLong()) {
            0L -> EPOCH_DATE
            else -> Date(value)
        }
        false -> EPOCH_DATE
        true -> Date()
        else -> {
            val dateString = this.toString()
            dateString.getDateFormat().parse(dateString)
        }
    }
}

fun Any?.toInstant(dateTimePattern: String): Instant {
    return toInstant(DateTimeFormatter.ofPattern(dateTimePattern))
}

@JvmOverloads
fun Any?.toInstant(dateTimeFormatter: DateTimeFormatter? = null): Instant {
    return when (this) {
        null -> Instant.EPOCH
        is Instant -> this
        is Date -> toInstant()
        is ZonedDateTime -> toInstant()
        is OffsetDateTime -> toInstant()
        is LocalDateTime -> toInstant(OffsetDateTime.now().offset)
        is LocalDate -> toLocalDateTime().toInstant(OffsetDateTime.now().offset)
        is LocalTime -> toLocalDateTime().toInstant(OffsetDateTime.now().offset)
        is Temporal -> Instant.from(this)
        is Number -> when (val value = toLong()) {
            0L -> Instant.EPOCH
            else -> Instant.ofEpochMilli(value)
        }
        false -> Instant.EPOCH
        true -> Instant.now()
        else -> {
            val dateString = this.toString()
            Instant.from(
                dateString.getDateTimeFormatter(dateTimeFormatter).parse(dateString)
            )
        }
    }
}

fun Any?.toZonedDateTime(dateTimePattern: String): ZonedDateTime {
    return toZonedDateTime(DateTimeFormatter.ofPattern(dateTimePattern))
}

@JvmOverloads
fun Any?.toZonedDateTime(dateTimeFormatter: DateTimeFormatter? = null): ZonedDateTime {
    return when (this) {
        null -> EPOCH_ZONED_DATE_TIME
        is ZonedDateTime -> this
        is Date -> toInstant().atZone(ZoneOffset.UTC)
        is Instant -> atZone(ZoneOffset.UTC)
        is OffsetDateTime -> toZonedDateTime()
        is LocalDateTime -> atZone(ZoneId.systemDefault())
        is LocalDate -> ZonedDateTime.of(this, EPOCH_LOCAL_TIME, ZoneId.systemDefault())
        is LocalTime -> ZonedDateTime.of(EPOCH_LOCAL_DATE, this, ZoneId.systemDefault())
        is Temporal -> ZonedDateTime.from(this)
        is Number -> when (val value = toLong()) {
            0L -> EPOCH_ZONED_DATE_TIME
            else -> Instant.ofEpochMilli(value).atZone(ZoneOffset.UTC)
        }
        false -> EPOCH_ZONED_DATE_TIME
        true -> ZonedDateTime.now()
        else -> {
            val dateString = this.toString()
            ZonedDateTime.from(
                dateString.getDateTimeFormatter(dateTimeFormatter).parse(dateString)
            )
        }
    }
}

fun Any?.toOffsetDateTime(dateTimePattern: String): OffsetDateTime {
    return toOffsetDateTime(DateTimeFormatter.ofPattern(dateTimePattern))
}

@JvmOverloads
fun Any?.toOffsetDateTime(dateTimeFormatter: DateTimeFormatter? = null): OffsetDateTime {
    return when (this) {
        null -> EPOCH_OFFSET_DATE_TIME
        is OffsetDateTime -> this
        is Date -> toInstant().atOffset(ZoneOffset.UTC)
        is Instant -> atOffset(ZoneOffset.UTC)
        is ZonedDateTime -> toOffsetDateTime()
        is LocalDateTime -> toZonedDateTime().toOffsetDateTime()
        is LocalDate -> toZonedDateTime().toOffsetDateTime()
        is LocalTime -> toZonedDateTime().toOffsetDateTime()
        is Temporal -> OffsetDateTime.from(this)
        is Number -> when (val value = toLong()) {
            0L -> EPOCH_OFFSET_DATE_TIME
            else -> Instant.ofEpochMilli(value).atOffset(ZoneOffset.UTC)
        }
        false -> EPOCH_OFFSET_DATE_TIME
        true -> OffsetDateTime.now()
        else -> {
            val dateString = this.toString()
            OffsetDateTime.from(
                dateString.getDateTimeFormatter(dateTimeFormatter).parse(dateString)
            )
        }
    }
}

fun Any?.toLocalDateTime(dateTimePattern: String): LocalDateTime {
    return toLocalDateTime(DateTimeFormatter.ofPattern(dateTimePattern))
}

@JvmOverloads
fun Any?.toLocalDateTime(dateTimeFormatter: DateTimeFormatter? = null): LocalDateTime {
    return when (this) {
        null -> EPOCH_LOCAL_DATE_TIME
        is LocalDateTime -> this
        is Date -> LocalDateTime.ofInstant(toInstant(), ZoneOffset.UTC)
        is Instant -> LocalDateTime.ofInstant(this, ZoneOffset.UTC)
        is ZonedDateTime -> toLocalDateTime()
        is OffsetDateTime -> toLocalDateTime()
        is LocalDate -> LocalDateTime.of(this, EPOCH_LOCAL_TIME)
        is LocalTime -> LocalDateTime.of(EPOCH_LOCAL_DATE, this)
        is Temporal -> LocalDateTime.from(this)
        is Number -> when (val value = toLong()) {
            0L -> EPOCH_LOCAL_DATE_TIME
            else -> LocalDateTime.ofInstant(Instant.ofEpochMilli(value), ZoneOffset.UTC)
        }
        false -> EPOCH_LOCAL_DATE_TIME
        true -> LocalDateTime.now()
        else -> {
            val dateString = this.toString()
            LocalDateTime.from(
                dateString.getDateTimeFormatter(dateTimeFormatter).parse(dateString)
            )
        }
    }
}

fun Any?.toLocalDate(dateTimePattern: String): LocalDate {
    return toLocalDate(DateTimeFormatter.ofPattern(dateTimePattern))
}

@JvmOverloads
fun Any?.toLocalDate(dateTimeFormatter: DateTimeFormatter? = null): LocalDate {
    return when (this) {
        null -> EPOCH_LOCAL_DATE
        is LocalDate -> this
        is Date -> toInstant().atOffset(ZoneOffset.UTC).toLocalDate()
        is Instant -> atOffset(ZoneOffset.UTC).toLocalDate()
        is ZonedDateTime -> toLocalDate()
        is OffsetDateTime -> toLocalDate()
        is LocalDateTime -> toLocalDate()
        is LocalTime -> EPOCH_LOCAL_DATE
        is Temporal -> LocalDate.from(this)
        is Number -> when (val value = toLong()) {
            0L -> EPOCH_LOCAL_DATE
            else -> Instant.ofEpochMilli(value).atOffset(ZoneOffset.UTC).toLocalDate()
        }
        false -> EPOCH_LOCAL_DATE
        true -> LocalDate.now()
        else -> {
            val dateString = this.toString()
            LocalDate.from(
                dateString.getDateTimeFormatter(dateTimeFormatter).parse(dateString)
            )
        }
    }
}

fun Any?.toLocalTime(dateTimePattern: String): LocalTime {
    return toLocalTime(DateTimeFormatter.ofPattern(dateTimePattern))
}

@JvmOverloads
fun Any?.toLocalTime(dateTimeFormatter: DateTimeFormatter? = null): LocalTime {
    return when (this) {
        null -> EPOCH_LOCAL_TIME
        is LocalTime -> this
        is Date -> toInstant().atOffset(ZoneOffset.UTC).toLocalTime()
        is Instant -> atOffset(ZoneOffset.UTC).toLocalTime()
        is ZonedDateTime -> toLocalTime()
        is OffsetDateTime -> toLocalTime()
        is LocalDateTime -> toLocalTime()
        is LocalDate -> EPOCH_LOCAL_TIME
        is Temporal -> LocalTime.from(this)
        is Number -> when (val value = toLong()) {
            0L -> EPOCH_LOCAL_TIME
            else -> Instant.ofEpochMilli(value).atOffset(ZoneOffset.UTC).toLocalTime()
        }
        false -> EPOCH_LOCAL_TIME
        true -> LocalTime.now()
        else -> {
            val dateString = this.toString()
            LocalTime.from(
                dateString.getDateTimeFormatter(dateTimeFormatter).parse(dateString)
            )
        }
    }
}

fun Any?.toDuration(): Duration {
    return when (this) {
        null -> Duration.ZERO
        is Duration -> this
        is Number -> when (val value = toLong()) {
            0L -> Duration.ZERO
            else -> Duration.ofMillis(value)
        }
        false -> Duration.ZERO
        else -> Duration.parse(toString())
    }
}

fun Any?.toTimestamp(): String {
    val dateString = this.toString()
    val formatter = dateString.guessDateTimeFormatterOrNull()
    if (formatter === null) {
        throw IllegalArgumentException("Unknown datetime formatter: $this")
    }
    return when (formatter) {
        TIMESTAMP_FORMATTER ->
            TIMESTAMP_FORMATTER.format(TIMESTAMP_FORMATTER.parse(dateString))
        SIMPLE_LOCAL_DATE_TIME_FORMATTER ->
            SIMPLE_LOCAL_DATE_TIME_FORMATTER.format(SIMPLE_LOCAL_DATE_TIME_FORMATTER.parse(dateString))
        SIMPLE_OFFSET_DATE_TIME_FORMATTER ->
            SIMPLE_OFFSET_DATE_TIME_FORMATTER.format(SIMPLE_OFFSET_DATE_TIME_FORMATTER.parse(dateString))
        ISO_LOCAL_DATE_TIME_FORMATTER ->
            ISO_LOCAL_DATE_TIME_FORMATTER.format(ISO_LOCAL_DATE_TIME_FORMATTER.parse(dateString))
        ISO_OFFSET_DATE_TIME_FORMATTER ->
            ISO_OFFSET_DATE_TIME_FORMATTER.format(ISO_OFFSET_DATE_TIME_FORMATTER.parse(dateString))
        ISO_ZONED_DATE_TIME_FORMATTER ->
            ISO_ZONED_DATE_TIME_FORMATTER.format(ISO_ZONED_DATE_TIME_FORMATTER.parse(dateString))
        else -> throw ImpossibleException(dateString)
    }
}

private fun String.getDateTimeFormatter(dateTimeFormatter: DateTimeFormatter?): DateTimeFormatter {
    if (dateTimeFormatter !== null) {
        return dateTimeFormatter
    }
    return this.guessDateTimeFormatterOrNull() ?: throw IllegalArgumentException("Unknown datetime formatter: $this.")
}