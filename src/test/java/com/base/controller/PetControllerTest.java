package com.base.controller;

import com.base.controllers.PetController;
import com.base.model.dto.PetDto;
import com.base.model.entities.Pet;
import com.base.services.PetService;
import com.base.utils.PetUtils;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PetControllerTest {

    ModelMapper modelMapper = mock(ModelMapper.class);
    PetService petService = mock(PetService.class);
    PetController petController = new PetController(petService, modelMapper);

    @Test
    void addPet_shouldReturnPetDto_WhenSuccessful() {
        PetDto petDto = new PetDto();
        Pet pet = new Pet();

        when(modelMapper.map(eq(petDto), eq(Pet.class))).thenReturn(pet);
        when(petService.save(any(Pet.class))).thenReturn(pet);
        when(modelMapper.map(eq(pet), eq(PetDto.class))).thenReturn(new PetDto());

        HttpResponse<PetDto> response = petController.addPet(petDto);

        assertEquals(HttpStatus.OK, response.status());
        assertNotNull(response.body());
        assertEquals(PetDto.class, response.body().getClass());
    }

    @Test
    void updatePet_shouldReturnPetDto_WhenSuccessful() {
        PetDto petDto = PetUtils.createValidPetDto();
        Pet pet = PetUtils.createValidPet();

        when(modelMapper.map(eq(petDto), eq(Pet.class))).thenReturn(pet);
        when(petService.update(any(Pet.class), eq(petDto.getId()))).thenReturn(pet);
        when(modelMapper.map(eq(pet), eq(PetDto.class))).thenReturn(new PetDto());

        HttpResponse<PetDto> response = petController.updatePet(petDto, petDto.getId());

        assertEquals(HttpStatus.OK, response.status());
        assertNotNull(response.body());
        assertEquals(PetDto.class, response.body().getClass());
    }
}
