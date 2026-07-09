package az.company.users.mapper;

import az.company.users.dao.entity.UserEntity;
import az.company.users.model.request.UserRegisterRequest;
import az.company.users.model.response.UserResponse;

import java.util.Set;

import static az.company.users.model.enums.UserRoles.USER;
import static az.company.users.model.enums.UserStatus.ACTIVE;
import static java.time.LocalDateTime.now;

public class UserMapper {
    public static UserEntity mapUserRegisterRequestToUserEntity(UserRegisterRequest userRegisterRequest){
        return UserEntity.builder()
                .username(userRegisterRequest.getUsername())
                .email(userRegisterRequest.getEmail())
                .password(userRegisterRequest.getPassword())
                .fullName(userRegisterRequest.getFullName())
                .role(Set.of(USER))
                .status(ACTIVE)
                .createdAt(now())
                .updatedAt(now())
                .build();
    }
    public static UserResponse mapUserEntityToUserResponse(UserEntity userEntity){
        return UserResponse.builder()
                .id(userEntity.getId())
                .username(userEntity.getUsername())
                .email(userEntity.getEmail())
                .fullName(userEntity.getFullName())
                .createdAt(userEntity.getCreatedAt())
                .updatedAt(userEntity.getUpdatedAt())
                .build();
    }
}
