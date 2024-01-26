package com.base.controllers;

import com.base.model.dto.PetDto;
import com.base.model.entities.Pet;
import com.base.services.PetService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Produces;
import jakarta.inject.Inject;
import org.modelmapper.ModelMapper;

@Controller("/api/v1/pets")
@Produces(MediaType.APPLICATION_JSON)
public class PetController {

    private final PetService petService;
    private final ModelMapper modelMapper;

    @Inject
    public PetController(PetService petService, ModelMapper modelMapper) {
        this.petService = petService;
        this.modelMapper = modelMapper;
    }

    @Post
    public HttpResponse<PetDto> addPet(@Body PetDto petDto) {
        Pet pet = petService.save(modelMapper.map(petDto, Pet.class));
        return HttpResponse
                .status(HttpStatus.CREATED)
                .body(modelMapper.map(pet, PetDto.class));
    }
}
