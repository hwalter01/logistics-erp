package com.logistics.masterdataservice.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Master Data API",
                version = "v1",
                description = "API for managing logistics master data."
        ),
        tags = {
                @Tag(name = "Addresses", description = "Operations for managing addresses"),
                @Tag(name = "Customers", description = "Operations for managing customers"),
                @Tag(name = "Drivers", description = "Operations for managing drivers"),
                @Tag(name = "Locations", description = "Operations for managing locations"),
                @Tag(name = "Trailers", description = "Operations for managing trailers"),
                @Tag(name = "Vehicles", description = "Operations for managing vehicles")
        }
)
public class OpenApiConfig {
}