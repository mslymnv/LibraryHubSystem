package az.company.books.controller;

import az.company.books.model.request.CreateBookRequest;
import az.company.books.model.request.UpdateBookRequest;
import az.company.books.model.response.BookResponse;
import az.company.books.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/books")
public class BookController {
    private final BookService bookService;

    @PostMapping
    @ResponseStatus(CREATED)
    public BookResponse createBook(@Valid @RequestBody CreateBookRequest createBookRequest) {
        return bookService.createBook(createBookRequest);
    }
    @PutMapping("/update")
    public BookResponse updateBook(@Valid @RequestBody UpdateBookRequest updateBookRequest) {
        return bookService.updateBook(updateBookRequest);
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public void deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
    }
    @GetMapping
    @ResponseStatus(OK)
    public Page<BookResponse> getBooks(Pageable pageable,
                                       @RequestParam(required = false) Long categoryId,
                                       @RequestParam(required = false) String author) {
        return bookService.getBooks(pageable, categoryId, author);
    }
    @GetMapping("/{id}")
    @ResponseStatus(OK)
    public BookResponse getBookById(@PathVariable Long id) {
        return bookService.getBookById(id);
    }
    @GetMapping("/search")
    @ResponseStatus(OK)
    public List<BookResponse> getBooksByAuthorOrTitle(@RequestParam(required = false) String author,
                                                      @RequestParam(required = false) String title) {
        return bookService.getBooksByAuthorOrTitle(author, title);
    }

}