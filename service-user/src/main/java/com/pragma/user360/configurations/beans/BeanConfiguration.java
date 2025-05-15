package com.pragma.user360.configurations.beans;

import com.pragma.user360.application.mappers.UserDtoMapper;
import com.pragma.user360.application.services.UserService;
import com.pragma.user360.application.services.impl.UserServiceImpl;
import com.pragma.user360.domain.ports.in.UserServicePort;
import com.pragma.user360.domain.ports.out.PasswordEncoderPort;
import com.pragma.user360.domain.ports.out.UserPersistencePort;
import com.pragma.user360.domain.usecases.UserUseCase;
import com.pragma.user360.infrastructure.adapters.persistence.UserPersistenceAdapter;
import com.pragma.user360.infrastructure.adapters.security.BCryptPasswordEncoderAdapter;
import com.pragma.user360.infrastructure.mappers.UserEntityMapper;
import com.pragma.user360.infrastructure.repositories.mysql.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class BeanConfiguration {

    private final UserRepository userRepository;
    private final UserEntityMapper userEntityMapper;
    private final UserDtoMapper userDtoMapper;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public PasswordEncoderPort passwordEncoder(BCryptPasswordEncoder bCryptPasswordEncoder) {
        return new BCryptPasswordEncoderAdapter(bCryptPasswordEncoder);
    }

    @Bean
    public UserPersistencePort userPersistencePort() {
        return new UserPersistenceAdapter(userRepository, userEntityMapper);
    }

    @Bean
    public UserServicePort userServicePort(UserPersistencePort userPersistencePort, PasswordEncoderPort passwordEncoder) {
        return new UserUseCase(userPersistencePort, passwordEncoder);
    }

    @Bean
    public UserService userService(UserServicePort userServicePort) {
        return new UserServiceImpl(userServicePort, userDtoMapper);
    }
}
