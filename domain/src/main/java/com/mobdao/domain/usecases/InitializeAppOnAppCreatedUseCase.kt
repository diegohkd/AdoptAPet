package com.mobdao.domain.usecases

import com.mobdao.domain.api.services.LogService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class InitializeAppOnAppCreatedUseCase @Inject constructor(private val logService: LogService) {

    fun execute(): Flow<Unit> = flow {
        logService.init()
        emit(Unit)
    }
}
