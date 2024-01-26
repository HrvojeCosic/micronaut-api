package com.base.repositories;

import com.base.model.entities.Pet;
import io.micronaut.core.async.annotation.SingleResult;
import jakarta.inject.Singleton;

import java.util.HashMap;
import java.util.Map;

@Singleton
public class PetRepository {

    private static Map<Long, Pet> pets = new HashMap<Long, Pet>();
    private static Long petIndex = 1L;

    public Pet save(Pet pet) {
        pet.setId(++petIndex);
        pets.put(petIndex, pet);
        return pet;
    }
}
