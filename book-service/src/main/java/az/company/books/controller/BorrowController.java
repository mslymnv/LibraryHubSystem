package az.company.books.controller;

import az.company.books.model.request.BorrowBookRequest;
import az.company.books.model.response.BorrowResponse;
import az.company.books.service.BorrowService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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
                       @AuthenticationPrincipal Long userId) {

        borrowService.borrow(request, userId);
    }
    @PutMapping("/{id}/return")
    public void returnBook(@PathVariable Long id) {
        borrowService.returnBook(id);
    }

    @GetMapping
    public Page<BorrowResponse> getBorrows(Pageable pageable) {
        return borrowService.getBorrows(pageable);
    }

    @PutMapping("/overdue")
    public void checkOverdue() {
        borrowService.checkBorrowStatus();
    }
}

