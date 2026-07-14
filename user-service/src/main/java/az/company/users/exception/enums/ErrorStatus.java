package az.company.users.exception.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorStatus {
    USER_ALREADY_EXIST("User is already exists with this username: %s"),
    BORROW_NOT_FOUND("Borrow not found with this id: %s"),
    USER_NOT_FOUND("User not found with this username: %s");
    private final String message;
}
