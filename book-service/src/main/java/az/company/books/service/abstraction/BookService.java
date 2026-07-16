package az.company.books.service.abstraction;

import az.company.books.model.request.CreateBookRequest;
import az.company.books.model.request.UpdateBookRequest;
import az.company.books.model.response.BookResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BookService {
    BookResponse createBook(CreateBookRequest createBookRequest);
    BookResponse updateBook(UpdateBookRequest updateBookRequest);
    BookResponse getBookById(Long id);
    void deleteBook(Long id);
    Page<BookResponse> getBooks(Pageable pageable, Long categoryId, String author);
    List<BookResponse> getBooksByAuthorOrTitle(String author, String title);
}
