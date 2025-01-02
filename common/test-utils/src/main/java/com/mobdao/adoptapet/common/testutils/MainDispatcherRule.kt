package com.mobdao.adoptapet.common.testutils

import com.mobdao.adoptapet.common.coroutines.CoroutineDispatcherProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description

@ExperimentalCoroutinesApi
class MainDispatcherRule(
    private val dispatcher: TestDispatcher = UnconfinedTestDispatcher(),
) : TestWatcher() {
    var dispatcherProvider: CoroutineDispatcherProvider = CoroutineDispatcherProvider()
        private set

    override fun starting(description: Description?) {
        super.starting(description)
        Dispatchers.setMain(dispatcher)
        dispatcherProvider =
            CoroutineDispatcherProvider(
                main = dispatcher,
                default = dispatcher,
                io = dispatcher,
            )
    }

    override fun finished(description: Description?) {
        super.finished(description)
        Dispatchers.resetMain()
    }
}
