package com.example.budgetmanager.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RestController;

@SecurityScheme(
        name = "api",
        scheme = "apikey",
        type = SecuritySchemeType.APIKEY,
        in = SecuritySchemeIn.HEADER
)
@OpenAPIDefinition
public class OpenApiDefinition {
    @Bean
    public OpenAPI openAPI(){
        return new OpenAPI()
                .info(new Info().title("Budget Manager API")
                        .description("Budget Manager web application")
                        .version("v0.0.2")
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://springdoc.org")))
                .externalDocs(new ExternalDocumentation()
                        .description("Budget Manager Documentation")
                        .url("https://gitlab.com/mozzzzart/budget-manager"));
    }
    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("rest")
                .pathsToMatch("/**")
                .addOpenApiMethodFilter(method -> {
                    Class<?> declaringClass = method.getDeclaringClass();
                    return declaringClass.isAnnotationPresent(RestController.class);
                })
                .build();
    }
}
