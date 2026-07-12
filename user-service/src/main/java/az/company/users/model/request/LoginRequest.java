package az.company.users.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static az.company.users.exception.constants.ApplicationConstants.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    @NotBlank(message = USERNAME_VALIDATION)
    private String username;
    @NotBlank(message = PASSWORD_VALIDATION)
    private String password;
}
