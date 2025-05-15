package com.pragma.user360.configurations.beans;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfiguration {

    @Bean
    public OpenAPI springHomeOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("User API")
                        .description("API RESTful para la gestión integral de usuarios que proporciona funcionalidades CRUD (Crear, Leer, Actualizar, Eliminar) con validación de datos en tiempo real. Incluye administración de perfiles, control de acceso basado en roles, autenticación segura mediante JWT, y seguimiento de actividad de usuarios. Diseñada con arquitectura escalable para alto rendimiento y disponibilidad.")
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
                )
                .servers(List.of(
                    new Server()
                        .url("/")
                        .description("Servidor local")
                ));
    }
}
