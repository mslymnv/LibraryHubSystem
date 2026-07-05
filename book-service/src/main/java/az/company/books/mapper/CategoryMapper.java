package az.company.books.mapper;

import az.company.books.dao.entity.CategoryEntity;
import az.company.books.model.request.CreateCategoryRequest;
import az.company.books.model.response.CategoryResponse;

import java.time.LocalDateTime;

public class CategoryMapper {
    public static CategoryEntity mapCategoryRequestToCategoryEntity(CreateCategoryRequest request) {
        return CategoryEntity.builder()
                .name(request.getName())
                .description(request.getDescription())
                .createdAt(LocalDateTime.now())
                .build();
    }
    public static CategoryResponse mapCategoryEntityToCategoryResponse(CategoryEntity entity) {
        return CategoryResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
