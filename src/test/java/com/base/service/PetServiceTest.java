package com.base.service;

import com.base.model.entities.Pet;
import com.base.repositories.PetRepository;
import com.base.services.PetService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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

    @Test
    public void update_shouldReturnPet_whenSuccessful() {
        when(petRepository.update(any(Pet.class), anyLong())).thenReturn(new Pet());
        Pet pet = petService.update(new Pet(), 1L);
        assertNotNull(pet);
    }

    @Test
    public void findByStatus_shouldReturnPetList_whenSuccessful() {
        List<String> statuses = List.of("available");
        when(petRepository.findByStatus(statuses)).thenReturn(List.of(new Pet()));
        List<Pet> pets = petService.findByStatus(statuses);
        assertNotNull(pets);
        assertNotNull(pets.get(0));
    }

    @Test
    public void findByTags_shouldReturnPetList_whenSuccessful() {
        List<String> tags = List.of("tag1");
        when(petRepository.findByTags(tags)).thenReturn(List.of(new Pet()));
        List<Pet> pets = petService.findByTags(tags);
        assertNotNull(pets);
        assertNotNull(pets.get(0));
    }
}
