package az.company.users.controller;

import az.company.users.model.request.UpdateProfilRequest;
import az.company.users.model.response.BorrowHistoryResponse;
import az.company.users.model.response.UserResponse;
import az.company.users.security.UserPrincipal;
import az.company.users.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

}
