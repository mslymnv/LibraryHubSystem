package az.company.books.scheduler

import az.company.books.config.RabbitMQConfig
import az.company.books.dao.entity.BookEntity
import az.company.books.dao.entity.BorrowEntity
import az.company.books.dao.repository.BorrowRepository
import az.company.books.model.enums.BorrowStatus
import org.springframework.amqp.rabbit.core.RabbitTemplate
import spock.lang.Specification

import java.time.LocalDate
import java.time.LocalDateTime

class BorrowSchedulerTest extends Specification {
      BorrowRepository borrowRepository=Mock()
      RabbitTemplate rabbitTemplate=Mock()
    BorrowScheduler borrowScheduler=new BorrowScheduler(
            borrowRepository,
            rabbitTemplate
    )


    def "CheckBorrowStatusShouldUpdateOverdueBorrow"() {


        given:

       def book=new BookEntity()


        def borrow = new BorrowEntity(
                1L,
                10L,
                LocalDateTime.now().minusDays(20),
                LocalDate.now().minusDays(6),
                null,
                BorrowStatus.BORROWED,
                book
        )


        when:

      borrowScheduler.checkBorrowStatus()


        then:

        1 * borrowRepository.findAll() >> [borrow]

        1 * rabbitTemplate.convertAndSend(
                RabbitMQConfig.BORROW_UPDATE_EXCHANGE,
                RabbitMQConfig.BORROW_UPDATE_ROUTING_KEY,
                _
        )

        borrow.status == BorrowStatus.OVERDUE
    }

    def "CheckBorrowStatusShouldNotUpdateFutureBorrow"() {


        given:

        def book=new BookEntity()


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

        borrowScheduler.checkBorrowStatus()


        then:

        1 * borrowRepository.findAll() >> [borrow]

        0 * rabbitTemplate.convertAndSend(
                RabbitMQConfig.BORROW_UPDATE_EXCHANGE,
                RabbitMQConfig.BORROW_UPDATE_ROUTING_KEY,
                _
        )

        borrow.status == BorrowStatus.BORROWED
    }

    def "CheckBorrowStatusShouldNotUpdateReturnedBook"() {


        given:

        def book=new BookEntity()


        def borrow = new BorrowEntity(
                1L,
                10L,
                LocalDateTime.now().minusDays(15),
                LocalDate.now().minusDays(1),
                LocalDateTime.now().minusDays(3),
                BorrowStatus.RETURNED,
                book
        )


        when:

        borrowScheduler.checkBorrowStatus()


        then:

        1 * borrowRepository.findAll() >> [borrow]

        0 * rabbitTemplate.convertAndSend(
                RabbitMQConfig.BORROW_UPDATE_EXCHANGE,
                RabbitMQConfig.BORROW_UPDATE_ROUTING_KEY,
                _
        )

        borrow.status == BorrowStatus.RETURNED
    }

    def "CheckBorrowStatusShouldNot"() {


        given:

        def book=new BookEntity()


        def borrow = new BorrowEntity(
                1L,
                10L,
                LocalDateTime.now().minusDays(14),
                LocalDate.now(),
                LocalDateTime.now().minusDays(3),
                BorrowStatus.RETURNED,
                book
        )


        when:

        borrowScheduler.checkBorrowStatus()


        then:

        1 * borrowRepository.findAll() >> []

        0 * rabbitTemplate.convertAndSend(
                RabbitMQConfig.BORROW_UPDATE_EXCHANGE,
                RabbitMQConfig.BORROW_UPDATE_ROUTING_KEY,
                _
        )

        borrow.status == BorrowStatus.RETURNED
    }


}
/*


    def "CheckBorrowStatusShouldNotUpdateFutureBorrow"() {

        given:
        var entity = new BookEntity(
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
                LocalDateTime.now(),
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

    def "CheckBorrowStatusShouldNotUpdateReturnedBorrow"() {

        given:
        var entity = new BookEntity(
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
                LocalDateTime.now().minusDays(20),
                LocalDate.now().minusDays(6),
                LocalDateTime.now(),
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
 */