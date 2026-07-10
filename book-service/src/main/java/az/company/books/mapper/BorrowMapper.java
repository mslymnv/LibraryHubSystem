package az.company.books.mapper;

import az.company.books.dao.entity.BorrowEntity;
import az.company.books.model.response.BorrowResponse;

public class BorrowMapper {
    public static BorrowResponse mapBorrowEntityToBorrowResponse(BorrowEntity entity){
        return BorrowResponse.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .bookId(entity.getBook().getId())
                .borrowedAt(entity.getBorrowedAt())
                .dueDate(entity.getDueDate())
                .returnedAt(entity.getReturnedAt())
                .status(entity.getStatus())
                .build();
    }
}
