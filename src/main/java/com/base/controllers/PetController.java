package com.base.controllers;

import com.base.model.dto.PetDto;
import com.base.model.entities.Pet;
import com.base.services.PetService;
import com.base.validations.ValidPetStatus;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Put;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.validation.Validated;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Produces;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

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
    @Validated
    public HttpResponse<PetDto> addPet(@Valid @Body PetDto petDto) {
        Pet pet = petService.save(modelMapper.map(petDto, Pet.class));
        return HttpResponse
                .status(HttpStatus.OK)
                .body(modelMapper.map(pet, PetDto.class));
    }

    @Put("/{id}")
    @Validated
    public HttpResponse<PetDto> updatePet(@Valid @Body PetDto petDto, @NotNull @PathVariable Long id) {
        Pet updated = petService.update(modelMapper.map(petDto, Pet.class), id);
        return HttpResponse
                .status(HttpStatus.OK)
                .body(modelMapper.map(updated, PetDto.class));
    }

    @Get("/findByStatus")
    public HttpResponse<List<PetDto>> findByStatus(@QueryValue("status") List<@NotBlank @ValidPetStatus String> statuses) {
        List<Pet> pets = petService.findByStatus(statuses);

        List<PetDto> found = pets.stream()
                .map(pet -> modelMapper.map(pet, PetDto.class))
                .collect(Collectors.toList());

        return HttpResponse
                .status(HttpStatus.OK)
                .body(found);
    }

    @Get("/findByTags")
    public HttpResponse<List<PetDto>> findByTags(@QueryValue("tags") List<@NotBlank String> tags) {
        List<Pet> pets = petService.findByTags(tags);

        List<PetDto> found = pets.stream()
                .map(pet -> modelMapper.map(pet, PetDto.class))
                .collect(Collectors.toList());

        return HttpResponse
                .status(HttpStatus.OK)
                .body(found);
    }

    @Get("/{id}")
    public HttpResponse<PetDto> findById(@NotNull @PathVariable Long id) {
        Pet pet = petService.findById(id);
        return HttpResponse
                .status(HttpStatus.OK)
                .body(modelMapper.map(pet, PetDto.class));
    }

}
