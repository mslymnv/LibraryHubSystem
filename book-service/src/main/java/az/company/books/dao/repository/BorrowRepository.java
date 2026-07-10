package az.company.books.dao.repository;

import az.company.books.dao.entity.BorrowEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface BorrowRepository extends JpaRepository<BorrowEntity,Long> {
@EntityGraph(attributePaths = {"book"})
    Page<BorrowEntity> findAllWithBook(Pageable pageable);
}
