package com.pragma.user360.domain.usecases;

import com.pragma.user360.domain.exceptions.UserDomainException;
import com.pragma.user360.domain.model.UserFilterModel;
import com.pragma.user360.domain.model.UserModel;
import com.pragma.user360.domain.ports.in.UserServicePort;
import com.pragma.user360.domain.ports.out.PasswordEncoderPort;
import com.pragma.user360.domain.ports.out.UserPersistencePort;
import com.pragma.user360.domain.utils.constants.DomainConstants;
import com.pragma.user360.domain.utils.pagination.PagedResult;
import org.springframework.data.domain.Page;

import java.util.Optional;

public class UserUseCase implements UserServicePort {

    private final UserPersistencePort userPersistencePort;
    private final PasswordEncoderPort passwordEncoderPort;

    public UserUseCase(UserPersistencePort userPersistencePort, PasswordEncoderPort passwordEncoderPort) {
        this.userPersistencePort = userPersistencePort;
        this.passwordEncoderPort = passwordEncoderPort;
    }

    @Override
    public UserModel registerUser(UserModel userModel) {
        validateUserUniqueness(userModel);

        userModel.setPassword(passwordEncoderPort.encode(userModel.getPassword()));
        userModel.setRole(DomainConstants.ROLE_VENDEDOR);

        return userPersistencePort.saveUser(userModel);
    }


    @Override
    public PagedResult<UserModel> getAllUsers(UserFilterModel filter) {
        return userPersistencePort.getAllUsers(filter);
    }

    @Override
    public Optional<UserModel> getUserById(Long id) {
        return userPersistencePort.getUserById(id);
    }

    @Override
    public Optional<UserModel> getUserByEmail(String email) {
        return userPersistencePort.getUserByEmail(email);
    }

    @Override
    public Optional<UserModel> getUserByDocumentId(String documentId) {
        return userPersistencePort.getUserByDocumentId(documentId);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userPersistencePort.existsByEmail(email);
    }

    @Override
    public boolean existsByDocumentId(String documentId) {
        return userPersistencePort.existsByDocumentId(documentId);
    }


    private void validateUserUniqueness(UserModel userModel) {
        if (userPersistencePort.existsByEmail(userModel.getEmail())) {
            throw new UserDomainException("El correo electrónico ya está registrado");
        }
        if (userPersistencePort.existsByDocumentId(userModel.getDocumentId())) {
            throw new UserDomainException("El número de documento ya está registrado");
        }
    }

    @Override
    public boolean deleteUser(Long id) {
        Optional<UserModel> userModel = userPersistencePort.getUserById(id);
        if (userModel.isPresent()) {
            userPersistencePort.deleteUser(id);
            return true;
        } else {
            throw new UserDomainException("El usuario no existe");
        }
    }
}
