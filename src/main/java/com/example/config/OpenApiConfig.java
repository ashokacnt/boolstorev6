package com.example.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.*;

@Configuration
public class OpenApiConfig {
    @Bean
    OpenAPI openAPI() {
        return new OpenAPI().info(new Info().title("Bookstore V5 API").version("1.0.0").description("Online bookstore using Spring Boot, Basic Authentication and modular architecture."));
    }
}
