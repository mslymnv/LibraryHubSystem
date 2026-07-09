package az.company.users.dao.repository;

import az.company.users.dao.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);
    @Query("SELECT COUNT(u) FROM UserEntity u ")
    Long countUsers();
}
