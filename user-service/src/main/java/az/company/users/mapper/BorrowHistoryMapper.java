package az.company.users.mapper;

import az.company.users.dao.entity.BorrowHistoryEntity;
import az.company.users.model.response.BorrowHistoryResponse;

public class BorrowHistoryMapper {
    public static BorrowHistoryResponse toResponse(BorrowHistoryEntity entity) {
        return BorrowHistoryResponse.builder()
                .id(entity.getId())
                .bookId(entity.getId())
                .bookTitle(entity.getBookTitle())
                .borrowedAt(entity.getBorrowedAt())
                .returnedAt(entity.getReturnedAt())
                .status(entity.getStatus())
                .build();
    }
}
