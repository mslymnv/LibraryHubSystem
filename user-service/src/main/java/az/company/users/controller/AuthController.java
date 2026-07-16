package az.company.users.controller;

import az.company.users.model.request.LoginRequest;
import az.company.users.model.request.RefreshTokenRequest;
import az.company.users.model.request.UserRegisterRequest;
import az.company.users.model.response.LoginResponse;
import az.company.users.model.response.UserResponse;
import az.company.users.service.concrete.AuthServiceHandler;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthServiceHandler authServiceHandler;

    @PostMapping("/register")
    @ResponseStatus(CREATED)
    public UserResponse registerUser(@Valid @RequestBody UserRegisterRequest userRegisterRequest) {
        return authServiceHandler.registerUser(userRegisterRequest);
    }
    @PostMapping("/login")
    @ResponseStatus(CREATED)
    public LoginResponse login(@Valid @RequestBody LoginRequest loginRequest) {
        return authServiceHandler.login(loginRequest);
    }
    @PostMapping("/refresh")
    @ResponseStatus(CREATED)
    public LoginResponse refresh(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        return authServiceHandler.refresh(refreshTokenRequest);
    }

    @DeleteMapping("/{userName}")
    @ResponseStatus(NO_CONTENT)
    public void delete(@PathVariable @AuthenticationPrincipal String userName) {
        authServiceHandler.logout(userName);
    }
}