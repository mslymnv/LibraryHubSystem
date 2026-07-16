package az.company.books.mapper;

import az.company.books.dao.entity.CategoryEntity;
import az.company.books.model.request.CreateCategoryRequest;
import az.company.books.model.response.CategoryResponse;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.time.LocalDateTime;
@Mapper(componentModel = "spring")
public interface CategoryMapper {
    @Mapping(target = "id",ignore = true)
    @Mapping(target = "createdAt",ignore = true)
    CategoryEntity mapCategoryRequestToCategoryEntity(CreateCategoryRequest request);

    @AfterMapping
    default void setCreatedAt(@MappingTarget CategoryEntity entity) {
        entity.setCreatedAt(LocalDateTime.now());
    }

   CategoryResponse mapCategoryEntityToCategoryResponse(CategoryEntity entity) ;
}
