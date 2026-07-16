package az.company.books.controller;

import az.company.books.model.request.CreateBookRequest;
import az.company.books.model.request.UpdateBookRequest;
import az.company.books.model.response.BookResponse;
import az.company.books.service.concrete.BookServiceHandler;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/books")
public class BookController {

    private final BookServiceHandler bookServiceHandler;

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    @ResponseStatus(CREATED)
    public BookResponse createBook(@Valid @RequestBody CreateBookRequest createBookRequest) {
        return bookServiceHandler.createBook(createBookRequest);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/update")
    public BookResponse updateBook(@Valid @RequestBody UpdateBookRequest updateBookRequest) {
        return bookServiceHandler.updateBook(updateBookRequest);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public void deleteBook(@PathVariable Long id) {
        bookServiceHandler.deleteBook(id);
    }

    @GetMapping
    @ResponseStatus(OK)
    public Page<BookResponse> getBooks(Pageable pageable,
                                       @RequestParam(required = false) Long categoryId,
                                       @RequestParam(required = false) String author) {
        return bookServiceHandler.getBooks(pageable, categoryId, author);
    }

    @GetMapping("/{id}")
    @ResponseStatus(OK)
    public BookResponse getBookById(@PathVariable Long id) {
        return bookServiceHandler.getBookById(id);
    }

    @GetMapping("/search")
    @ResponseStatus(OK)
    public List<BookResponse> getBooksByAuthorOrTitle(@RequestParam(required = false) String author,
                                                      @RequestParam(required = false) String title) {
        return bookServiceHandler.getBooksByAuthorOrTitle(author, title);
    }

}