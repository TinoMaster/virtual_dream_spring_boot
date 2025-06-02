package com.tinomaster.virtualdream.virtualdream.configs;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@OpenAPIDefinition(
        info = @io.swagger.v3.oas.annotations.info.Info(
                title = "Control API",
                version = "1.0",
                license = @io.swagger.v3.oas.annotations.info.License(
                        name = "License name",
                        url = "https://some-url.com"
                ),
                description = "Control API Documentation",
                contact = @io.swagger.v3.oas.annotations.info.Contact(
                        name = "TinoMaster",
                        email = "tinomaster@gmail.com",
                        url = "https://github.com/TinoMaster"
                )
        ),
        servers = {
                @io.swagger.v3.oas.annotations.servers.Server(
                        url = "http://localhost:5000",
                        description = "Local Server"
                ),
                @io.swagger.v3.oas.annotations.servers.Server(
                        url = "https://virtual-dream.onrender.com",
                        description = "Render Server"
                )
        },
        security = {
                @io.swagger.v3.oas.annotations.security.SecurityRequirement(
                        name = "Bearer Authentication"
                )
        }
)
@SecurityScheme(
        name = "Bearer Authentication",
        description = "Connect to the API using bearer authentication with the token",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {
}
