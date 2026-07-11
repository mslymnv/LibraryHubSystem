package az.company.users.dao.repository;

import az.company.users.dao.entity.BorrowHistoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BorrowHistoryRepository extends JpaRepository<BorrowHistoryEntity,Long> {
    @EntityGraph(attributePaths = {"user"})
    Page<BorrowHistoryEntity> findAll(Pageable pageable);
}
