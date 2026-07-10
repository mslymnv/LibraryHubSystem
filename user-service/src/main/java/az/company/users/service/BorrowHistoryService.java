package az.company.users.service;

import az.company.users.dao.repository.BorrowHistoryRepository;
import az.company.users.model.response.BorrowHistoryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BorrowHistoryService {
    private final BorrowHistoryRepository borrowHistoryRepository;

    public List<BorrowHistoryResponse> getBorrowHistory(Pageable pageable) {
        return borrowHistoryRepository.findAllWithUser(pageable)
                .stream()
                .map(entity -> BorrowHistoryResponse.builder()
                        .id(entity.getId())
                        .bookId(entity.getBookId())
                        .bookTitle(entity.getBookTitle())
                        .borrowedAt(entity.getBorrowedAt())
                        .returnedAt(entity.getReturnedAt())
                        .status(entity.getStatus())
                        .build())
                .toList();
    }
}
