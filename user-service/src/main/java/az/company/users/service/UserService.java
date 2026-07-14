package az.company.users.service;

import az.company.users.dao.entity.UserEntity;
import az.company.users.dao.repository.UserRepository;
import az.company.users.exception.NotFoundException;
import az.company.users.mapper.UserMapper;
import az.company.users.model.enums.UserStatus;
import az.company.users.model.request.UpdateProfilRequest;
import az.company.users.model.response.BorrowHistoryResponse;
import az.company.users.model.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;

import static az.company.users.exception.enums.ErrorStatus.USER_NOT_FOUND;
import static az.company.users.mapper.UserMapper.mapUserEntityToUserResponse;
import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserResponse getUserById(Long userId) {
        var userEntity = getUserEntity(userId);
        return mapUserEntityToUserResponse(userEntity);
    }
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserMapper::mapUserEntityToUserResponse)
                .toList();

    }

    public UserResponse updateUserProfile(Long userId, UpdateProfilRequest updateProfilRequest) {
        var userEntity = getUserEntity(userId);
        userEntity.setFullName(updateProfilRequest.getFullName());
        userEntity.setEmail(updateProfilRequest.getEmail());
        userRepository.save(userEntity);
        return mapUserEntityToUserResponse(userEntity);
    }

    public List<BorrowHistoryResponse> getUserBorrowHistory(Long userId) {
        var userEntity = getUserEntity(userId);
        return userEntity.getBorrowHistory().stream()
                .map(borrowHistoryEntity -> BorrowHistoryResponse.builder()
                        .id(borrowHistoryEntity.getId())
                        .bookId(borrowHistoryEntity.getBookId())
                        .bookTitle(borrowHistoryEntity.getBookTitle())
                        .borrowedAt(borrowHistoryEntity.getBorrowedAt())
                        .returnedAt(borrowHistoryEntity.getReturnedAt())
                        .status(borrowHistoryEntity.getStatus())

                        .build())
                .toList();
    }

    private @NonNull UserEntity getUserEntity(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException(USER_NOT_FOUND.name(),
                        format(USER_NOT_FOUND.getMessage(), userId)
                )
        );
    }
    public void deleteUser(Long userId) {
        var userEntity = getUserEntity(userId);
        userEntity.setStatus(UserStatus.INACTIVE);
    }

}
