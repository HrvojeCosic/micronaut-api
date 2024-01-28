package com.base.services;

import com.base.model.entities.Adoption;
import com.base.repositories.PetRepository;
import com.base.repositories.ShelterRepository;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class ShelterService {

    private final ShelterRepository shelterRepository;
    private final PetRepository petRepository;

    @Inject
    public ShelterService(ShelterRepository shelterRepository, PetRepository petRepository) {
        this.shelterRepository = shelterRepository;
        this.petRepository = petRepository;
    }

    public Adoption adoptPet(Long petId) {
        petRepository.findById(petId); // id validation happens inside this method (TODO: it'd be better to validate here)
        return shelterRepository.adoptPet(petId);
    }
}
