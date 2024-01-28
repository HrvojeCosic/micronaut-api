package com.base.services;

import com.base.model.entities.Pet;
import com.base.repositories.PetRepository;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.util.List;

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

    public Pet update(Pet pet, Long id) {
        return petRepository.update(pet, id);
    }

    public List<Pet> findByStatus(List<String> statuses) {
        return petRepository.findByStatus(statuses);
    }

    public List<Pet> findByTags(List<String> tags) {
        return petRepository.findByTags(tags);
    }

    public Pet findById(Long id) {
        return petRepository.findById(id);
    }
}
