package dev.ilkerk.digitalwallet.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
public class SpringDocConfig {
    @Value("${com.api.host}")
    private String apiHost;

    @Value("${com.name}")
    private String name;

    @Value("${com.email}")
    private String email;

    @Value("${com.api.doc.title}")
    private String docTitle;

    @Value("${com.api.doc.description}")
    private String docDescription;

    @Value("${application.version}")
    private String applicationVersion;

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("basicAuth", new SecurityScheme()
                                .name("basic")
                                .scheme("basic")
                                .in(SecurityScheme.In.HEADER)
                                .type(SecurityScheme.Type.HTTP)
                                .description("Username and password required")))
                .security(Collections.singletonList(new SecurityRequirement().addList("basicAuth")))
                .servers(Collections.singletonList(new Server().url(apiHost)))
                .info(new Info()
                        .title(docTitle)
                        .description(docDescription)
                        .version(applicationVersion)
                        .contact(new Contact()
                                .name(name)
                                .url(apiHost)
                                .email(email)));
    }
}
