package az.company.books.dao.repository;

import az.company.books.dao.entity.BorrowEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface BorrowRepository extends JpaRepository<BorrowEntity,Long> {
    @Override
@EntityGraph(attributePaths = {"book"})
    Page<BorrowEntity> findAll( Pageable pageable);
    Page<BorrowEntity> findByUserId(Long userId, Pageable pageable);
    Optional<BorrowEntity> findByBookIdAndUserId(Long bookId, Long userId);
}
