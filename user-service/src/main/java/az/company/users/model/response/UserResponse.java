package az.company.users.model.response;

import az.company.users.model.enums.UserRoles;
import az.company.users.model.enums.UserStatus;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonPropertyOrder({"id", "fullName", "username", "email", "roles", "createdAt", "status"})
public class UserResponse {
    private Long id;
    private String username;
    private UserStatus status;
    private String fullName;
    private String email;
    private LocalDateTime createdAt;
    private Set<UserRoles> roles;
}
