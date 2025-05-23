package com.pragma.user360.infrastructure.adapters.persistence.seed;

import com.pragma.user360.domain.model.UserModel;
import com.pragma.user360.domain.ports.in.UserServicePort;
import com.pragma.user360.domain.utils.constants.DomainConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;
import java.util.Objects;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DatabaseSeeder.class);
    private final UserServicePort userServicePort;

    // Administrador
    @Value("${app.admin-user.email:admin@example.com}")
    private String adminUserEmail;
    @Value("${app.admin-user.password:admin}")
    private String adminUserPassword;
    @Value("${app.admin-user.firstName: John}")
    private String adminUserFirstName;
    @Value("${app.admin-user.lastName: Doe}")
    private String adminUserLastName;
    @Value("${app.admin-user.documentId:00000001}")
    private String adminUserDocumentId;
    @Value("${app.admin-user.phoneNumber:+1000000001}")
    private String adminUserPhoneNumber;

    // Vendedor
    @Value("${app.seller-user.email:seller@example.com}")
    private String sellerUserEmail;
    @Value("${app.seller-user.password:alice}")
    private String sellerUserPassword;
    @Value("${app.seller-user.firstName:Alice}")
    private String sellerUserFirstName;
    @Value("${app.seller-user.lastName: Doe}")
    private String sellerUserLastName;
    @Value("${app.seller-user.documentId:00000002}")
    private String sellerUserDocumentId;
    @Value("${app.seller-user.phoneNumber:+1000000002}")
    private String sellerUserPhoneNumber;

    // Cliente
    @Value("${app.customer-user.email:customer@example.com}")
    private String customerUserEmail;
    @Value("${app.customer-user.password:homero}")
    private String customerUserPassword;
    @Value("${app.customer-user.firstName: Homero }")
    private String customerUserFirstName;
    @Value("${app.customer-user.lastName: Doe}")
    private String customerUserLastName;
    @Value("${app.customer-user.documentId:00000003}")
    private String customerUserDocumentId;
    @Value("${app.customer-user.phoneNumber:+1000000003}")
    private String customerUserPhoneNumber;

    public DatabaseSeeder(UserServicePort userServicePort) {
        this.userServicePort = Objects.requireNonNull(userServicePort);
    }

    private record UserSeedData(String email, String password, String firstName, String lastName, String documentId,
                                String phoneNumber, String role, int ageOffset) {
    }

    @Override
    public void run(String... args) throws Exception {
        createAndRegisterUserIfNotExists(new UserSeedData(adminUserEmail, adminUserPassword, adminUserFirstName, adminUserLastName, adminUserDocumentId, adminUserPhoneNumber, DomainConstants.ROLE_ADMIN, 30));
        createAndRegisterUserIfNotExists(new UserSeedData(sellerUserEmail, sellerUserPassword, sellerUserFirstName, sellerUserLastName, sellerUserDocumentId, sellerUserPhoneNumber, DomainConstants.ROLE_SELLER, 25));
        createAndRegisterUserIfNotExists(new UserSeedData(customerUserEmail, customerUserPassword, customerUserFirstName, customerUserLastName, customerUserDocumentId, customerUserPhoneNumber, DomainConstants.ROLE_CUSTOMER, 20));
    }

    private void createAndRegisterUserIfNotExists(UserSeedData seedData) {
        if (!userServicePort.existsByEmail(seedData.email())) {
            if (seedData.documentId() != null && !seedData.documentId().isBlank() && userServicePort.existsByDocumentId(seedData.documentId())) {
                log.warn("Usuario con email '{}' no existe, pero el documento '{}' ya está registrado. No se creará.", seedData.email(), seedData.documentId());
                return;
            }

            UserModel user = new UserModel();
            user.setFirstName(seedData.firstName());
            user.setLastName(seedData.lastName());
            user.setDocumentId(seedData.documentId());
            user.setPhoneNumber(seedData.phoneNumber());
            user.setBirthDate(LocalDate.now().minusYears(seedData.ageOffset()));
            user.setEmail(seedData.email());
            user.setPassword(seedData.password());
            user.setActive(true);
            user.setRole(seedData.role());

            try {
                UserModel createdUser = userServicePort.registerUser(user);
                log.info("Usuario '{}' creado con ID: {} y rol (deseado/real): {}/{}", createdUser.getEmail(), createdUser.getId(), seedData.role(), createdUser.getRole());
            } catch (Exception e) {
                log.error("Error al crear usuario por defecto '{}': {}", seedData.email(), e.getMessage(), e);
            }
        } else {
            log.info("Usuario por defecto con email '{}' ya existe.", seedData.email());
        }
    }
}
