package io.github.codedrink5154.todoapp.utils

import java.time.Instant
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter


/* 用于格式化时间，完成Long? -> Instant 转换 */
fun longToInstant(millis: Long?): Instant? {
    if (millis == null) return null
    return Instant.ofEpochMilli(millis)
}

/* Instant? -> ZonedDateTime? */
fun Instant?.instantToDateTime(): ZonedDateTime? {
    if (this == null) return null
    return this.atZone(ZoneId.systemDefault())
}

/* ZonedDateTime? -> String */
fun ZonedDateTime?.toStringZHCN(): String {
    if (this == null) return "无时间"
    return this.format(DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm"))
}

/* 工具函数，将提醒时间（Int?）转换为Long? */
// 在这里我们需要对空值做处理
// 如果提醒时间为空，则不需要提醒；如果截止时间为空，也不需要提醒
fun transformRemindTime(remind: Int?, until: Long?): Long? {
    return if (until != null && remind != null) {
        (until - remind * 60 * 1000L).takeIf {
            // 在这里判断提醒时间是否有效，避免把提醒放到过去
            it > System.currentTimeMillis()
        }
    } else {
        null
    }
}

fun combineUtcDateAndLocalTime(
    dateUtcMillis: Long?,
    localTime: LocalTime,
    zoneId: ZoneId = ZoneId.systemDefault(),
): Long? {
    if (dateUtcMillis == null) return null
    val localDate = Instant.ofEpochMilli(dateUtcMillis)
        .atZone(ZoneOffset.UTC)
        .toLocalDate()
    return localDate.atTime(localTime)
        .atZone(zoneId)
        .toInstant()
        .toEpochMilli()
}
