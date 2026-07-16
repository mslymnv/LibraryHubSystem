package az.company.users.exception.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorStatus {
    USER_ALREADY_EXIST("User is already exists with this username: %s"),
    BORROW_NOT_FOUND("Borrow not found with this id: %s"),
    USER_NOT_FOUND("User not found with this username: %s"),
    REFRESH_TOKEN_IS_NOT_ACTIVE("Refresh token not found : %s"),
    TOKEN_INVALID("Token is invalid: %s");
    private final String message;
}
