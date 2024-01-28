package com.base.repositories;

import com.base.model.entities.Adoption;
import jakarta.inject.Singleton;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class ShelterRepository {

    List<Adoption> adoptions = new ArrayList<>();

    public Adoption adoptPet(Long petId) {
        Adoption adoption = new Adoption(petId, LocalDateTime.now());
        adoptions.add(adoption);
        return adoption;
    }
}
