package az.company.books.mapper;

import az.company.books.dao.entity.BookEntity;
import az.company.books.model.request.CreateBookRequest;
import az.company.books.model.response.BookResponse;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;


import static az.company.books.model.enums.BookStatus.ACTIVE;
import static java.time.LocalDateTime.now;

@Mapper(componentModel = "spring")
public interface BookMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "availableCopies", source = "totalCopies")
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "borrows",ignore = true)
    @Mapping(target = "category",ignore = true)
    BookEntity mapBookRequestToBookEntity(CreateBookRequest createBookRequest);

    @AfterMapping
    default void setDefaults(@MappingTarget BookEntity bookEntity) {

        bookEntity.setStatus(ACTIVE);
        bookEntity.setCreatedAt(now());
        bookEntity.setUpdatedAt(now());


    }

    @Mapping(target = "categoryName", source = "category.name")
    BookResponse mapBookEntityToBookResponse(BookEntity bookEntity);


}
