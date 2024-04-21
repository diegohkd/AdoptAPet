package com.mobdao.domain.usecases.onboarding

import com.mobdao.domain.dataapi.repositories.GeoLocationRepository
import com.mobdao.domain.dataapi.services.OnboardingService
import com.mobdao.domain.models.Address
import com.mobdao.domain.utils.mappers.AddressMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CompleteOnboardingUseCase @Inject constructor(
    private val geoLocationRepository: GeoLocationRepository,
    private val onboardingService: OnboardingService,
    private val addressMapper: AddressMapper,
) {

    // TODO improve name of address?
    fun execute(address: Address): Flow<Unit> = flow {
        geoLocationRepository.cacheCurrentLocationAddress(addressMapper.map(address))
        onboardingService.saveOnboardingAsCompleted()
        emit(Unit)
    }
}