package az.company.books.model.client.response;

import az.company.books.model.client.enums.UserRoles;
import az.company.books.model.client.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
    private Long id;
    private String username;
    private UserStatus status;
    private String fullName;
    private String email;
    private LocalDateTime createdAt;
    private Set<UserRoles> roles;

}
