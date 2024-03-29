package com.base.utils;

import com.base.model.dto.PetDto;
import com.base.model.entities.Pet;
import com.base.model.enums.PetCategory;
import com.base.model.entities.Tag;
import com.base.model.enums.PetStatus;

import java.util.List;

public class PetUtils {

    private static final Long validPetId = 2L;

    public static PetDto createValidPetDto() {
        return new PetDto(validPetId, "Name", PetCategory.dog, List.of(new Tag("some tag")), "available");
    }

    public static PetDto createInvalidPetDto() {
        return new PetDto();
    }

    public static Pet createValidPet() {
        Pet pet = new Pet("Name", PetCategory.dog, List.of(new Tag()), "available");
        pet.setId(validPetId);
        return pet;
    }
}
