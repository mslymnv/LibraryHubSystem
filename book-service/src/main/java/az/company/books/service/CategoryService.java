package az.company.books.service;

import az.company.books.dao.repository.CategoryRepository;
import az.company.books.exception.CategoryAlreadyCreatedException;
import az.company.books.exception.NotFoundException;
import az.company.books.exception.enums.ErrorStatus;
import az.company.books.mapper.CategoryMapper;
import az.company.books.model.request.CreateCategoryRequest;
import az.company.books.model.request.UpdateCategoryRequest;
import az.company.books.model.response.CategoryResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

import static az.company.books.exception.enums.ErrorStatus.*;
import static az.company.books.mapper.CategoryMapper.*;
import static java.lang.String.format;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService {
    private final CategoryRepository categoryRepository;
    public CategoryResponse createCategory(CreateCategoryRequest createCategoryRequest) {
        if(categoryRepository.findByName(createCategoryRequest.getName()).isPresent()) {
            throw new CategoryAlreadyCreatedException(
                    CATEGORY_ALREADY_CREATED.name(),
                    format(CATEGORY_ALREADY_CREATED.getMessage(), createCategoryRequest.getName())
            );
        }
        var entity = mapCategoryRequestToCategoryEntity(createCategoryRequest);
        categoryRepository.save(entity);
        return mapCategoryEntityToCategoryResponse(entity);
    }
    public CategoryResponse updateCategory(UpdateCategoryRequest updateCategoryRequest) {
        var entity = categoryRepository.findById(updateCategoryRequest.getCategoryId())
                .orElseThrow(
                        () -> new NotFoundException(
                                CATEGORY_NOT_FOUND.name(),
                                format(CATEGORY_NOT_FOUND.getMessage(), updateCategoryRequest.getCategoryId())
                        ));
        entity.setDescription(updateCategoryRequest.getDescription());
        categoryRepository.save(entity);
        return mapCategoryEntityToCategoryResponse(entity);
    }
    public void deleteCategory(Long id) {
        var entity = categoryRepository.findById(id)
                .orElseThrow(
                        () -> new NotFoundException(
                                CATEGORY_NOT_FOUND.name(),
                                format(CATEGORY_NOT_FOUND.getMessage(), id)
                        )
                );
        categoryRepository.delete(entity);
    }
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(CategoryMapper::mapCategoryEntityToCategoryResponse)
                .toList();
    }
    public CategoryResponse getCategoryById(Long id) {
        var entity = categoryRepository.findById(id)
                .orElseThrow(
                        () -> new NotFoundException(
                                CATEGORY_NOT_FOUND.name(),
                                format(CATEGORY_NOT_FOUND.getMessage(), id)
                        )
                );
        return mapCategoryEntityToCategoryResponse(entity);
    }
}
