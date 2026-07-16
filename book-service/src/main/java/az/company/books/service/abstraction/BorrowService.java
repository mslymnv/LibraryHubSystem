package az.company.books.service.abstraction;

import az.company.books.model.request.BorrowBookRequest;
import az.company.books.model.response.BorrowResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BorrowService {
    BorrowResponse borrow(BorrowBookRequest borrowBookRequest, Long id);
    void returnBook(Long borrowId, Long userId);
    Page<BorrowResponse> getBorrows(Pageable pageable);
    Page<BorrowResponse> getBorrowsByUserId(Long userId, Pageable pageable);
}