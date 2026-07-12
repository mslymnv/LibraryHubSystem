package az.company.books.service;

import az.company.books.dao.repository.BookRepository;
import az.company.books.dao.repository.CategoryRepository;
import az.company.books.exception.NotFoundException;
import az.company.books.mapper.BookMapper;
import az.company.books.model.enums.BookStatus;
import az.company.books.model.request.CreateBookRequest;
import az.company.books.model.request.UpdateBookRequest;
import az.company.books.model.response.BookResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.List;

import static az.company.books.exception.enums.ErrorStatus.*;
import static az.company.books.mapper.BookMapper.*;
import static az.company.books.model.enums.BookStatus.*;
import static java.lang.String.format;

@Service
@RequiredArgsConstructor
@Transactional

public class BookService {
    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;
    @CacheEvict(value = "books", allEntries = true)
    public BookResponse createBook(CreateBookRequest createBookRequest) {
        var category = categoryRepository.findById(createBookRequest.getCategoryId())
                .orElseThrow(
                        () -> new NotFoundException(
                                CATEGORY_NOT_FOUND.name(),
                                format(CATEGORY_NOT_FOUND.getMessage(), createBookRequest.getCategoryId())
                        )
                );


        var bookEntity = mapBookRequestToBookEntity(createBookRequest);
        getBooksByAuthorOrTitle(createBookRequest.getAuthor(), createBookRequest.getTitle())
                .stream()
                .findFirst()
                .ifPresent(
                        bookResponse -> {
                            throw new NotFoundException(
                                    BOOK_ALREADY_EXISTS.name(),
                                    BOOK_ALREADY_EXISTS.getMessage()

                            );
                        }
                );

        bookEntity.setCategory(category);
        bookRepository.save(bookEntity);
        return mapBookEntityToBookResponse(bookEntity);
    }

    @CacheEvict(value = "books", allEntries = true)
    public BookResponse updateBook(UpdateBookRequest updateBookRequest) {
        var entity = bookRepository.findById(updateBookRequest.getId())
                .orElseThrow(
                        () -> new NotFoundException(
                                BOOK_NOT_FOUND.name(),
                                format(BOOK_NOT_FOUND.getMessage(), updateBookRequest.getId())
                        )
                );
        entity.setTitle(updateBookRequest.getTitle());
        entity.setAuthor(updateBookRequest.getAuthor());
        entity.setDescription(updateBookRequest.getDescription());
        entity.setTotalCopies(updateBookRequest.getTotalCopies());
        entity.setAvailableCopies(updateBookRequest.getTotalCopies());
        entity.setPublishedYear(updateBookRequest.getPublishedYear());
        entity.setStatus(updateBookRequest.getStatus());
        var category = categoryRepository.findById(updateBookRequest.getCategoryId()).orElseThrow(
                () -> new NotFoundException(
                        CATEGORY_NOT_FOUND.name(),
                        format(CATEGORY_NOT_FOUND.getMessage(), updateBookRequest.getCategoryId())
                )
        );
        entity.setCategory(category);
        bookRepository.save(entity);
        return mapBookEntityToBookResponse(entity);
    }

    @CacheEvict(value = "books", allEntries = true)
    public void deleteBook(Long id) {
        var entity = bookRepository.findById(id)
                .orElseThrow(
                        () -> new NotFoundException(
                                BOOK_NOT_FOUND.name(),
                                format(BOOK_NOT_FOUND.getMessage(), id)
                        )
                );
        entity.setStatus(INACTIVE);
        bookRepository.save(entity);
    }

    public Page<BookResponse> getBooks(Pageable pageable, Long categoryId, String author) {
        return bookRepository.findAllBooks(pageable, categoryId, author)
                .map(BookMapper::mapBookEntityToBookResponse);
    }

    @Cacheable(value = "books", key = "#id")
    public BookResponse getBookById(Long id) {
        var entity = bookRepository.findById(id)
                .orElseThrow(
                        () -> new NotFoundException(
                                BOOK_NOT_FOUND.name(),
                                format(BOOK_NOT_FOUND.getMessage(), id)
                        )
                );
        return mapBookEntityToBookResponse(entity);
    }

    public List<BookResponse> getBooksByAuthorOrTitle(String author, String title) {
        return bookRepository.findAllBooksByAuthorOrTitle(author, title)
                .stream()
                .map(BookMapper::mapBookEntityToBookResponse)
                .toList();
    }

}



