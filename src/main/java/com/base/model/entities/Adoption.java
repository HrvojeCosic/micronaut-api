package com.base.model.entities;

import java.time.LocalDateTime;

public class Adoption {
    Long petId;
    LocalDateTime adoptionTime;

    public Adoption() {
    }

    public Adoption(Long petId, LocalDateTime adoptionTime) {
        this.petId = petId;
        this.adoptionTime = adoptionTime;
    }

    public Long getPetId() {
        return petId;
    }

    public void setPetId(Long petId) {
        this.petId = petId;
    }

    public LocalDateTime getAdoptionTime() {
        return adoptionTime;
    }

    public void setAdoptionTime(LocalDateTime adoptionTime) {
        this.adoptionTime = adoptionTime;
    }

}
