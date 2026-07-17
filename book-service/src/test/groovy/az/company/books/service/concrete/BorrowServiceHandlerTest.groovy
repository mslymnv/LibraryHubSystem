package az.company.books.service.concrete

import az.company.books.dao.entity.BookEntity
import az.company.books.dao.entity.BorrowEntity
import az.company.books.dao.repository.BookRepository
import az.company.books.dao.repository.BorrowRepository
import az.company.books.exception.BookAlreadyBorrowedException
import az.company.books.exception.ConflictException
import az.company.books.exception.NotFoundException
import az.company.books.mapper.BorrowMapper
import az.company.books.model.enums.BookStatus
import az.company.books.model.enums.BorrowStatus
import az.company.books.model.request.BorrowBookRequest
import az.company.books.model.response.BorrowResponse
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import spock.lang.Specification

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Year


class BorrowServiceHandlerTest extends Specification {

    private BorrowRepository borrowRepository
    private BookRepository bookRepository
    private RabbitTemplate rabbitTemplate
    private BorrowMapper borrowMapper
    private BorrowServiceHandler borrowServiceHandler


    def setup() {
        borrowRepository = Mock(BorrowRepository)
        bookRepository = Mock(BookRepository)
        rabbitTemplate = Mock(RabbitTemplate)
        borrowMapper = Mock(BorrowMapper)

        borrowServiceHandler = new BorrowServiceHandler(
                borrowRepository,
                bookRepository,
                rabbitTemplate,
                borrowMapper
        )
    }


    def "BorrowShouldReturnCreatedBorrow"() {

        given:

        def request = new BorrowBookRequest(
                1L
        )

        def book = new BookEntity(
                1L,
                "Clean Code",
                "Robert C. Martin",
                "9780132350884",
                "A handbook of agile software craftsmanship",
                5,
                3,
                Year.of(2002),
                LocalDateTime.now(),
                null,
                BookStatus.ACTIVE,
                null,
                null
        )


        def response = new BorrowResponse(
                1L,
                10L,
                1L,
                LocalDateTime.now(),
                LocalDate.now().plusDays(14),
                null,
                BorrowStatus.BORROWED
        )


        when:

        def result = borrowServiceHandler.borrow(request, 10L)


        then:

        1 * bookRepository.findById(1L) >> Optional.of(book)

        1 * borrowRepository.findByBookIdAndUserId(1L, 10L)
                >> Optional.empty()

        1 * borrowRepository.save(_ as BorrowEntity)

        1 * rabbitTemplate.convertAndSend(_, _, _)

        1 * borrowMapper.mapBorrowEntityToBorrowResponse(_)
                >> response


        result == response
        book.availableCopies == 2
    }


    def "BorrowShouldThrowExceptionWhenBookNotFound"() {

        given:

        def request = new BorrowBookRequest(
                1L
        )


        when:

         borrowServiceHandler.borrow(request, 10L)


        then:

        1 * bookRepository.findById(1L)
                >> Optional.empty()


        thrown(NotFoundException)
    }


    def "BorrowShouldThrowExceptionWhenBookAlreadyBorrowed"() {

        given:

        def request = new BorrowBookRequest(
                1L
        )


        def book = new BookEntity(
                1L,
                "Clean Code",
                "Robert C. Martin",
                "9780132350884",
                "Description",
                5,
                3,
                null,
                LocalDateTime.now(),
                null,
                BookStatus.ACTIVE,
                null,
                null
        )


        def borrow = new BorrowEntity(
                1L,
                10L,
                LocalDateTime.now(),
                LocalDate.now().plusDays(14),
                null,
                BorrowStatus.BORROWED,
                book
        )


        when:

        borrowServiceHandler.borrow(request, 10L)


        then:

        1 * bookRepository.findById(1L)
                >> Optional.of(book)


        2 * borrowRepository.findByBookIdAndUserId(1L, 10L)
                >> Optional.of(borrow)


        thrown(BookAlreadyBorrowedException)
    }


    def "BorrowShouldThrowExceptionWhenNoAvailableCopies"() {

        given:

        def request = new BorrowBookRequest(
                1L
        )


        def book = new BookEntity(
                1L,
                "Clean Code",
                "Robert C. Martin",
                "9780132350884",
                "Description",
                5,
                0,
                null,
                LocalDateTime.now(),
                null,
                BookStatus.ACTIVE,
                null,
                null
        )


        when:

        borrowServiceHandler.borrow(request, 10L)


        then:

        1 * bookRepository.findById(1L) >> Optional.of(book)


        1 * borrowRepository.findByBookIdAndUserId(1L, 10L) >> Optional.empty()


        thrown(ConflictException)
    }


    def "ReturnBookShouldReturnSuccessfully"() {

        given:

        def book = new BookEntity(
                1L,
                "Clean Code",
                "Robert C. Martin",
                "9780132350884",
                "Description",
                5,
                2,
                null,
                LocalDateTime.now(),
                null,
                BookStatus.ACTIVE,
                null,
                null
        )


        def borrow = new BorrowEntity(
                1L,
                10L,
                LocalDateTime.now(),
                LocalDate.now().plusDays(14),
                null,
                BorrowStatus.BORROWED,
                book
        )


        when:

        borrowServiceHandler.returnBook(1L, 10L)


        then:

        1 * borrowRepository.findById(1L)
                >> Optional.of(borrow)


        1 * bookRepository.findById(1L)
                >> Optional.of(book)


        1 * rabbitTemplate.convertAndSend(_, _, _)


        borrow.status == BorrowStatus.RETURNED
        book.availableCopies == 3
    }


    def "ReturnBookShouldThrowExceptionWhenBorrowNotFound"() {

        when:

        borrowServiceHandler.returnBook(1L, 10L)


        then:

        1 * borrowRepository.findById(1L)
                >> Optional.empty()


        thrown(NotFoundException)
    }


    def "ReturnBookShouldThrowExceptionWhenBookNotFound"() {

        given:

        def book = new BookEntity(
                1L,
                "Clean Code",
                "Robert C. Martin",
                "9780132350884",
                "Description",
                5,
                2,
                null,
                LocalDateTime.now(),
                null,
                BookStatus.ACTIVE,
                null,
                null
        )


        def borrow = new BorrowEntity(
                1L,
                10L,
                LocalDateTime.now(),
                LocalDate.now().plusDays(14),
                null,
                BorrowStatus.BORROWED,
                book
        )


        when:

        borrowServiceHandler.returnBook(1L, 10L)


        then:

        1 * borrowRepository.findById(1L)
                >> Optional.of(borrow)


        1 * bookRepository.findById(1L)
                >> Optional.empty()


        thrown(NotFoundException)
    }


    def "GetBorrowsShouldReturnBorrowPage"() {

        given:

        def pageable = PageRequest.of(0, 10)

        def entity = new BorrowEntity(
                1L,
                10L,
                LocalDateTime.now(),
                LocalDate.now().plusDays(14),
                null,
                BorrowStatus.BORROWED,
                null
        )


        def response = new BorrowResponse()
        response.id = 1L


        when:

        def result = borrowServiceHandler.getBorrows(pageable)


        then:

        1 * borrowRepository.findAll(pageable)
                >> new PageImpl([entity])


        1 * borrowMapper.mapBorrowEntityToBorrowResponse(entity)
                >> response


        result.content == [response]
    }


    def "GetBorrowsByUserIdShouldReturnBorrowPage"() {

        given:

        def pageable = PageRequest.of(0, 10)

        def entity = new BorrowEntity(
                1L,
                10L,
                LocalDateTime.now(),
                LocalDate.now().plusDays(14),
                null,
                BorrowStatus.BORROWED,
                null
        )


        def response = new BorrowResponse()
        response.id = 1L


        when:

        def result = borrowServiceHandler.getBorrowsByUserId(
                10L,
                pageable
        )


        then:

        1 * borrowRepository.findByUserId(10L, pageable)
                >> new PageImpl([entity])


        1 * borrowMapper.mapBorrowEntityToBorrowResponse(entity)
                >> response


        result.content == [response]
    }


    def "CheckBorrowStatusShouldUpdateOverdueBorrow"() {


        given:

        def book = new BookEntity(
                1L,
                "Clean Code",
                "Robert C. Martin",
                "9780132350884",
                "Description",
                5,
                2,
                null,
                LocalDateTime.now(),
                null,
                BookStatus.ACTIVE,
                null,
                null
        )

        def borrow = new BorrowEntity(
                1L,
                10L,
                LocalDateTime.now(),
                LocalDate.now().minusDays(1),
                null,
                BorrowStatus.BORROWED,
                book
        )


        when:

        borrowServiceHandler.checkBorrowStatus()


        then:

        1 * borrowRepository.findAll() >> [borrow]

        1 * rabbitTemplate.convertAndSend(
                BORROW_UPDATE_EXCHANGE,
                BORROW_UPDATE_ROUTING_KEY,
                _
        )

        borrow.status == BorrowStatus.OVERDUE
    }
    def "CheckBorrowStatusShouldNotUpdateFutureBorrow"() {

        given:
var entity=new BookEntity(
        1L,
        "Clean Code",
        "Robert C. Martin",
        "9780132350884",
        "Description",
        5,
        2,
        null,
        LocalDateTime.now(),
        null,
        BookStatus.ACTIVE,
        null,
        null
)
        def borrow = new BorrowEntity(
                1L,
                10L,
                LocalDateTime.now(),
                LocalDate.now().plusDays(1),
                null,
                BorrowStatus.BORROWED,
                entity
        )

        when:
        borrowServiceHandler.checkBorrowStatus()

        then:

        1 * borrowRepository.findAll() >> [borrow]

        0 * rabbitTemplate.convertAndSend(_, _, _)

        borrow.status != BorrowStatus.OVERDUE
    }
}