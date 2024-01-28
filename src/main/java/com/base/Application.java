package com.base;

import io.micronaut.runtime.Micronaut;

import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.info.*;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                title = "petshelter"
        ), servers = @Server(url = "https://localhost:8080/")
)
public class Application {

    public static void main(String[] args) {
        Micronaut.run(Application.class, args);
    }
}