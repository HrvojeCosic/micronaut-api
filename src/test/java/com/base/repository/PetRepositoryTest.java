package com.base.repository;

import com.base.exceptions.InvalidResourceIdException;
import com.base.exceptions.ResourceNotFoundException;
import com.base.model.entities.Pet;
import com.base.repositories.PetRepository;
import com.base.utils.PetUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PetRepositoryTest {

    PetRepository petRepository = new PetRepository();

    @Test
    public void save_shouldReturnPet_whenSuccessful() {
        Pet pet = petRepository.save(new Pet());
        assertNotNull(pet);
    }

    @Test
    public void update_shouldReturnPet_whenSuccessful() {
        Pet pet = petRepository.save(PetUtils.createValidPet());
        petRepository.update(new Pet(), pet.getId());
        assertNotNull(pet);
    }

    @Test
    public void update_shouldThrowInvalidResourceIdException_whenIdIsInvalid() {
        assertThrows(InvalidResourceIdException.class, () -> petRepository.update(new Pet(), null));
        assertThrows(InvalidResourceIdException.class, () -> petRepository.update(new Pet(), 0L));
        assertThrows(InvalidResourceIdException.class, () -> petRepository.update(new Pet(), -1L));
    }

    @Test
    public void update_shouldThrowResourceNotFoundException_whenPetIsNotFound() {
        assertThrows(ResourceNotFoundException.class, () -> petRepository.update(new Pet(), 999999L));
    }
}
