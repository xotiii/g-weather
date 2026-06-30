package com.dcchua.gweather.core.util.coroutines

import kotlinx.coroutines.CancellationException

inline fun <T, R> T.coRunCatching(block: T.() -> R): Result<R> {
	return try {
		Result.success(block())
	} catch (c: CancellationException) {
		throw c
	} catch (e : Throwable) {
		Result.failure(e)
	}
}