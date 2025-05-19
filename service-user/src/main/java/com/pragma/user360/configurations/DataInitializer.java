package com.pragma.user360.configurations;


import com.pragma.user360.domain.model.UserModel;
import com.pragma.user360.domain.ports.in.UserServicePort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;
import java.util.Objects;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final UserServicePort userServicePort;

    @Value("${app.default-user.email:admin@example.com}")
    private String defaultUserEmail;

    @Value("${app.default-user.password:DefaultPassword123}")
    private String defaultUserPassword;

    @Value("${app.default-user.firstName:Admin}")
    private String defaultUserFirstName;

    @Value("${app.default-user.lastName:User}")
    private String defaultUserLastName;

    @Value("${app.default-user.documentId:00000000}")
    private String defaultUserDocumentId;

    @Value("${app.default-user.phoneNumber:+1000000000}")
    private String defaultUserPhoneNumber;

    public DataInitializer(UserServicePort userServicePort) {
        this.userServicePort = Objects.requireNonNull(userServicePort);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Verificando si el usuario por defecto '{}' existe...", defaultUserEmail);

        if (!userServicePort.existsByEmail(defaultUserEmail)) {
            log.info("Usuario por defecto '{}' no encontrado. Creando...", defaultUserEmail);

            try {
                UserModel defaultUser = new UserModel();
                defaultUser.setFirstName(defaultUserFirstName);
                defaultUser.setLastName(defaultUserLastName);
                defaultUser.setDocumentId(defaultUserDocumentId);
                defaultUser.setPhoneNumber(defaultUserPhoneNumber);
                defaultUser.setBirthDate(LocalDate.now().minusYears(25));
                defaultUser.setEmail(defaultUserEmail);
                defaultUser.setPassword(defaultUserPassword);
                defaultUser.setActive(true);

                UserModel createdUser = userServicePort.registerUser(defaultUser);

                log.info("Usuario por defecto '{}' creado con ID: {}", createdUser.getEmail(), createdUser.getId());

            } catch (Exception e) {
                log.error("Error al crear el usuario por defecto '{}': {}", defaultUserEmail, e.getMessage(), e);
            }
        } else {
            log.info("El usuario por defecto '{}' ya existe. No se requiere acción.", defaultUserEmail);
        }
    }
}
