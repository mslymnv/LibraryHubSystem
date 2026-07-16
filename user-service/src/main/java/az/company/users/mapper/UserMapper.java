package az.company.users.mapper;

import az.company.users.dao.entity.UserEntity;
import az.company.users.model.enums.UserRoles;
import az.company.users.model.enums.UserStatus;
import az.company.users.model.request.UserRegisterRequest;
import az.company.users.model.response.UserResponse;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.time.LocalDateTime;
import java.util.Set;
@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "lastLoginAt", ignore = true)
    @Mapping(target = "borrowHistory", ignore = true)
    UserEntity toEntity(UserRegisterRequest request);

    @Mapping(source = "role", target = "roles")
    UserResponse toResponse(UserEntity entity);

    @AfterMapping
    default void setDefaults(@MappingTarget UserEntity userEntity) {
        userEntity.setRole(Set.of(UserRoles.USER));
        userEntity.setStatus(UserStatus.ACTIVE);
        userEntity.setCreatedAt(LocalDateTime.now());
        userEntity.setUpdatedAt(LocalDateTime.now());
    }
}