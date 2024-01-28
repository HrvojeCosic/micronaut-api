package com.base.model.dto;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;

import java.time.LocalDateTime;

@Introspected
@Serdeable
public class AdoptionDto {

    private Long petId;
    LocalDateTime adoptionTime;

    public AdoptionDto() {
    }

    public AdoptionDto(Long petId, LocalDateTime adoptionTime) {
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
