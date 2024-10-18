package io.lynx.oebs.configs;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        // Define the security scheme (JWT in this case)
        SecurityScheme securityScheme = new SecurityScheme()
                .name("Authorization")
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT");

        // Add the security requirement to require JWT authentication for protected endpoints
        SecurityRequirement securityRequirement = new SecurityRequirement().addList("Authorization");
        Contact contact = new Contact();
        contact.setEmail("niomwungeri.fabrice@gmail.com");
        contact.setUrl("https://github.com/niomwungeri-fabrice");
        contact.name("Fabrice NIYOMWUNGERI");


        return new OpenAPI()
                .components(new Components().addSecuritySchemes("Authorization", securityScheme))
                .security(List.of(securityRequirement))
                .info(new Info().title("OEBS API Documentation")
                        .description("API documentation with JWT authentication")
                        .contact(contact)

                        .version("1.0"));
    }
}

