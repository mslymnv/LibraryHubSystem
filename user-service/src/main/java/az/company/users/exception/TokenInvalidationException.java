package az.company.users.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TokenInvalidationException extends RuntimeException {
    private String code;
    public TokenInvalidationException(String code, String message) {
        super(message);
        this.code = code;
    }
}
