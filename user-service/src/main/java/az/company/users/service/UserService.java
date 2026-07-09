package az.company.users.service;

import az.company.users.dao.repository.UserRepository;
import az.company.users.exception.NotFoundException;
import az.company.users.exception.UserPresentException;
import az.company.users.model.request.UserRegisterRequest;
import az.company.users.model.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

import static az.company.users.exception.enums.ErrorStatus.*;
import static az.company.users.mapper.UserMapper.*;
import static az.company.users.model.enums.UserRoles.ADMIN;
import static az.company.users.model.enums.UserRoles.USER;
import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserResponse registerUser(UserRegisterRequest userRegisterRequest) {
        if (userRepository.findByUsername(userRegisterRequest.getUsername()).isPresent()) {
            throw new UserPresentException(
                    USER_ALREADY_EXIST.name(),
                    format(USER_ALREADY_EXIST.getMessage(), userRegisterRequest.getUsername())
            );
        }
        var userEntity = mapUserRegisterRequestToUserEntity(userRegisterRequest);
        if (userRepository.countUsers() == 0) {
            userEntity.setRole(Set.of(ADMIN, USER));
        }
        userRepository.save(userEntity);
        return mapUserEntityToUserResponse(userEntity);

    }
    public UserResponse getUserById(Long userId) {
        var userEntity = userRepository.findById(userId).orElseThrow(
                ()->new NotFoundException(
                        USER_NOT_FOUND.name(),
                        format(USER_NOT_FOUND.getMessage(), userId)
                )
        );
        return mapUserEntityToUserResponse(userEntity);
    }
}
