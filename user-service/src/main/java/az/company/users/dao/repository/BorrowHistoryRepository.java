package az.company.users.dao.repository;

import az.company.users.dao.entity.BorrowHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BorrowHistoryRepository extends JpaRepository<BorrowHistoryEntity,Long> {
}
