package com.mobdao.data.services

import com.mobdao.common.config.AppConfig
import com.mobdao.data.utils.wrappers.TimberWrapper
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

class LogServiceImplTest {

    private val appConfig: AppConfig = mockk {
        every { isDebugBuild } returns false
    }
    private val timber: TimberWrapper = mockk {
        justRun { plantDebugTree() }
    }

    private val tested = LogServiceImpl(appConfig, timber)

    @Test
    fun `given is debug build when init then timber plants debug tree`() {
        // given
        every { appConfig.isDebugBuild } returns true

        // when
        tested.init()

        // then
        verify { timber.plantDebugTree() }
    }

    @Test
    fun `given is not debug build when init then timber does not plant debug tree`() {
        // given / when
        tested.init()

        // then
        verify(exactly = 0) { timber.plantDebugTree() }
    }
}