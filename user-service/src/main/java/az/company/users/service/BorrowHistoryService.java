package az.company.users.service;

import az.company.users.dao.repository.BorrowHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BorrowHistoryService {
    private final BorrowHistoryRepository borrowHistoryRepository;
}
