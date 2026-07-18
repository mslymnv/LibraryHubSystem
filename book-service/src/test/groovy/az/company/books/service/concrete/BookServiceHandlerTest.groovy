package az.company.books.service.concrete

import az.company.books.dao.entity.BookEntity
import az.company.books.dao.entity.CategoryEntity
import az.company.books.dao.repository.BookRepository
import az.company.books.dao.repository.CategoryRepository
import az.company.books.exception.BookAlreadyCreatedException
import az.company.books.exception.IsbnAlreadyUsedException
import az.company.books.exception.NotFoundException
import az.company.books.mapper.BookMapper
import az.company.books.model.enums.BookStatus
import az.company.books.model.request.CreateBookRequest
import az.company.books.model.request.UpdateBookRequest
import az.company.books.model.response.BookResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import spock.lang.Specification

import java.time.LocalDateTime
import java.time.Year

class BookServiceHandlerTest extends Specification {

    private BookRepository bookRepository
    private CategoryRepository categoryRepository
    private BookMapper bookMapper
    private BookServiceHandler bookServiceHandler

    def setup() {
        bookRepository = Mock()
        categoryRepository = Mock()
        bookMapper = Mock()

        bookServiceHandler = new BookServiceHandler(
                bookRepository,
                categoryRepository,
                bookMapper
        )
    }

    def "CreateBookShouldReturnCreatedBook"() {
        given:
        def request = new CreateBookRequest(
                "Clean Code",
                "Robert Martin",
                "Programming",
                "ISBN",
                5,
                Year.of(2008),
                1L
        )

        def category = new CategoryEntity(
                1L,
                "Programming",
                "Programming books",
                LocalDateTime.now(),
                null
        )

        def entity = new BookEntity(

        )
        entity.title = request.title
        entity.author = request.author
        entity.description = request.description

        def response = new BookResponse()
        response.title = entity.title
        response.author = entity.author

        when:
        def result = bookServiceHandler.createBook(request)
        then:
        1 * categoryRepository.findById(1L) >> Optional.of(category)
        1 * bookMapper.mapBookRequestToBookEntity(request) >> entity
        1 * bookRepository.findAllBooksByAuthorOrTitle(request.author, request.title) >> []
        1 * bookRepository.findByIsbn(entity.getIsbn()) >> Optional.empty()
        1 * bookRepository.save(entity) >> entity
        1 * bookMapper.mapBookEntityToBookResponse(entity) >> response

        result == response
    }

    def "CreateBookShouldThrowWhenCategoryNotFound"() {
        given:
        def request = new CreateBookRequest(
                "Clean Code",
                "Robert Martin",
                "Programming",
                "ISBN",
                5,
                Year.of(2008),
                1L
        )

        when:
        bookServiceHandler.createBook(request)

        then:
        1 * categoryRepository.findById(1L) >> Optional.empty()

        thrown(NotFoundException)
    }


    def "CreateBookShouldThrowWhenBookAlreadyCreated"() {
        given:
        def request = new CreateBookRequest(
                "Clean Code",
                "Robert Martin",
                "Programming",
                "ISBN",
                5,
                Year.of(2008),
                1L
        )

        def category = new CategoryEntity()
        category.id = 1L

        def entity = new BookEntity()

        def response = new BookResponse()
        response.title = request.title
        response.author = request.author

        when:
        bookServiceHandler.createBook(request)

        then:
        1 * categoryRepository.findById(1L) >> Optional.of(category)
        1 * bookMapper.mapBookRequestToBookEntity(request) >> entity
        1 * bookRepository.findAllBooksByAuthorOrTitle(request.author, request.title) >> [entity]
        1 * bookMapper.mapBookEntityToBookResponse(entity) >> response

        thrown(BookAlreadyCreatedException)
    }

    def "CreateBookShouldThrowWhenIsbnAlreadyUsed"() {
        given:
        def request = new CreateBookRequest(
                "Clean Code",
                "Robert Martin",
                "Programming",
                "ISBN",
                5,
                Year.of(2008),
                1L
        )

        def category = new CategoryEntity()
        category.id = 1L

        def book = new BookEntity()
        def bookInDb = new BookEntity()
        book.setIsbn("ISBN")
        bookInDb.setIsbn("ISBN")
        def response = new BookResponse()
        response.title = request.title
        response.author = request.author

        when:
        bookServiceHandler.createBook(request)

        then:
        1 * categoryRepository.findById(1L) >> Optional.of(category)
        1 * bookMapper.mapBookRequestToBookEntity(request) >> book
        1 * bookRepository.findAllBooksByAuthorOrTitle(request.author, request.title) >> []
        1 * bookRepository.findByIsbn(request.getIsbn()) >> Optional.of(bookInDb)


        thrown(IsbnAlreadyUsedException)
    }

    def "UpdateBookShouldReturnUpdatedBook"() {
        given:
        def request = new UpdateBookRequest(
                1L,
                "Effective Java",
                "Joshua Bloch",
                "Java",
                10,
                Year.of(2008),
                BookStatus.ACTIVE,
                2L
        )

        def entity = new BookEntity()
        entity.id = 1L

        def category = new CategoryEntity()
        category.id = 2L

        def response = new BookResponse()
        response.title = request.title

        when:
        def result = bookServiceHandler.updateBook(request)

        then:
        1 * bookRepository.findById(1L) >> Optional.of(entity)
        1 * categoryRepository.findById(2L) >> Optional.of(category)
        1 * bookRepository.save(entity) >> entity
        1 * bookMapper.mapBookEntityToBookResponse(entity) >> response

        result == response
    }

    def "UpdateBookShouldThrowWhenBookNotFound"() {
        given:
        def request = new UpdateBookRequest()

        request.id = 1L

        when:
        bookServiceHandler.updateBook(request)

        then:
        1 * bookRepository.findById(1L) >> Optional.empty()

        thrown(NotFoundException)
    }

    def "UpdateBookShouldThrowWhenCategoryNotFound"() {
        given:
        def request = new UpdateBookRequest()
        request.id = 1L
        request.categoryId = 5L

        def entity = new BookEntity()

        when:
        bookServiceHandler.updateBook(request)

        then:
        1 * bookRepository.findById(1L) >> Optional.of(entity)
        1 * categoryRepository.findById(5L) >> Optional.empty()

        thrown(NotFoundException)
    }

    def "DeleteBookShouldMakeBookInactive"() {
        given:
        def entity = new BookEntity()
        entity.status = BookStatus.ACTIVE

        when:
        bookServiceHandler.deleteBook(1L)

        then:
        1 * bookRepository.findById(1L) >> Optional.of(entity)
        1 * bookRepository.save({
            it.status == BookStatus.INACTIVE
        }) >> entity
    }

    def "DeleteBookShouldThrowWhenBookNotFound"() {

        when:
        bookServiceHandler.deleteBook(1L)

        then:
        1 * bookRepository.findById(1L) >> Optional.empty()

        thrown(NotFoundException)
    }

    def "GetBooksShouldReturnPage"() {
        given:
        def pageable = PageRequest.of(0, 10)

        def entity = new BookEntity()
        entity.title = "Clean Code"

        def response = new BookResponse()
        response.title = "Clean Code"

        Page<BookEntity> page = new PageImpl<>([entity])

        when:
        def result = bookServiceHandler.getBooks(pageable, 1L, "Robert Martin")

        then:
        1 * bookRepository.findAllBooks(pageable, 1L, "Robert Martin") >> page
        1 * bookMapper.mapBookEntityToBookResponse(entity) >> response

        result.content == [response]
    }

    def "GetBookByIdShouldReturnBook"() {
        given:
        def entity = new BookEntity()
        entity.id = 1L

        def response = new BookResponse()
        response.id = 1L

        when:
        def result = bookServiceHandler.getBookById(1L)

        then:
        1 * bookRepository.findById(1L) >> Optional.of(entity)
        1 * bookMapper.mapBookEntityToBookResponse(entity) >> response

        result == response
    }

    def "GetBookByIdShouldThrowWhenBookNotFound"() {

        when:
        bookServiceHandler.getBookById(1L)

        then:
        1 * bookRepository.findById(1L) >> Optional.empty()

        thrown(NotFoundException)
    }

    def "GetBooksByAuthorOrTitleShouldReturnBooks"() {
        given:
        def entity = new BookEntity()
        entity.title = "Clean Code"

        def response = new BookResponse()
        response.title = "Clean Code"

        when:
        def result = bookServiceHandler.getBooksByAuthorOrTitle(
                "Robert Martin",
                "Clean Code"
        )

        then:
        1 * bookRepository.findAllBooksByAuthorOrTitle(
                "Robert Martin",
                "Clean Code"
        ) >> [entity]

        1 * bookMapper.mapBookEntityToBookResponse(entity) >> response

        result == [response]
    }
}