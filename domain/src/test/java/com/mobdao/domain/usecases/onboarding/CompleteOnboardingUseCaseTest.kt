package com.mobdao.domain.usecases.onboarding

import com.mobdao.common.testutils.mockfactories.domain.AddressMockFactory
import com.mobdao.common.testutils.mockfactories.domain.api.entities.AddressEntityMockFactory
import com.mobdao.domain.api.repositories.GeoLocationRepository
import com.mobdao.domain.api.services.OnboardingService
import com.mobdao.domain.models.Address
import com.mobdao.domain.utils.AddressEntity
import com.mobdao.domain.utils.mappers.AddressMapper
import io.mockk.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test

class CompleteOnboardingUseCaseTest {

    private val address: Address = AddressMockFactory.create()
    private val addressEntity: AddressEntity = AddressEntityMockFactory.create()

    private val geoLocationRepository: GeoLocationRepository = mockk {
        coJustRun { cacheCurrentLocationAddress(addressEntity) }
    }
    private val onboardingService: OnboardingService = mockk {
        justRun { saveOnboardingAsCompleted() }
    }
    private val addressMapper: AddressMapper = mockk {
        every { map(address) } returns addressEntity
    }

    private val tested = CompleteOnboardingUseCase(
        geoLocationRepository = geoLocationRepository,
        onboardingService = onboardingService,
        addressMapper = addressMapper,
    )

    @Test
    fun `given address when execute then address is cached as current location`() = runTest {
        // given / when
        tested.execute(address).first()

        // then
        coVerify { geoLocationRepository.cacheCurrentLocationAddress(addressEntity) }
    }

    @Test
    fun `when execute onboarding is saved as completed`() = runTest {
        // given / when
        tested.execute(address).first()

        // then
        coVerify { onboardingService.saveOnboardingAsCompleted() }
    }
}