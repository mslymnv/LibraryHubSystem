package az.company.users.controller;

import az.company.users.model.request.UserRegisterRequest;
import az.company.users.model.response.UserResponse;
import az.company.users.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    @ResponseStatus(CREATED)
    public UserResponse registerUser(@Valid @RequestBody UserRegisterRequest userRegisterRequest) {
        return userService.registerUser(userRegisterRequest);
    }
    @GetMapping("/{id}")
    @ResponseStatus(OK)
    public UserResponse getUser(@PathVariable Long id) {
        return userService.getUserById(id);
    }
}
