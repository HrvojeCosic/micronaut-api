package com.base.services;

import com.base.model.entities.Pet;
import com.base.repositories.PetRepository;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class PetService {

    private final PetRepository petRepository;

    @Inject
    public PetService(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    public Pet save(Pet pet) {
        return petRepository.save(pet);
    }
}
