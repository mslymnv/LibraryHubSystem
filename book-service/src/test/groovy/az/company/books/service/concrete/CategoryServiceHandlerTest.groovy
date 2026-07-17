package az.company.books.service.concrete

import az.company.books.dao.entity.CategoryEntity
import az.company.books.dao.repository.CategoryRepository
import az.company.books.exception.CategoryAlreadyCreatedException
import az.company.books.exception.NotFoundException
import az.company.books.mapper.CategoryMapper
import az.company.books.model.request.CreateCategoryRequest
import az.company.books.model.request.UpdateCategoryRequest
import az.company.books.model.response.CategoryResponse
import spock.lang.Specification

import java.time.LocalDateTime

class CategoryServiceHandlerTest extends Specification {

    private CategoryRepository categoryRepository
    private CategoryMapper categoryMapper
    private CategoryServiceHandler categoryServiceHandler

    def setup() {
        categoryRepository = Mock(CategoryRepository)
        categoryMapper = Mock(CategoryMapper)
        categoryServiceHandler = new CategoryServiceHandler(categoryRepository, categoryMapper)
    }

    def "CreateCategoryShouldReturnCreatedCategory"() {
        given:
        def request = new CreateCategoryRequest(
                "Science Fiction",
                "A genre of speculative fiction."
        )

        def entity = new CategoryEntity(
                1L,
                request.name,
                request.description,
                LocalDateTime.now(),
                null
        )

        def response = new CategoryResponse(
                entity.id,
                entity.name,
                entity.description,
                entity.createdAt
        )

        when:
        def result = categoryServiceHandler.createCategory(request)

        then:
        1 * categoryRepository.findByName(request.name) >> Optional.empty()
        1 * categoryMapper.mapCategoryRequestToCategoryEntity(request) >> entity
        1 * categoryRepository.save(entity) >> entity
        1 * categoryMapper.mapCategoryEntityToCategoryResponse(entity) >> response

        result == response
    }

    def "CreateCategoryShouldThrowExceptionWhenCategoryAlreadyExists"() {
        given:
        def request = new CreateCategoryRequest(
                "Science Fiction",
                "A genre of speculative fiction."
        )

        def entity = new CategoryEntity(
                1L,
                request.name,
                request.description,
                LocalDateTime.now(),
                null
        )

        when:
        categoryServiceHandler.createCategory(request)

        then:
        1 * categoryRepository.findByName(request.name) >> Optional.of(entity)

        thrown(CategoryAlreadyCreatedException)
    }

    def "UpdateCategoryShouldReturnUpdatedCategory"() {
        given:
        def request = new UpdateCategoryRequest(
                1L,
                "Updated description"
        )

        def entity = new CategoryEntity(
                1L,
                "Science Fiction",
                "Old description",
                LocalDateTime.now(),
                null
        )

        def response = new CategoryResponse(
                entity.id,
                entity.name,
                request.description,
                entity.createdAt
        )

        when:
        def result = categoryServiceHandler.updateCategory(request)

        then:
        1 * categoryRepository.findById(1L) >> Optional.of(entity)
        1 * categoryRepository.save({
            it.description == request.description
        }) >> entity
        1 * categoryMapper.mapCategoryEntityToCategoryResponse(entity) >> response

        result == response
    }

    def "UpdateCategoryShouldThrowNotFoundException"() {
        given:
        def request = new UpdateCategoryRequest(
                1L,
                "Updated description"
        )

        when:
        categoryServiceHandler.updateCategory(request)

        then:
        1 * categoryRepository.findById(1L) >> Optional.empty()

        thrown(NotFoundException)
    }

    def "DeleteCategoryShouldDeleteCategory"() {
        given:
        def entity = new CategoryEntity(
                1L,
                "Science Fiction",
                "Description",
                LocalDateTime.now(),
                null
        )

        when:
        categoryServiceHandler.deleteCategory(1L)

        then:
        1 * categoryRepository.findById(1L) >> Optional.of(entity)
        1 * categoryRepository.delete(entity)
    }

    def "DeleteCategoryShouldThrowNotFoundException"() {

        when:
        categoryServiceHandler.deleteCategory(1L)

        then:
        1 * categoryRepository.findById(1L) >> Optional.empty()

        thrown(NotFoundException)
    }

    def "GetAllCategoriesShouldReturnCategoryList"() {
        given:
        def entity1 = new CategoryEntity(
                1L,
                "Science Fiction",
                "Description 1",
                LocalDateTime.now(),
                null
        )

        def entity2 = new CategoryEntity(
                2L,
                "History",
                "Description 2",
                LocalDateTime.now(),
                null
        )

        def response1 = new CategoryResponse(
                entity1.id,
                entity1.name,
                entity1.description,
                entity1.createdAt
        )

        def response2 = new CategoryResponse(
                entity2.id,
                entity2.name,
                entity2.description,
                entity2.createdAt
        )

        when:
        def result = categoryServiceHandler.getAllCategories()

        then:
        1 * categoryRepository.findAll() >> [entity1, entity2]
        1 * categoryMapper.mapCategoryEntityToCategoryResponse(entity1) >> response1
        1 * categoryMapper.mapCategoryEntityToCategoryResponse(entity2) >> response2

        result == [response1, response2]
    }

    def "GetCategoryByIdShouldReturnCategory"() {
        given:
        def entity = new CategoryEntity(
                1L,
                "Science Fiction",
                "Description",
                LocalDateTime.now(),
                null
        )

        def response = new CategoryResponse(
                entity.id,
                entity.name,
                entity.description,
                entity.createdAt
        )

        when:
        def result = categoryServiceHandler.getCategoryById(1L)

        then:
        1 * categoryRepository.findById(1L) >> Optional.of(entity)
        1 * categoryMapper.mapCategoryEntityToCategoryResponse(entity) >> response

        result == response
    }

    def "GetCategoryByIdShouldThrowNotFoundException"() {

        when:
        categoryServiceHandler.getCategoryById(1L)

        then:
        1 * categoryRepository.findById(1L) >> Optional.empty()

        thrown(NotFoundException)
    }
}