package az.company.users.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserPresentException extends RuntimeException {
    private String code;
    public UserPresentException(String code, String message) {
        super(message);
        this.code = code;
    }
}
