package az.company.users.service;

import az.company.users.dao.entity.UserEntity;
import az.company.users.dao.repository.UserRepository;
import az.company.users.exception.NotFoundException;
import az.company.users.exception.UserPresentException;
import az.company.users.model.enums.UserStatus;
import az.company.users.model.request.LoginRequest;
import az.company.users.model.request.UserRegisterRequest;
import az.company.users.model.response.LoginResponse;
import az.company.users.model.response.UserResponse;
import az.company.users.security.UserPrincipal;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;

import static az.company.users.exception.enums.ErrorStatus.*;
import static az.company.users.mapper.UserMapper.*;
import static az.company.users.model.enums.UserRoles.ADMIN;
import static az.company.users.model.enums.UserRoles.USER;
import static az.company.users.model.enums.UserStatus.INACTIVE;
import static java.lang.String.format;

@Service
@RequiredArgsConstructor
@Transactional

public class AuthService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final TokenStorageService tokenStorageService;
    private final PasswordEncoder passwordEncoder;

    public UserResponse registerUser(UserRegisterRequest userRegisterRequest) {
        if (userRepository.findByUsername(userRegisterRequest.getUsername()).isPresent()) {
            throw new UserPresentException(
                    USER_ALREADY_EXIST.name(),
                    format(USER_ALREADY_EXIST.getMessage(), userRegisterRequest.getUsername())
            );
        }
        var userEntity = mapUserRegisterRequestToUserEntity(userRegisterRequest);
        userEntity.setPassword(
                passwordEncoder.encode(userRegisterRequest.getPassword())
        );
        if (userRepository.countUsers() == 0) {
            userEntity.setRole(Set.of(ADMIN, USER));
        }
        userRepository.save(userEntity);
        return mapUserEntityToUserResponse(userEntity);

    }

    public LoginResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        String accesToken = jwtService.generateAccesToken(userPrincipal);
        tokenStorageService.storeAccessToken(userPrincipal.getUsername(), accesToken);
        UserEntity userEntity = userRepository.findById(userPrincipal.getId()).orElseThrow(
                ()-> new NotFoundException(
                        USER_NOT_FOUND.name(),
                        format(USER_NOT_FOUND.getMessage(), userPrincipal.getUsername())
                )
        );
        userEntity.setLastLoginAt(LocalDateTime.now());
        return LoginResponse
                .builder()
                .accessToken(accesToken)
                .build();


    }
    //scheduled(cron__ every 10 minutes
    @Scheduled(cron = "*/10 * * * * *  ")
    public void checkUsersLastLogin(){

        var list=userRepository.findAll().stream()
                .filter(userEntity -> userEntity
                        .getLastLoginAt()
                        .isBefore(LocalDateTime.now()
                        .minusDays(90))).toList();
        for (UserEntity userEntity : list) {
            userEntity.setStatus(INACTIVE);
        }
    }

}
