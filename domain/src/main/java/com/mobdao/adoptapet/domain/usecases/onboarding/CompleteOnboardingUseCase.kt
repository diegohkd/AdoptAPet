package com.mobdao.adoptapet.domain.usecases.onboarding

import com.mobdao.adoptapet.domain.dataapi.repositories.GeoLocationRepository
import com.mobdao.adoptapet.domain.dataapi.services.OnboardingService
import com.mobdao.adoptapet.domain.internal.mappers.AddressMapper
import com.mobdao.adoptapet.domain.models.Address
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CompleteOnboardingUseCase
    @Inject
    internal constructor(
        private val geoLocationRepository: GeoLocationRepository,
        private val onboardingService: OnboardingService,
        private val addressMapper: AddressMapper,
    ) {
        // TODO improve name of address?
        fun execute(address: Address): Flow<Unit> =
            flow {
                geoLocationRepository.cacheCurrentLocationAddress(addressMapper.map(address))
                onboardingService.saveOnboardingAsCompleted()
                emit(Unit)
            }
    }
