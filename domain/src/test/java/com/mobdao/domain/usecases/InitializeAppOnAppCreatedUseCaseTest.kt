package com.mobdao.domain.usecases

import com.mobdao.adoptapet.domain.dataapi.services.LogService
import com.mobdao.adoptapet.domain.usecases.InitializeAppOnAppCreatedUseCase
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test

class InitializeAppOnAppCreatedUseCaseTest {
    private val logService: LogService =
        mockk {
            justRun { init() }
        }

    private val tested = InitializeAppOnAppCreatedUseCase(logService = logService)

    @Test
    fun `when executed then log service is initialized`() =
        runTest {
            // when
            tested.execute().first()

            // then
            verify { logService.init() }
        }
}
