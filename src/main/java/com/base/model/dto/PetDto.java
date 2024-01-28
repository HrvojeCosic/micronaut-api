package com.base.model.dto;

import com.base.model.enums.PetCategory;
import com.base.model.entities.Tag;
import com.base.model.enums.PetStatus;
import com.base.validations.ValidPetStatus;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Introspected
@Serdeable
public class PetDto {

    private Long id;

    @NotBlank
    private String name;

    @NotNull
    private PetCategory category;

    @NotNull
    private List<Tag> tags;

    @NotBlank
    @ValidPetStatus
    private String status;

    public PetDto() {
    }

    public PetDto(Long id, String name, PetCategory category, List<Tag> tags, String status) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.tags = tags;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PetCategory getCategory() {
        return category;
    }

    public void setCategory(PetCategory category) {
        this.category = category;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
