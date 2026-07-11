package az.company.users.controller;

import az.company.users.model.request.LoginRequest;
import az.company.users.model.request.UserRegisterRequest;
import az.company.users.model.response.LoginResponse;
import az.company.users.model.response.UserResponse;
import az.company.users.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    @ResponseStatus(CREATED)
    public UserResponse registerUser(@Valid @RequestBody UserRegisterRequest userRegisterRequest) {
        return authService.registerUser(userRegisterRequest);
    }
    @PostMapping("/login")
    @ResponseStatus(CREATED)
    public LoginResponse login(@Valid @RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }
}