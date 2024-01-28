package com.base.mapper;

import com.base.model.dto.AdoptionDto;
import com.base.model.entities.Adoption;
import jakarta.inject.Singleton;

@Singleton
public class AdoptionMapper implements Mapper<Adoption, AdoptionDto> {

    @Override
    public AdoptionDto mapTo(Adoption adoption) {
        if (adoption == null) {
            return null;
        }
        AdoptionDto adoptionDto = new AdoptionDto();
        adoptionDto.setPetId(adoption.getPetId());
        adoptionDto.setAdoptionTime(adoption.getAdoptionTime());

        return adoptionDto;
    }

    @Override
    public Adoption mapFrom(AdoptionDto adoptionDto) {
        if (adoptionDto == null) {
            return null;
        }
        Adoption adoption = new Adoption();
        adoption.setPetId(adoptionDto.getPetId());
        adoption.setAdoptionTime(adoptionDto.getAdoptionTime());

        return adoption;
    }
}
