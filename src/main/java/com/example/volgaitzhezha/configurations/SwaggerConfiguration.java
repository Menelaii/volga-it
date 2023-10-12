package com.example.volgaitzhezha.configurations;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "Simbir.GO API",
                version = "1.0",
                description = "API сгруппирован в user-api и admin-api выбирать через выпадающий список сверху"
        ),
        servers = {
                @Server(
                        description = "Локальная машина",
                        url = "http://localhost:8080"
                )
        },
        security = {
                @SecurityRequirement(
                        name = "jwtBearer"
                )
        }
)
@SecurityScheme(
        name = "jwtBearer",
        description = "Аутентификация по jwt токену",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER,
        paramName = "Authorization"
)
@Configuration
public class SwaggerConfiguration {
        @Bean
        GroupedOpenApi adminApi() {
                return GroupedOpenApi.builder()
                        .group("admin-api")
                        .pathsToMatch("/api/Admin/**")
                        .build();
        }

        @Bean
        GroupedOpenApi userApi() {
                return GroupedOpenApi.builder()
                        .group("user-api")
                        .pathsToMatch("/api/**")
                        .pathsToExclude("/api/Admin/**")
                        .build();
        }
}
