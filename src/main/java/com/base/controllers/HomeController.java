package com.base.controllers;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.uri.UriBuilder;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.annotation.security.PermitAll;

import java.net.URI;
import java.security.Principal;

@Controller
@Secured(SecurityRule.IS_AUTHENTICATED)
class HomeController {

    private final static URI SWAGGER_UI = UriBuilder.of("/swagger-ui").path("index.html").build();

    @Get
    @Hidden
    HttpResponse<?> home() {
        return HttpResponse.seeOther(SWAGGER_UI);
    }

    @Produces(MediaType.TEXT_PLAIN)
    @Post
    String index(Principal principal) {
        return principal.getName();
    }
}