package com.base.integration;

import com.base.model.dto.PetDto;
import com.base.utils.PetUtils;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

@MicronautTest(rebuildContext = true)
public class ShelterIntegrationTest {

    @Inject
    @Client("/api/v1")
    HttpClient client;

    @Test
    void findInventory_shouldReturnInventory_WhenSuccessful() {
        var getRequest = HttpRequest.GET("/shelter/inventory");
        var response = client.toBlocking().exchange(getRequest, String.class);

        assertEquals(HttpStatus.OK, response.status());
        assertNotNull(response.body());
    }

    @Test
    void adoptPet_shouldReturnAdoptionDto_WhenSuccessful() {
        PetDto petDto = PetUtils.createValidPetDto();
        HttpRequest<PetDto> addPetRequest = HttpRequest.POST("/pets", petDto);
        HttpResponse<PetDto> postResponse = client.toBlocking().exchange(addPetRequest, PetDto.class);
        PetDto newPetDto = postResponse.body();
        assertEquals(postResponse.getStatus(), HttpStatus.OK);

        var adoptRequest = HttpRequest.POST(String.format("/shelter/adopt/%d", newPetDto.getId()), "");
        var response = client.toBlocking().exchange(adoptRequest, String.class);

        assertEquals(HttpStatus.OK, response.status());
        assertNotNull(response.body());
    }

    @ParameterizedTest
    @CsvSource({
            "-1, 400",
            ", 400",
            "999999, 404"
    })
    void adoptPet_shouldReturnError_WhenUnsuccessful(Long petId, int expectedStatus) {
        var adoptRequest = HttpRequest.POST(String.format("/shelter/adopt/%d", petId), "");
        try {
            client.toBlocking().exchange(adoptRequest, String.class);
            fail("Expected HttpClientResponseException, but no exception was thrown");
        } catch (HttpClientResponseException e) {
            assertEquals(expectedStatus, e.getResponse().status().getCode());
        }
    }
}
