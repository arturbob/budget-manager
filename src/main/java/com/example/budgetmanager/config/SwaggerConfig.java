package com.example.budgetmanager.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RestController;

@OpenAPIDefinition(
        info = @Info(
                title = "Budget Manager API",
                version = "v0.0.3",
                description = "Budget Manager web application",
                license = @License(name = "Apache 2.0", url = "http://springdoc.org"),
                contact = @Contact(url = "https://gitlab.com/mozzzzart", name = "Patryk")
        )
)
public class SwaggerConfig {
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
