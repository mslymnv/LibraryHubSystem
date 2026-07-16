package az.company.users.service.abstraction;

import az.company.users.model.request.UpdateProfilRequest;
import az.company.users.model.response.BorrowHistoryResponse;
import az.company.users.model.response.UserResponse;

import java.util.List;

public interface UserService {
    UserResponse getUserById(Long userId);
    UserResponse updateUserProfile(Long userId, UpdateProfilRequest updateProfilRequest);
    List<UserResponse> getAllUsers();
    List<BorrowHistoryResponse> getUserBorrowHistory(Long userId);
    void deleteUser(Long userId);
}
