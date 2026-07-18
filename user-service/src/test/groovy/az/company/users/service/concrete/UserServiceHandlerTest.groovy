package az.company.users.service.concrete

import az.company.users.dao.entity.BorrowHistoryEntity
import az.company.users.dao.entity.UserEntity
import az.company.users.dao.repository.UserRepository
import az.company.users.exception.NotFoundException
import az.company.users.mapper.UserMapper
import az.company.users.model.enums.BorrowStatus
import az.company.users.model.enums.UserRoles
import az.company.users.model.enums.UserStatus
import az.company.users.model.request.UpdateProfilRequest
import az.company.users.model.response.BorrowHistoryResponse
import az.company.users.model.response.UserResponse
import spock.lang.Specification

import java.time.LocalDateTime

class UserServiceHandlerTest extends Specification {
    private UserRepository userRepository;
    private UserMapper userMapper;
    private UserServiceHandler userServiceHandler

    void setup() {
        userRepository = Mock()
        userMapper = Mock()
        userServiceHandler = new UserServiceHandler(
                userRepository,
                userMapper
        )
    }

    def "GetUserByIdReturnUser"() {
        given:
        def userId = 1L
        def userEntity = new UserEntity()
        userEntity.setId(1L)
        def response = new UserResponse()
        response.setId(1L)
        when:
        def result = userServiceHandler.getUserById(userId)
        then:
        1 * userRepository.findById(userId) >> Optional.of(userEntity)
        1 * userMapper.toResponse(userEntity) >> response
        result == response

    }

    def "GetUserByIdThrowUserNotFound"() {
        given:
        def userId = 1L
        when:
        userServiceHandler.getUserById(userId)
        then:
        1 * userRepository.findById(userId) >> Optional.empty()
        thrown(NotFoundException)
    }

    def "GetAllUsersReturnListUserResponse"() {
        given:
        def entity = new UserEntity(
                1L,
                "username",
                "example@gmail.com",
                "password2026!",
                "Jhon Doe",
                Set.of(
                        UserRoles.ADMIN,
                        UserRoles.USER),
                UserStatus.ACTIVE,
                LocalDateTime.now(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                null
        )
        def response = new UserResponse(
                1L,
                "username",
                UserStatus.ACTIVE,

                "Jhon Doe",

                "example@gmail.com",

                LocalDateTime.now(),
                Set.of(
                        UserRoles.ADMIN,
                        UserRoles.USER),

        )
        when:
        def result = userServiceHandler.getAllUsers()
        then:
        1 * userRepository.findAll() >> [entity]
        1 * userMapper.toResponse(entity) >> response
        result == [response]
    }

    def "GetAllUsersReturnListUsersTableIsEmpty"() {

        given:
        when:
        def result = userServiceHandler.getAllUsers()
        then:
        1 * userRepository.findAll() >> []
        result == []
    }

    def "UpdateUserProfileReturnUserResponse"() {
        given:
        def userId = 1L
        def request = new UpdateProfilRequest(
                "Jhon Doe",
                "jhondoe26@gmail.com"
        )
        def entity = new UserEntity(
                1L,
                "username",
                "example@gmail.com",
                "password2026!",
                "Jhon Doe",
                Set.of(
                        UserRoles.ADMIN,
                        UserRoles.USER),
                UserStatus.ACTIVE,
                LocalDateTime.now(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                null
        )
        def response = new UserResponse(
                1L,
                "username",
                UserStatus.ACTIVE,

                "Jhon Doe",

                "example@gmail.com",

                LocalDateTime.now(),
                Set.of(
                        UserRoles.ADMIN,
                        UserRoles.USER),

        )
        when:
        def result = userServiceHandler.updateUserProfile(userId, request)
        then:
        1 * userRepository.findById(userId) >> Optional.of(entity)
        1 * userMapper.toResponse(entity) >> response
        result == response
    }

    def "UpdateUserProfileThrownUserNotFound"() {
        given:
        def userId = 1L

        def request = new UpdateProfilRequest()
        when:
        userServiceHandler.updateUserProfile(userId, request)
        then:
        1 * userRepository.findById(userId) >> Optional.empty()
        thrown(NotFoundException)

    }

    def "GetUserBorrowHistoryReturnBorrowHistoryResponseList"() {
        given:
        def userId = 1L
        def userEntity = new UserEntity()
        userEntity.setId(1L)
        def borrowedAt = LocalDateTime.now()

        def returnedAt = LocalDateTime.now().plusDays(5)

        def borrowHistory = new BorrowHistoryEntity(
                1L,
                1L,
                "Clean Code",
                borrowedAt,
                returnedAt,
                BorrowStatus.RETURNED,
                userEntity)
        userEntity.setBorrowHistory(borrowHistory as List<BorrowHistoryEntity>)
        def response = new BorrowHistoryResponse(
                1L,
                1L,
                "Clean Code",
                borrowedAt,
                returnedAt,
                BorrowStatus.RETURNED
        )
        when:
        def result = userServiceHandler.getUserBorrowHistory(userId)
        then:
        1 * userRepository.findById(userId) >> Optional.of(userEntity)
        result == [response]

    }

    def "GetUserBorrowHistoryThrownNotFound"() {
        given:
        def userId =1

        when:
         userServiceHandler.getUserBorrowHistory(userId)
        then:
        1 * userRepository.findById(userId) >> Optional.empty()
        thrown(NotFoundException)

    }

    def "DeleteUser"(){
        given:
        def userId=1L
        def userEntity=new UserEntity(
                1L,
                "username",
                "example@gmail.com",
                "password2026!",
                "Jhon Doe",
                Set.of(
                        UserRoles.ADMIN,
                        UserRoles.USER),
                UserStatus.ACTIVE,
                LocalDateTime.now(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                null
        )
        when:
        userServiceHandler.deleteUser(userId)
        then:
        1 * userRepository.findById(userId)>>Optional.of(userEntity)


    }

    def "DeleteUserThrownNotFound"() {

        given:
        def userId =1

        when:
        userServiceHandler.getUserBorrowHistory(userId)
        then:
        1 * userRepository.findById(userId) >> Optional.empty()
        thrown(NotFoundException)}
}
