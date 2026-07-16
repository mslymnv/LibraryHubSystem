package az.company.users.service.abstraction;

import az.company.users.model.request.LoginRequest;
import az.company.users.model.request.RefreshTokenRequest;
import az.company.users.model.request.UserRegisterRequest;
import az.company.users.model.response.LoginResponse;
import az.company.users.model.response.UserResponse;

public interface AuthService {
    UserResponse registerUser(UserRegisterRequest userRegisterRequest);

    LoginResponse login(LoginRequest loginRequest);

    LoginResponse refresh(RefreshTokenRequest request);
    void logout(String username);
}
