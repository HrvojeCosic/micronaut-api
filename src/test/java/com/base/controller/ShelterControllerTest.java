package com.base.controller;

import com.base.controllers.ShelterController;
import com.base.mapper.AdoptionMapper;
import com.base.model.entities.Adoption;
import com.base.services.PetService;
import com.base.services.ShelterService;
import io.micronaut.http.HttpStatus;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ShelterControllerTest {

    AdoptionMapper adoptionMapper = mock(AdoptionMapper.class);
    PetService petService = mock(PetService.class);
    ShelterService shelterService = mock(ShelterService.class);
    ShelterController shelterController = new ShelterController(petService, shelterService, adoptionMapper);

    @Test
    void findInventory_shouldReturnInventory_WhenSuccessful() {
        Map<String, Integer> mockInventory = Map.of("available", 3);
        when(petService.getInventories()).thenReturn(mockInventory);
        var response = shelterController.findInventory();

        assertEquals(HttpStatus.OK, response.status());
        assertEquals(mockInventory, response.body());
    }

    @Test
    void adoptPet_shouldReturnAdoption_WhenSuccessful() {
        Adoption mockAdoption = new Adoption(1L, null);
        when(shelterService.adoptPet(1L)).thenReturn(mockAdoption);
        when(adoptionMapper.mapTo(mockAdoption)).thenReturn(new com.base.model.dto.AdoptionDto(1L, LocalDateTime.now()));

        var response = shelterController.adoptPet(mockAdoption.getPetId());

        assertEquals(HttpStatus.OK, response.status());
        assertEquals(mockAdoption.getPetId(), response.body().getPetId());
        assertEquals(mockAdoption.getAdoptionTime(), null);
    }
}
