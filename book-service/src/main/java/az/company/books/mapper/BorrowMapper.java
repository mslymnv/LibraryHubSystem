package az.company.books.mapper;

import az.company.books.dao.entity.BorrowEntity;
import az.company.books.model.response.BorrowResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BorrowMapper {
    @Mapping(target = "bookId", source = "book.id")
     BorrowResponse mapBorrowEntityToBorrowResponse(BorrowEntity entity);


}
