package az.company.users.exception.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access=AccessLevel.PRIVATE)
public class ApplicationConstants {
    public static final String USERNAME_VALIDATIION = "Username is required";
    public static final String EMAIL_VALIDATIION = "Email is required";
    public static final String PASSWORD_VALIDATIION = "Password is required";
    public static final String USERNAME_SIZE_VALIDATION = "Username size has to be between 3 and 50 characters";
    public static final String PASSWORD_SIZE_VALIDATION = "Password size has to be minimum 8 characters";
    public static final String FULLNAME_SIZE_VALIDATION = "Full name size has to be maximum 100 characters";

}
