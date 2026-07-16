package az.company.users.controller;

import az.company.users.model.request.UpdateProfilRequest;
import az.company.users.model.response.BorrowHistoryResponse;
import az.company.users.model.response.UserResponse;
import az.company.users.security.UserPrincipal;
import az.company.users.service.UserServiceHandler;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserServiceHandler userServiceHandler;
    @GetMapping("/me")
    public UserResponse getUser(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return userServiceHandler.getUserById(userPrincipal.getId());
    }
    @PutMapping("/me")
    public UserResponse updateUserProfile(@AuthenticationPrincipal UserPrincipal userPrincipal, @Valid @RequestBody UpdateProfilRequest updateProfilRequest ) {
        return userServiceHandler.updateUserProfile(userPrincipal.getId(), updateProfilRequest);
    }
    @GetMapping("/me/borrows")
    public List<BorrowHistoryResponse> getUserBorrows(@AuthenticationPrincipal UserPrincipal userPrincipal) {

        return userServiceHandler.getUserBorrowHistory(userPrincipal.getId());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public UserResponse getUserById(@PathVariable Long id) {
                return userServiceHandler.getUserById(id);

    }
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getAllUsers() {
        return userServiceHandler.getAllUsers();
    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteUserById(@PathVariable Long id) {
        userServiceHandler.deleteUser(id);
    }
}
