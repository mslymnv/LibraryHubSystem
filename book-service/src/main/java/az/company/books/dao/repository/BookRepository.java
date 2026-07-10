package az.company.books.dao.repository;

import az.company.books.dao.entity.BookEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookRepository extends JpaRepository<BookEntity, Long> {
    @Query
            ("""
                    SELECT b FROM BookEntity b
                                        JOIN FETCH b.category c
                    WHERE ( :categoryId IS NULL  OR c.id= :categoryId )
                    AND (b.author = :author OR :author IS NULL)
                    
                    """)
    Page<BookEntity> findAllBooks(Pageable pageable, Long categoryId, String author);


@EntityGraph(attributePaths = {"category"})
    @Query
            ("""
                    SELECT b FROM BookEntity b
                    WHERE (b.author = :author OR :author IS NULL)
                    AND (b.title = :title OR :title IS NULL)
                    """)
    List<BookEntity> findAllBooksByAuthorOrTitle(String author, String title);
}
