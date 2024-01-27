package com.base.repositories;

import com.base.exceptions.InvalidResourceIdException;
import com.base.exceptions.ResourceNotFoundException;
import com.base.model.entities.Pet;
import io.micronaut.core.async.annotation.SingleResult;
import jakarta.inject.Singleton;

import java.util.HashMap;
import java.util.Map;

@Singleton
public class PetRepository {

    private static final Map<Long, Pet> pets = new HashMap<Long, Pet>();
    private static Long petIndex = 1L;

    public Pet save(Pet pet) {
        pet.setId(petIndex);
        pets.put(petIndex, pet);
        petIndex++;
        return pet;
    }

    public Pet update(Pet pet, Long id) {
        if (id == null || id <= 0) {
            throw new InvalidResourceIdException(String.format("Invalid id %d", id));
        }

        if (!pets.containsKey(id)) {
            throw new ResourceNotFoundException(String.format("Pet with id %d not found", id));
        }

        pets.put(id, pet);
        return pet;
    }
}
