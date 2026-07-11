package az.company.users.model.request;

import static az.company.users.exception.constants.ApplicationConstants.*;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProfilRequest {
    @Size(max = 100, message = FULLNAME_SIZE_VALIDATION)
    private String fullName;
    @NotBlank(message = EMAIL_VALIDATIION)
    @Email(message = EMAIL_VALIDATIION)
    private String email;
}
