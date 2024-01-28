package com.base.controller;

import com.base.controllers.PetController;
import com.base.model.dto.PetDto;
import com.base.model.entities.Pet;
import com.base.services.PetService;
import com.base.utils.PetUtils;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.util.List;

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

    PetDto petDto = PetUtils.createValidPetDto();
    Pet pet = PetUtils.createValidPet();
    List<String> statuses = List.of("available");
    List<String> tags = List.of("tag1");

    @BeforeEach
    void setUp() {
        when(modelMapper.map(eq(petDto), eq(Pet.class))).thenReturn(pet);
        when(modelMapper.map(eq(pet), eq(PetDto.class))).thenReturn(new PetDto());
    }

    @Test
    void addPet_shouldReturnPetDto_WhenSuccessful() {
        when(petService.save(any(Pet.class))).thenReturn(pet);
        HttpResponse<PetDto> response = petController.addPet(petDto);

        assertEquals(HttpStatus.OK, response.status());
        assertNotNull(response.body());
        assertEquals(PetDto.class, response.body().getClass());
    }

    @Test
    void updatePet_shouldReturnPetDto_WhenSuccessful() {
        when(petService.update(any(Pet.class), eq(petDto.getId()))).thenReturn(pet);
        HttpResponse<PetDto> response = petController.updatePet(petDto, petDto.getId());

        assertEquals(HttpStatus.OK, response.status());
        assertNotNull(response.body());
        assertEquals(PetDto.class, response.body().getClass());
    }

    @Test
    void getPetByStatus_shouldReturnPetDto_WhenSuccessful() {
        when(petService.findByStatus(statuses)).thenReturn(List.of(pet));
        HttpResponse<List<PetDto>> response = petController.findByStatus(statuses);

        assertEquals(HttpStatus.OK, response.status());
        assertNotNull(response.body());
        assertEquals(PetDto.class, response.body().get(0).getClass());
    }

    @Test
    void getPetByStatus_shouldReturnEmptyList_WhenNoPetsFound() {
        when(petService.findByStatus(statuses)).thenReturn(List.of());
        HttpResponse<List<PetDto>> response = petController.findByStatus(statuses);

        assertEquals(HttpStatus.OK, response.status());
        assertNotNull(response.body());
        assertEquals(0, response.body().size());
    }

    @Test
    void getPetByTags_shouldReturnPetDto_WhenSuccessful() {
        when(petService.findByTags(tags)).thenReturn(List.of(pet));
        HttpResponse<List<PetDto>> response = petController.findByTags(tags);

        assertEquals(HttpStatus.OK, response.status());
        assertNotNull(response.body());
        assertEquals(PetDto.class, response.body().get(0).getClass());
    }

    @Test
    void getPetByTags_shouldReturnEmptyList_WhenNoPetsFound() {
        when(petService.findByTags(tags)).thenReturn(List.of());
        HttpResponse<List<PetDto>> response = petController.findByTags(tags);

        assertEquals(HttpStatus.OK, response.status());
        assertNotNull(response.body());
        assertEquals(0, response.body().size());
    }

    @Test
    void getPetById_shouldReturnPetDto_WhenSuccessful() {
        when(petService.findById(petDto.getId())).thenReturn(pet);
        HttpResponse<PetDto> response = petController.findById(petDto.getId());

        assertEquals(HttpStatus.OK, response.status());
        assertNotNull(response.body());
        assertEquals(PetDto.class, response.body().getClass());
    }

    @Test
    void updatePetWithForm_shouldReturnVoid_WhenSuccessful() {
        HttpResponse<Void> response = petController.updatePetWithForm(1L, "Rex", "available");
        assertEquals(HttpStatus.OK, response.status());
    }

    @Test
    void deletePet_shouldReturnVoid_WhenSuccessful() {
        HttpResponse<Void> response = petController.deleteById(petDto.getId());
        assertEquals(HttpStatus.OK, response.status());
    }

}
