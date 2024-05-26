package com.w2m.naves.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {

        Contact contact = new Contact()
                .name("w2m")
                .email("w2m@w2m.com")
                .url("https://www.w2m.travel/");

        Info info = new Info()
                .title("Naves API")
                .description("Naves Backend API")
                .version("v1.0")
                .contact(contact);

        Components components = new Components();

        return new OpenAPI()
                .info(info)
                .addServersItem(new Server().url("/"))
                .components(components);
    }
}
