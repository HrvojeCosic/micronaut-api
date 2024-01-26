package com.base.repository;

import com.base.model.entities.Pet;
import com.base.repositories.PetRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PetRepositoryTest {

    PetRepository petRepository = new PetRepository();

    @Test
    public void save_shouldReturnPet_whenSuccessful() {
        Pet pet = petRepository.save(new Pet());
        assertNotNull(pet);
    }
}
