package com.dcchua.gweather.util.time

import java.text.SimpleDateFormat
import java.time.Duration
import java.time.LocalDateTime
import java.util.Date
import java.util.Locale
import java.util.TimeZone

fun toLocalTime(unixUtcSeconds: Long): String {
	try {
		val date = Date(unixUtcSeconds * 1000L)
		val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
		sdf.timeZone = TimeZone.getDefault()
		return sdf.format(date)
	} catch (_: Exception) {
		return "Invalid Time"
	}
}

fun getDurationToNextHour(now: LocalDateTime): Duration {
	val nextHour = now.plusHours(1)
		.withMinute(0)
		.withSecond(0)
		.withNano(0)

	return Duration.between(now, nextHour)
}