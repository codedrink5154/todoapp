package io.github.codedrink5154.todoapp.utils

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter


/* 用于格式化时间，完成Long? -> Instant 转换 */
fun longToInstant(millis: Long?): Instant? {
    if (millis == null) return null
    return Instant.ofEpochMilli(millis)
}

/* Instant? -> ZonedDateTime? */
fun instantToDateTime(value: Instant?): ZonedDateTime? {
    if (value == null) return null
    return value.atZone(ZoneId.systemDefault())
}

/* ZonedDateTime? -> String */
fun zonedDateTimeToString(value: ZonedDateTime?): String {
    if (value == null) return "无时间"
    return value.format(DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm"))
}
