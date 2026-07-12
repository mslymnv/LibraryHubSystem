package az.company.users.controller;

import az.company.users.model.request.UpdateProfilRequest;
import az.company.users.model.response.BorrowHistoryResponse;
import az.company.users.model.response.UserResponse;
import az.company.users.security.UserPrincipal;
import az.company.users.service.UserService;
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
    private final UserService userService;
    @GetMapping("/me")
    public UserResponse getUser(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return userService.getUserById(userPrincipal.getId());
    }
    @PutMapping("/me")
    public UserResponse updateUserProfile(@AuthenticationPrincipal UserPrincipal userPrincipal, @Valid @RequestBody UpdateProfilRequest updateProfilRequest ) {
        return userService.updateUserProfile(userPrincipal.getId(), updateProfilRequest);
    }
    @GetMapping("/me/borrows")
    public List<BorrowHistoryResponse> getUserBorrows(@AuthenticationPrincipal UserPrincipal userPrincipal) {

        return userService.getUserBorrowHistory(userPrincipal.getId());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public UserResponse getUserById(@PathVariable Long id) {
                return userService.getUserById(id);

    }
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getAllUsers() {
        return userService.getAllUsers();
    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteUserById(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}
