package az.company.users.service.concrete;

import az.company.users.dao.entity.UserEntity;
import az.company.users.dao.repository.UserRepository;
import az.company.users.exception.NotFoundException;
import az.company.users.exception.TokenInvalidationException;
import az.company.users.exception.UserPresentException;
import az.company.users.mapper.UserMapper;
import az.company.users.model.request.LoginRequest;
import az.company.users.model.request.RefreshTokenRequest;
import az.company.users.model.request.UserRegisterRequest;
import az.company.users.model.response.LoginResponse;
import az.company.users.model.response.UserResponse;
import az.company.users.security.UserPrincipal;
import az.company.users.security.service.JwtService;
import az.company.users.security.service.TokenStorageService;
import az.company.users.service.abstraction.AuthService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;

import static az.company.users.exception.enums.ErrorStatus.*;
import static az.company.users.model.enums.UserRoles.ADMIN;
import static az.company.users.model.enums.UserRoles.USER;
import static az.company.users.model.enums.UserStatus.INACTIVE;
import static java.lang.String.format;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AuthServiceHandler implements AuthService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final TokenStorageService tokenStorageService;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final CustomUserDetailsService userDetailsService;


    @Override
    public UserResponse registerUser(UserRegisterRequest userRegisterRequest) {
        if (userRepository.findByUsername(userRegisterRequest.getUsername()).isPresent()) {
            throw new UserPresentException(
                    USER_ALREADY_EXIST.name(),
                    format(USER_ALREADY_EXIST.getMessage(), userRegisterRequest.getUsername())
            );
        }
        var userEntity = userMapper.toEntity(userRegisterRequest);
        userEntity.setPassword(
                passwordEncoder.encode(userRegisterRequest.getPassword())
        );
        if (userRepository.countUsers() == 0) {
            userEntity.setRole(Set.of(ADMIN, USER));
        }
        userRepository.save(userEntity);
        return userMapper.toResponse(userEntity);

    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        String accesToken = jwtService.generateAccesToken(userPrincipal);
        String refreshToken = jwtService.generateRefreshToken(userPrincipal);
        tokenStorageService.storeRefreshToken(userPrincipal.getUsername(), refreshToken);
        tokenStorageService.storeAccessToken(userPrincipal.getUsername(), accesToken);
        UserEntity userEntity = userRepository.findById(userPrincipal.getId()).orElseThrow(
                () -> new NotFoundException(
                        USER_NOT_FOUND.name(),
                        format(USER_NOT_FOUND.getMessage(), userPrincipal.getUsername())
                )
        );
        userEntity.setLastLoginAt(LocalDateTime.now());
        return LoginResponse
                .builder()
                .accessToken(accesToken)
                .refreshToken(refreshToken)
                .build();


    }

    @Override
    public LoginResponse refresh(RefreshTokenRequest request) {
        String refreshToken = request.getRefreshToken();

        if (jwtService.isTokenValid(refreshToken) || !jwtService.isRefreshTokenValid(refreshToken)) {
            throw new TokenInvalidationException(
                    TOKEN_INVALID.name(),
                    format(TOKEN_INVALID.getMessage(), refreshToken)
            );
        }

        String username = jwtService.extractUsername(refreshToken);

        if (!tokenStorageService.isRefreshTokenValid(username, refreshToken)) {
            throw new TokenInvalidationException(
                    REFRESH_TOKEN_IS_NOT_ACTIVE.name(),
                    format(REFRESH_TOKEN_IS_NOT_ACTIVE.getMessage(), refreshToken)
            );
        }

        UserPrincipal userPrincipal = (UserPrincipal) userDetailsService.loadUserByUsername(username);
        String newAccessToken = jwtService.generateAccesToken(userPrincipal);
        String newRefreshToken = jwtService.generateRefreshToken(userPrincipal);

        tokenStorageService.storeAccessToken(username, newAccessToken);
        tokenStorageService.storeRefreshToken(username, newRefreshToken);

        return LoginResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
    }
    @Override
public void logout(String username) {
        tokenStorageService.deleteAccessToken(username);
        tokenStorageService.deleteRefreshToken(username);
    }

    @Scheduled(cron = "0 0 0  * * MON")
    public void checkUsersLastLogin() {


        var list = userRepository.findAll().stream()
                .filter(userEntity -> userEntity
                        .getLastLoginAt()
                        .isBefore(LocalDateTime.now()
                                .minusDays(90))).toList();
        for (var entry : list) {

            entry.setStatus(INACTIVE);
            log.info("INACTIVE USER: id: {}, username: {}, lastloginat: {}", entry.getId(), entry.getUsername(), entry.getLastLoginAt());
        }

    }


}
