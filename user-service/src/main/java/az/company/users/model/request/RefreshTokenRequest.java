package az.company.users.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static az.company.users.exception.constants.ApplicationConstants.REFRESH_TOKEN_VALIDATION;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RefreshTokenRequest {
    @NotBlank(message = REFRESH_TOKEN_VALIDATION)
    private String refreshToken;
}
