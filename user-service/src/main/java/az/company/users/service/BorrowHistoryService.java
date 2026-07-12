package az.company.users.service;

import az.company.users.dao.entity.BorrowHistoryEntity;
import az.company.users.dao.repository.BorrowHistoryRepository;
import az.company.users.model.response.BorrowHistoryResponse;
import az.company.users.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BorrowHistoryService {
    private final BorrowHistoryRepository borrowHistoryRepository;

}
