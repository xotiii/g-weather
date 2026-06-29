package com.dcchua.gweather.testutils

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher

data class FlowCollectorResult<T>(
	private val values: List<T>,
	private val cancelCollection: () -> Unit
) {
	fun getValues() = values
	fun getLatestValue() = values.last()
	fun finishCollection() {
		cancelCollection()
	}
}
@ExperimentalCoroutinesApi
fun <T> TestScope.testFlowCollection(flow: Flow<T>): FlowCollectorResult<T> {
	val collectedValues = mutableListOf<T>()

	// Launch using the backgroundScope so it's guaranteed to clean up if not cancelled manually
	val job: Job = backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
		flow.collect { collectedValues.add(it) }
	}

	return FlowCollectorResult(
		values = collectedValues,
		cancelCollection = { job.cancel() }
	)
}