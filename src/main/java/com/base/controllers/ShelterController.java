package com.base.controllers;

import com.base.mapper.AdoptionMapper;
import com.base.model.dto.AdoptionDto;
import com.base.model.entities.Adoption;
import com.base.services.PetService;
import com.base.services.ShelterService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Produces;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotNull;

import java.util.Map;

@Controller("/api/v1/shelter")
@Produces(MediaType.APPLICATION_JSON)
public class ShelterController {

    private final PetService petService;
    private final ShelterService shelterService;
    private final AdoptionMapper adoptionMapper;

    @Inject
    public ShelterController(PetService petService, ShelterService shelterService, AdoptionMapper adoptionMapper) {
        this.petService = petService;
        this.shelterService = shelterService;
        this.adoptionMapper = adoptionMapper;
    }

    @Get("/inventory")
    public HttpResponse<Map<String, Integer>> findInventory() {
        return HttpResponse
                .status(HttpStatus.OK)
                .body(petService.getInventories());
    }

    @Post("/adopt/{petId}")
    public HttpResponse<AdoptionDto> adoptPet(@NotNull @PathVariable Long petId) {
        Adoption newAdoption = shelterService.adoptPet(petId);
        AdoptionDto adoptionDto = adoptionMapper.mapTo(newAdoption);

        return HttpResponse
                .status(HttpStatus.OK)
                .body(adoptionDto);
    }
}
