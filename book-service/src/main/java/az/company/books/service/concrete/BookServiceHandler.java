package az.company.books.service.concrete;

import az.company.books.dao.repository.BookRepository;
import az.company.books.dao.repository.CategoryRepository;
import az.company.books.exception.BookAlreadyCreatedException;
import az.company.books.exception.IsbnAlreadyUsedException;
import az.company.books.exception.NotFoundException;
import az.company.books.mapper.BookMapper;
import az.company.books.model.request.CreateBookRequest;
import az.company.books.model.request.UpdateBookRequest;
import az.company.books.model.response.BookResponse;
import az.company.books.service.abstraction.BookService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.List;

import static az.company.books.exception.enums.ErrorStatus.*;
import static az.company.books.model.enums.BookStatus.*;
import static java.lang.String.format;

@Service
@RequiredArgsConstructor
@Transactional

public class BookServiceHandler implements BookService {
    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;
    private final BookMapper bookMapper;

    @CacheEvict(value = "books", allEntries = true)
    @Override
    public BookResponse createBook(CreateBookRequest createBookRequest) {
        var category = categoryRepository.findById(createBookRequest.getCategoryId())
                .orElseThrow(
                        () -> new NotFoundException(
                                CATEGORY_NOT_FOUND.name(),
                                format(CATEGORY_NOT_FOUND.getMessage(), createBookRequest.getCategoryId())
                        )
                );


        var bookEntity = bookMapper.mapBookRequestToBookEntity(createBookRequest);

        getBooksByAuthorOrTitle(createBookRequest.getAuthor(), createBookRequest.getTitle())
                .stream()
                .findFirst()
                .ifPresent(
                        bookResponse -> {
                            throw new BookAlreadyCreatedException(
                                    BOOK_ALREADY_EXISTS.name(),
                                    format(BOOK_ALREADY_EXISTS.getMessage(), createBookRequest.getTitle(), createBookRequest.getAuthor())

                            );
                        }
                );
        if(bookRepository.findByIsbn(bookEntity.getIsbn()).isPresent()){

                    throw new IsbnAlreadyUsedException(
                            ISBN_ALREADY_USED.name(),
                            format(ISBN_ALREADY_USED.getMessage(),bookEntity.getIsbn()));
                }


        {



        }

        bookEntity.setCategory(category);

        bookRepository.save(bookEntity);

        return bookMapper.mapBookEntityToBookResponse(bookEntity);
    }

    @CacheEvict(value = "books", allEntries = true)
    @Override
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
        return bookMapper.mapBookEntityToBookResponse(entity);
    }

    @CacheEvict(value = "books", allEntries = true)
    @Override
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

    @Override
    public Page<BookResponse> getBooks(Pageable pageable, Long categoryId, String author) {
        return bookRepository.findAllBooks(pageable, categoryId, author)
                .map(bookMapper::mapBookEntityToBookResponse);
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
        return bookMapper.mapBookEntityToBookResponse(entity);
    }

    @Override
    public List<BookResponse> getBooksByAuthorOrTitle(String author, String title) {
        return bookRepository.findAllBooksByAuthorOrTitle(author, title)
                .stream()
                .map(bookMapper::mapBookEntityToBookResponse)
                .toList();
    }

}



