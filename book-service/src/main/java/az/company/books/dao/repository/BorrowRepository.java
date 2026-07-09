package az.company.books.dao.repository;

import az.company.books.dao.entity.BookBorrowEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface BorrowRepository extends JpaRepository<BookBorrowEntity,Long> {

}
