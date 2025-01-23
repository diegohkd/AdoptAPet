package com.mobdao.adoptapet.domain.internal.mappers

import com.mobdao.adoptapet.domain.entities.AddressEntity
import com.mobdao.adoptapet.domain.entities.PetGenderEntity
import com.mobdao.adoptapet.domain.entities.PetGenderEntity.FEMALE
import com.mobdao.adoptapet.domain.entities.PetGenderEntity.MALE
import com.mobdao.adoptapet.domain.entities.PetTypeEntity
import com.mobdao.adoptapet.domain.entities.PetTypeEntity.BARNYARD
import com.mobdao.adoptapet.domain.entities.PetTypeEntity.BIRD
import com.mobdao.adoptapet.domain.entities.PetTypeEntity.CAT
import com.mobdao.adoptapet.domain.entities.PetTypeEntity.DOG
import com.mobdao.adoptapet.domain.entities.PetTypeEntity.HORSE
import com.mobdao.adoptapet.domain.entities.PetTypeEntity.RABBIT
import com.mobdao.adoptapet.domain.entities.PetTypeEntity.SCALES_FINS_OTHER
import com.mobdao.adoptapet.domain.entities.PetTypeEntity.SMALL_FURRY
import com.mobdao.adoptapet.domain.entities.SearchFilterEntity
import com.mobdao.adoptapet.domain.models.Address
import com.mobdao.adoptapet.domain.models.PetGender
import com.mobdao.adoptapet.domain.models.PetType
import com.mobdao.adoptapet.domain.models.SearchFilter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class SearchFilterMapper @Inject constructor() {
    fun mapToEntity(searchFilter: SearchFilter): SearchFilterEntity =
        SearchFilterEntity(
            address =
                searchFilter.address.let {
                    AddressEntity(
                        addressLine = it.addressLine,
                        latitude = it.latitude,
                        longitude = it.longitude,
                    )
                },
            petType = searchFilter.petType?.toEntity(),
            petGenders = searchFilter.petGenders.map { it.toEntity() },
        )

    fun mapFromEntity(searchFilterEntity: SearchFilterEntity): SearchFilter =
        SearchFilter(
            address =
                searchFilterEntity.address.let {
                    Address(
                        addressLine = it.addressLine,
                        latitude = it.latitude,
                        longitude = it.longitude,
                    )
                },
            petType = searchFilterEntity.petType?.toDomainModel(),
            petGenders = searchFilterEntity.petGenders.map { it.toDomainModel() },
        )

    private fun PetType.toEntity(): PetTypeEntity =
        when (this) {
            PetType.DOG -> DOG
            PetType.CAT -> CAT
            PetType.RABBIT -> RABBIT
            PetType.BIRD -> BIRD
            PetType.SMALL_FURRY -> SMALL_FURRY
            PetType.HORSE -> HORSE
            PetType.BARNYARD -> BARNYARD
            PetType.SCALES_FINS_OTHER -> SCALES_FINS_OTHER
        }

    private fun PetGender.toEntity(): PetGenderEntity =
        when (this) {
            PetGender.MALE -> MALE
            PetGender.FEMALE -> FEMALE
        }

    private fun PetTypeEntity.toDomainModel(): PetType =
        when (this) {
            DOG -> PetType.DOG
            CAT -> PetType.CAT
            RABBIT -> PetType.RABBIT
            BIRD -> PetType.BIRD
            SMALL_FURRY -> PetType.SMALL_FURRY
            HORSE -> PetType.HORSE
            BARNYARD -> PetType.BARNYARD
            SCALES_FINS_OTHER -> PetType.SCALES_FINS_OTHER
        }

    private fun PetGenderEntity.toDomainModel(): PetGender =
        when (this) {
            MALE -> PetGender.MALE
            FEMALE -> PetGender.FEMALE
        }
}
