package jp.itohiro.sample.util

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter
import java.sql.Timestamp


object DateUtils {
    private val DATE_TIME_FORMATTER: DateTimeFormatter = DateTimeFormat.forPattern("YYYYMMdd")

    private val DATE_TIME_FORMATTER_FULL: DateTimeFormatter = DateTimeFormat.forPattern("YYYYMMdd HH:mm:ss.SSS")

    fun parseFull(dateTimeString: String?): Timestamp {
        val dateTime: DateTime = DATE_TIME_FORMATTER_FULL.parseDateTime(dateTimeString)
        return Timestamp(dateTime.toDateTime().millis)
    }

    fun printFull(ts: Timestamp): String {
        return DATE_TIME_FORMATTER_FULL.print(ts.getTime())
    }

    fun parse(dateTimeString: String?): Timestamp {
        val dateTime: DateTime = DATE_TIME_FORMATTER.parseDateTime(dateTimeString+"01")
        return Timestamp(dateTime.toDateTime().millis)
    }

    fun print(ts: Timestamp): String {
        return DATE_TIME_FORMATTER.print(ts.getTime())
    }

    val currentTimestamp: Timestamp
        get() = Timestamp(System.currentTimeMillis())
}