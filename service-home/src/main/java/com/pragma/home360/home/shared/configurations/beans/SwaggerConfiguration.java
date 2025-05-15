package com.pragma.home360.home.shared.configurations.beans;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {

    @Bean
    public OpenAPI springHomeOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Home API")
                        .description("API para la gestión eficiente de inmuebles, proporcionando funcionalidades clave para administración y consulta.")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("Yeimar Lemus")
                                .email("yeimar112003@gmail.com")
                                .url("https://github.com/usyeimar")
                        )

                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")
                        )
                );
    }


}
