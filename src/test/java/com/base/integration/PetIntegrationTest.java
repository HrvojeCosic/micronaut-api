package com.base.integration;

import com.base.exceptions.ResourceNotFoundException;
import com.base.model.dto.PetDto;
import com.base.model.entities.Pet;
import com.base.model.entities.PetCategory;
import com.base.model.entities.Tag;
import com.base.repositories.PetRepository;
import com.base.services.PetService;
import com.base.utils.PetUtils;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.annotation.Resource;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@MicronautTest
public class PetIntegrationTest {

    @Inject
    @Client("/api/v1")
    HttpClient client;

    @Test
    public void post_shouldCreateAndReturnValidPet_whenSuccessful() {
        PetDto petDto = PetUtils.createValidPetDto();
        HttpRequest<PetDto> request = HttpRequest.POST("/pets", petDto);
        HttpResponse<PetDto> response = client.toBlocking().exchange(request, PetDto.class);
        assertEquals(response.getStatus(), HttpStatus.OK);
        assertEquals(response.body().getName(), petDto.getName());
        assertEquals(response.body().getCategory(), petDto.getCategory());
        assertEquals(response.body().getStatus(), petDto.getStatus());
    }

    @Test
    public void post_shouldReturnBadRequest_whenBadInput() {
        PetDto petDto = PetUtils.createInvalidPetDto();
        HttpRequest<PetDto> request = HttpRequest.POST("/pets", petDto);

        try {
            client.toBlocking().exchange(request, PetDto.class);
            fail("Expected HttpClientResponseException, but no exception was thrown");
        } catch (HttpClientResponseException e) {
            assertEquals(HttpStatus.BAD_REQUEST, e.getResponse().status());
        }
    }

    @Test
    public void put_shouldUpdateAndReturnValidPet_whenSuccessful() {
        PetDto petDto = PetUtils.createValidPetDto();
        HttpRequest<PetDto> request = HttpRequest.POST("/pets", petDto);
        HttpResponse<PetDto> response = client.toBlocking().exchange(request, PetDto.class);
        assertEquals(response.getStatus(), HttpStatus.OK);

        petDto.setName("New Name");
        request = HttpRequest.PUT(String.format("/pets/%d", petDto.getId()), petDto);
        response = client.toBlocking().exchange(request, PetDto.class);
        assertEquals(response.getStatus(), HttpStatus.OK);
        assertEquals(response.body().getId(), petDto.getId());
        assertEquals(response.body().getName(), petDto.getName());
        assertEquals(response.body().getCategory(), petDto.getCategory());
        assertEquals(response.body().getStatus(), petDto.getStatus());
    }

    @Test
    public void put_shouldReturnMethodNotAllowed_whenBadIdProvided() {
        PetDto petDto = PetUtils.createValidPetDto();
        Long invalidId = 999999L;
        HttpRequest<PetDto> request = HttpRequest.PUT(String.format("/pets/%d", invalidId), petDto);

        try {
            client.toBlocking().exchange(request, PetDto.class);
            fail("Expected ResourceNotFoundException, but no exception was thrown");
        } catch (HttpClientResponseException e) {
            assertEquals(e.getStatus(), HttpStatus.BAD_REQUEST);
        }
    }

    @Test
    public void put_shouldReturnNotFound_whenPetOfGivenIdNotFound() {
        PetDto petDto = PetUtils.createValidPetDto();
        HttpRequest<PetDto> request = HttpRequest.POST("/pets", petDto);
        HttpResponse<PetDto> response = client.toBlocking().exchange(request, PetDto.class);
        assertEquals(response.getStatus(), HttpStatus.OK);

        Long nonExistentId = -1L;
        petDto.setName("New Name");
        request = HttpRequest.PUT(String.format("/pets/%d", nonExistentId), petDto);

        try {
            client.toBlocking().exchange(request, PetDto.class);
            fail("Expected InvalidResourceIdException but no exception was thrown");
        } catch (HttpClientResponseException e) {
            assertEquals(HttpStatus.NOT_FOUND, e.getResponse().status());
        }
    }

    @Test
    public void put_shouldReturnBadRequest_whenValidationException() {
        PetDto petDto = PetUtils.createValidPetDto();
        HttpRequest<PetDto> request = HttpRequest.POST("/pets", petDto);
        HttpResponse<PetDto> response = client.toBlocking().exchange(request, PetDto.class);
        assertEquals(response.getStatus(), HttpStatus.OK);

        petDto.setName(null); // invalid field
        request = HttpRequest.PUT(String.format("/pets/%d", petDto.getId()), petDto);

        try {
            client.toBlocking().exchange(request, PetDto.class);
            fail("Expected HttpClientResponseException, but no exception was thrown");
        } catch (HttpClientResponseException e) {
            assertEquals(HttpStatus.BAD_REQUEST, e.getResponse().status());
        }
    }

}
