package az.company.books.service.concrete;

import az.company.books.dao.repository.CategoryRepository;
import az.company.books.exception.CategoryAlreadyCreatedException;
import az.company.books.exception.NotFoundException;
import az.company.books.mapper.CategoryMapper;
import az.company.books.model.request.CreateCategoryRequest;
import az.company.books.model.request.UpdateCategoryRequest;
import az.company.books.model.response.CategoryResponse;
import az.company.books.service.abstraction.CategoryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static az.company.books.exception.enums.ErrorStatus.*;
import static az.company.books.mapper.CategoryMapper.*;
import static java.lang.String.format;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryServiceHandler implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    @Override
    public CategoryResponse createCategory(CreateCategoryRequest createCategoryRequest) {
        if(categoryRepository.findByName(createCategoryRequest.getName()).isPresent()) {
            throw new CategoryAlreadyCreatedException(
                    CATEGORY_ALREADY_CREATED.name(),
                    format(CATEGORY_ALREADY_CREATED.getMessage(), createCategoryRequest.getName())
            );
        }
        var entity = categoryMapper.mapCategoryRequestToCategoryEntity(createCategoryRequest);
        categoryRepository.save(entity);
        return categoryMapper.mapCategoryEntityToCategoryResponse(entity);
    }
    @Override
    public CategoryResponse updateCategory(UpdateCategoryRequest updateCategoryRequest) {
        var entity = categoryRepository.findById(updateCategoryRequest.getCategoryId())
                .orElseThrow(
                        () -> new NotFoundException(
                                CATEGORY_NOT_FOUND.name(),
                                format(CATEGORY_NOT_FOUND.getMessage(), updateCategoryRequest.getCategoryId())
                        ));
        entity.setDescription(updateCategoryRequest.getDescription());
        categoryRepository.save(entity);
        return categoryMapper.mapCategoryEntityToCategoryResponse(entity);
    }
    @Override
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
    @Override
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(categoryMapper::mapCategoryEntityToCategoryResponse)
                .toList();
    }
    @Override
    public CategoryResponse getCategoryById(Long id) {
        var entity = categoryRepository.findById(id)
                .orElseThrow(
                        () -> new NotFoundException(
                                CATEGORY_NOT_FOUND.name(),
                                format(CATEGORY_NOT_FOUND.getMessage(), id)
                        )
                );
        return categoryMapper.mapCategoryEntityToCategoryResponse(entity);
    }
}
