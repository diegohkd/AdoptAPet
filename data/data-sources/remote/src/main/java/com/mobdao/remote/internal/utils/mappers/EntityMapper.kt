package com.mobdao.remote.internal.utils.mappers

import com.mobdao.adoptapet.domain.entities.AddressEntity
import com.mobdao.adoptapet.domain.entities.BreedsEntity
import com.mobdao.adoptapet.domain.entities.PetEntity
import com.mobdao.adoptapet.domain.entities.PetGenderEntity
import com.mobdao.adoptapet.domain.entities.PetTypeEntity
import com.mobdao.adoptapet.domain.entities.PhotoEntity
import com.mobdao.remote.internal.responses.Animal
import com.mobdao.remote.internal.responses.AnimalType
import com.mobdao.remote.internal.responses.AnimalType.BARNYARD
import com.mobdao.remote.internal.responses.AnimalType.BIRD
import com.mobdao.remote.internal.responses.AnimalType.CAT
import com.mobdao.remote.internal.responses.AnimalType.DOG
import com.mobdao.remote.internal.responses.AnimalType.HORSE
import com.mobdao.remote.internal.responses.AnimalType.RABBIT
import com.mobdao.remote.internal.responses.AnimalType.SCALES_FINS_AND_OTHER
import com.mobdao.remote.internal.responses.AnimalType.SMALL_AND_FURRY
import com.mobdao.remote.internal.responses.Gender
import com.mobdao.remote.internal.responses.GeocodeResponse
import com.mobdao.remote.internal.utils.DomainEntityAnimalType
import com.mobdao.remote.internal.utils.DomainEntityContact
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class EntityMapper @Inject constructor() {
    fun toPets(animals: List<Animal>): List<PetEntity> =
        animals.map { animal ->
            with(animal) {
                PetEntity(
                    id = id,
                    type =
                        when (type) {
                            DOG -> DomainEntityAnimalType.DOG
                            CAT -> DomainEntityAnimalType.CAT
                            RABBIT -> DomainEntityAnimalType.RABBIT
                            SMALL_AND_FURRY -> DomainEntityAnimalType.SMALL_AND_FURRY
                            HORSE -> DomainEntityAnimalType.HORSE
                            BIRD -> DomainEntityAnimalType.BIRD
                            SCALES_FINS_AND_OTHER -> DomainEntityAnimalType.SCALES_FINS_AND_OTHER
                            BARNYARD -> DomainEntityAnimalType.BARNYARD
                        },
                    name = name,
                    breeds =
                        BreedsEntity(
                            primary = breeds.primary,
                            secondary = breeds.secondary,
                        ),
                    age = age.orEmpty(),
                    size = size.orEmpty(),
                    gender = gender.orEmpty(),
                    description = description.orEmpty(),
                    distance = distance,
                    photos =
                        photos.map { photo ->
                            PhotoEntity(
                                smallUrl = photo.small,
                                mediumUrl = photo.medium,
                                largeUrl = photo.large,
                                fullUrl = photo.full,
                            )
                        },
                    contact =
                        DomainEntityContact(
                            email = contact?.email.orEmpty(),
                            phone = contact?.phone.orEmpty(),
                        ),
                )
            }
        }

    fun toAnimalType(petType: PetTypeEntity): AnimalType =
        when (petType) {
            PetTypeEntity.DOG -> DOG
            PetTypeEntity.CAT -> CAT
            PetTypeEntity.RABBIT -> RABBIT
            PetTypeEntity.SMALL_FURRY -> SMALL_AND_FURRY
            PetTypeEntity.HORSE -> HORSE
            PetTypeEntity.BIRD -> BIRD
            PetTypeEntity.SCALES_FINS_OTHER -> SCALES_FINS_AND_OTHER
            PetTypeEntity.BARNYARD -> BARNYARD
        }

    fun toGender(gender: PetGenderEntity): Gender =
        when (gender) {
            PetGenderEntity.MALE -> Gender.MALE
            PetGenderEntity.FEMALE -> Gender.FEMALE
        }

    fun toAddresses(geocodeResponse: GeocodeResponse): List<AddressEntity> =
        geocodeResponse.results.map { geocodeResult ->
            with(geocodeResult) {
                AddressEntity(
                    addressLine = formatted,
                    latitude = lat,
                    longitude = lon,
                )
            }
        }
}
