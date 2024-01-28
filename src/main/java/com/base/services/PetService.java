package com.base.services;

import com.base.model.entities.Pet;
import com.base.model.enums.PetStatus;
import com.base.repositories.PetRepository;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    public void updateWithForm(Long id, String name, String status) {
        petRepository.updateWithForm(id, name, status);
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

    public void deleteById(Long id) {
        petRepository.deleteById(id);
    }

    public Map<String, Integer> getInventories() {
        List<Pet> pets = petRepository.findAll();

        return pets
                .stream()
                .collect(Collectors.groupingBy(Pet::getStatus, Collectors.summingInt(pet -> 1)));
    }}
