package com.base.integration;

import com.base.model.dto.PetDto;
import com.base.model.entities.PetCategory;
import com.base.model.entities.Tag;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@MicronautTest
public class PetIntegrationTest {

    @Inject
    @Client("/api/v1")
    HttpClient client;

    @Test
    public void post_shouldCreateAndReturnValidPet_whenSuccessful() {
        PetDto petDto = new PetDto(2L, "Name", PetCategory.dog, List.of(new Tag()), "available");
        HttpRequest<PetDto> request = HttpRequest.POST("/pets", petDto);
        HttpResponse<PetDto> response = client.toBlocking().exchange(request, PetDto.class);
        assertEquals(response.getStatus(), HttpStatus.OK);
        assertEquals(response.body().getId(), petDto.getId());
        assertEquals(response.body().getName(), petDto.getName());
        assertEquals(response.body().getCategory(), petDto.getCategory());
        assertEquals(response.body().getStatus(), petDto.getStatus());
    }

    @Test
    public void post_shouldReturnBadRequest_whenBadInput() {
        PetDto petDto = new PetDto();
        HttpRequest<PetDto> request = HttpRequest.POST("/pets", petDto);

        try {
            client.toBlocking().exchange(request, PetDto.class);
            fail("Expected HttpClientResponseException, but no exception was thrown");
        } catch (HttpClientResponseException e) {
            assertEquals(HttpStatus.BAD_REQUEST, e.getResponse().status());
        }
    }

}
