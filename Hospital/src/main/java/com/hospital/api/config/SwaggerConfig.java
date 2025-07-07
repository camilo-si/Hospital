package com.hospital.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenApi(){
        return new OpenAPI()
                .info(new Info()
                        .title("API 2025, Gestión Hospitalaria - Hospital DUOC")
                        .version("1.0")
                        .description("Documentación de la API para nuevo sistema")
                        .description("Camilo")
                );
    }
}
