package com.base.repositories;

import com.base.exceptions.InvalidResourceIdException;
import com.base.exceptions.ResourceNotFoundException;
import com.base.model.entities.Pet;
import io.micronaut.core.async.annotation.SingleResult;
import jakarta.inject.Singleton;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        validateId(id);
        pets.put(id, pet);
        return pet;
    }

    public List<Pet> findByStatus(List<String> statuses) {
        return pets.values().stream()
                .filter(pet -> statuses.contains(pet.getStatus()))
                .collect(Collectors.toList());
    }

    public List<Pet> findByTags(List<String> tags) {
        return pets.values().stream()
                .filter(pet -> pet.getTags().stream().anyMatch(tag -> tags.contains(tag.getName())))
                .collect(Collectors.toList());
    }

    public Pet findById(Long id) {
        validateId(id);
        return pets.get(id);
    }

    private void validateId(Long id) {
        if (id == null || id <= 0) {
            throw new InvalidResourceIdException(String.format("Invalid id %d", id));
        }

        if (!pets.containsKey(id)) {
            throw new ResourceNotFoundException(String.format("Pet with id %d not found", id));
        }
    }
}
