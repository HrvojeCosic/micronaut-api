package com.base.service;

import com.base.model.entities.Pet;
import com.base.repositories.PetRepository;
import com.base.services.PetService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PetServiceTest {

    PetRepository petRepository = mock(PetRepository.class);
    PetService petService = new PetService(petRepository);

    @Test
    public void save_shouldReturnPet_whenSuccessful() {
        when(petRepository.save(any(Pet.class))).thenReturn(new Pet());
        Pet pet = petService.save(new Pet());
        assertNotNull(pet);
    }
}
