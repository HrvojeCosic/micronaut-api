package com.base.integration;

import com.base.model.dto.PetDto;
import com.base.repositories.PetRepository;
import com.base.utils.PetUtils;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpMethod;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@MicronautTest(rebuildContext = true)
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

        petDto = response.body();
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
    public void put_shouldReturnMethodNotAllowed_whenInvalidIdProvided() {
        PetDto petDto = PetUtils.createValidPetDto();
        Long invalidId = -1L;
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

        Long nonExistentId = 9999L;
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

    @Test
    public void getByStatus_shouldReturnValidPet_whenSuccessful() {
        PetDto petDto = PetUtils.createValidPetDto();
        HttpRequest<PetDto> postRequest = HttpRequest.POST("/pets", petDto);
        HttpResponse<PetDto> postResponse = client.toBlocking().exchange(postRequest, PetDto.class);

        String statusQueryParam = petDto.getStatus();
        HttpRequest<?> getRequest = HttpRequest
                .GET("/pets/findByStatus")
                .uri(uriBuilder -> uriBuilder.queryParam("status", statusQueryParam).build());
        HttpResponse<List<PetDto>> getResponse = client.toBlocking().exchange(getRequest, Argument.listOf(PetDto.class));

        assertEquals(postResponse.getStatus(), HttpStatus.OK);
        assertEquals(getResponse.getStatus(), HttpStatus.OK);
        assertEquals(getResponse.body().get(0).getName(), petDto.getName());
        assertEquals(getResponse.body().get(0).getCategory(), petDto.getCategory());
        assertEquals(getResponse.body().get(0).getStatus(), petDto.getStatus());
    }

    @Test
    public void getByStatus_shouldReturnValidPets_whenMultipleValidStatusValuesProvided() {
        PetDto petDto1 = PetUtils.createValidPetDto();
        PetDto petDto2 = PetUtils.createValidPetDto();
        petDto2.setStatus("adopted");
        client.toBlocking().exchange(HttpRequest.POST("/pets", petDto1), PetDto.class);
        client.toBlocking().exchange(HttpRequest.POST("/pets", petDto2), PetDto.class);

        String statusQueryParam = String.format("%s,%s", petDto1.getStatus(), petDto2.getStatus());
        HttpRequest<?> getRequest = HttpRequest
                .GET("/pets/findByStatus")
                .uri(uriBuilder -> uriBuilder.queryParam("status", statusQueryParam).build());
        HttpResponse<List<PetDto>> getResponse = client.toBlocking().exchange(getRequest, Argument.listOf(PetDto.class));

        assertEquals(getResponse.getStatus(), HttpStatus.OK);
        assertEquals(getResponse.body().size(), 2);
        assertEquals(getResponse.body().get(0).getName(), petDto1.getName());
        assertEquals(getResponse.body().get(0).getCategory(), petDto1.getCategory());
        assertEquals(getResponse.body().get(0).getStatus(), petDto1.getStatus());
        assertEquals(getResponse.body().get(1).getName(), petDto2.getName());
        assertEquals(getResponse.body().get(1).getCategory(), petDto2.getCategory());
        assertEquals(getResponse.body().get(1).getStatus(), petDto2.getStatus());
    }

    @Test
    public void getByStatus_shouldReturnBadRequest_whenInvalidStatusValue() {
        String statusQueryParam = "invalid";
        HttpRequest<?> getRequest = HttpRequest
                .GET("/pets/findByStatus")
                .uri(uriBuilder -> uriBuilder.queryParam("status", statusQueryParam).build());

        try {
            client.toBlocking().exchange(getRequest, Argument.listOf(PetDto.class));
            fail("Expected HttpClientResponseException, but no exception was thrown");
        } catch (HttpClientResponseException e) {
            assertEquals(HttpStatus.BAD_REQUEST, e.getResponse().status());
        }
    }

    @Test
    public void getByTags_shouldReturnValidPets_WhenSuccessful() {
        PetDto petDto = PetUtils.createValidPetDto();
        HttpRequest<PetDto> postRequest = HttpRequest.POST("/pets", petDto);
        HttpResponse<PetDto> postResponse = client.toBlocking().exchange(postRequest, PetDto.class);

        String tagsQueryParam = petDto.getTags().get(0).getName();
        HttpRequest<?> getRequest = HttpRequest
                .GET("/pets/findByTags")
                .uri(uriBuilder -> uriBuilder.queryParam("tags", tagsQueryParam).build());
        HttpResponse<List<PetDto>> getResponse = client.toBlocking().exchange(getRequest, Argument.listOf(PetDto.class));

        assertEquals(postResponse.getStatus(), HttpStatus.OK);
        assertEquals(getResponse.getStatus(), HttpStatus.OK);
        assertEquals(getResponse.body().get(0).getName(), petDto.getName());
        assertEquals(getResponse.body().get(0).getCategory(), petDto.getCategory());
        assertEquals(getResponse.body().get(0).getStatus(), petDto.getStatus());
        assertEquals(getResponse.body().get(0).getTags().get(0).getName(), petDto.getTags().get(0).getName());
    }

    @Test
    public void getByTags_shouldReturnBadRequest_whenInvalidStatusValue() {
        HttpRequest<?> getRequest = HttpRequest
                .GET("/pets/findByTags")
                .uri(uriBuilder -> uriBuilder.queryParam("status", "").build());

        try {
            client.toBlocking().exchange(getRequest, Argument.listOf(PetDto.class));
            fail("Expected HttpClientResponseException, but no exception was thrown");
        } catch (HttpClientResponseException e) {
            assertEquals(HttpStatus.BAD_REQUEST, e.getResponse().status());
        }
    }

    @Test
    public void getById_shouldReturnValidPet_whenSuccessful() {
        PetDto petDto = PetUtils.createValidPetDto();
        HttpRequest<PetDto> postRequest = HttpRequest.POST("/pets", petDto);
        HttpResponse<PetDto> postResponse = client.toBlocking().exchange(postRequest, PetDto.class);
        PetDto responseDto = postResponse.body();
        assertEquals(postResponse.getStatus(), HttpStatus.OK);

        HttpRequest<PetDto> getRequest = HttpRequest.GET(String.format("/pets/%d", responseDto.getId()));
        HttpResponse<PetDto> getResponse = client.toBlocking().exchange(getRequest, PetDto.class);

        assertEquals(getResponse.getStatus(), HttpStatus.OK);
        assertEquals(getResponse.body().getName(), petDto.getName());
        assertEquals(getResponse.body().getCategory(), petDto.getCategory());
        assertEquals(getResponse.body().getStatus(), petDto.getStatus());
    }

    @ParameterizedTest
    @CsvSource({
            "-1, 400",
            "999999, 404",
    })
    public void getById_shouldReturnErrorCode_whenBadIdProvided(Long invalidId, int expectedStatusCode) {
        HttpRequest<PetDto> getRequest = HttpRequest.GET(String.format("/pets/%d", invalidId));

        try {
            client.toBlocking().exchange(getRequest, PetDto.class);
            fail("Expected HttpClientResponseException, but no exception was thrown");
        } catch (HttpClientResponseException e) {
            assertEquals(expectedStatusCode, e.getResponse().status().getCode());
        }
    }

    @Test
    public void updateWithForm_shouldUpdateAndReturnOk_whenSuccessful() {
        PetDto petDto = PetUtils.createValidPetDto();
        String updatedName = "updatedName";
        String updatedStatus = "adopted";

        HttpRequest<PetDto> postRequest = HttpRequest.POST("/pets", petDto);
        HttpResponse<PetDto> postResponse = client.toBlocking().exchange(postRequest, PetDto.class);
        PetDto newPetDto = postResponse.body();
        assertEquals(postResponse.getStatus(), HttpStatus.OK);

        var updateRequest = HttpRequest
                .create(HttpMethod.POST, String.format("/pets/%d", newPetDto.getId()))
                .uri(uriBuilder -> uriBuilder
                        .queryParam("name", updatedName)
                        .queryParam("status", updatedStatus)
                        .build());

        var updateResponse = client.toBlocking().exchange(updateRequest);
        assertEquals(updateResponse.getStatus(), HttpStatus.OK);

        var getRequest = HttpRequest.GET(String.format("/pets/%d", newPetDto.getId()));
        HttpResponse<PetDto> getResponse = client.toBlocking().exchange(getRequest, PetDto.class);
        assertEquals(getResponse.getStatus(), HttpStatus.OK);
        assertEquals(getResponse.body().getName(), updatedName);
        assertEquals(getResponse.body().getStatus(), updatedStatus);
    }

    @ParameterizedTest
    @CsvSource({
            "-1, updatedName, adopted, 400",
            ", updatedName, adopted, 400",
            "999999, updatedName, adopted, 404",
    })
    public void updateWithForm_shouldReturnErrorCode_whenBadIdProvided(
            Long invalidId,
            String updatedName,
            String updatedStatus,
            int expectedStatusCode
    ) {
        var updateRequest = HttpRequest
                .create(HttpMethod.POST, String.format("/pets/%s", invalidId))
                .uri(uriBuilder -> uriBuilder
                        .queryParam("name", updatedName)
                        .queryParam("status", updatedStatus)
                        .build());

        try {
            client.toBlocking().exchange(updateRequest);
            fail("Expected HttpClientResponseException, but no exception was thrown");
        } catch (HttpClientResponseException e) {
            assertEquals(expectedStatusCode, e.getResponse().status().getCode());
        }
    }

    @Test
    public void deleteById_shouldDeleteAndReturnOk_whenSuccessful() {
        PetDto petDto = PetUtils.createValidPetDto();
        HttpRequest<PetDto> postRequest = HttpRequest.POST("/pets", petDto);
        HttpResponse<PetDto> postResponse = client.toBlocking().exchange(postRequest, PetDto.class);
        PetDto newPetDto = postResponse.body();
        assertEquals(postResponse.getStatus(), HttpStatus.OK);

        var deleteRequest = HttpRequest.DELETE(String.format("/pets/%d", newPetDto.getId()));
        var deleteResponse = client.toBlocking().exchange(deleteRequest);
        assertEquals(deleteResponse.getStatus(), HttpStatus.OK);

        var getRequest = HttpRequest.GET(String.format("/pets/%d", newPetDto.getId()));
        try {
            client.toBlocking().exchange(getRequest, PetDto.class);
            fail("Expected HttpClientResponseException, but no exception was thrown");
        } catch (HttpClientResponseException e) {
            assertEquals(HttpStatus.NOT_FOUND, e.getResponse().status());
        }
    }

    @ParameterizedTest
    @CsvSource({
            "-1, 400",
            ", 400",
            "999999, 404"
    })
    public void deleteById_shouldReturnErrorCode_whenBadIdProvided(Long invalidId, int expectedStatusCode) {
        var deleteRequest = HttpRequest.DELETE(String.format("/pets/%d", invalidId));
        try {
            client.toBlocking().exchange(deleteRequest);
            fail("Expected HttpClientResponseException, but no exception was thrown");
        } catch (HttpClientResponseException e) {
            assertEquals(expectedStatusCode, e.getResponse().status().getCode());
        }
    }
}
