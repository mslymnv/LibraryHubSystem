package az.company.books.controller;

import az.company.books.model.request.BorrowBookRequest;
import az.company.books.model.response.BorrowResponse;
import az.company.books.service.BorrowService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/v1/borrows")
@RequiredArgsConstructor
public class BorrowController {
    private final BorrowService borrowService;

    @PostMapping
    @ResponseStatus(CREATED)
    public void borrow(@RequestBody BorrowBookRequest request,
                       @AuthenticationPrincipal Long id) {

        borrowService.borrow(request,id);
    }
    @PutMapping("/{bookId}/return")
    public void returnBook(@PathVariable Long bookId,@AuthenticationPrincipal Long userId) {
        borrowService.returnBook(bookId,userId);
    }
    @GetMapping("/my")
    public Page<BorrowResponse> getMyBorrows(@AuthenticationPrincipal Long userId, Pageable pageable) {
        return borrowService.getBorrowsByUserId(userId, pageable);
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public Page<BorrowResponse> getBorrows(Pageable pageable) {
        return borrowService.getBorrows(pageable);
    }
    @PreAuthorize("hasAuthority('ADMIN')")

    @PutMapping("/overdue")
    public void checkOverdue() {
        borrowService.checkBorrowStatus();
    }
}

