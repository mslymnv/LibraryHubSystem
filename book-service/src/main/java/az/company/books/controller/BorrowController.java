package az.company.books.controller;

import az.company.books.model.request.BorrowBookRequest;
import az.company.books.model.response.BorrowResponse;
import az.company.books.service.concrete.BorrowServiceHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/v1/borrows")
@RequiredArgsConstructor
public class BorrowController {
    private final BorrowServiceHandler handler;

    @PostMapping
    @ResponseStatus(CREATED)
    public BorrowResponse borrow(@RequestBody BorrowBookRequest request,
                       @AuthenticationPrincipal Long id) {

       return handler.borrow(request,id);
    }
    @PutMapping("/{bookId}/return")
    public void returnBook(@PathVariable Long bookId,@AuthenticationPrincipal Long userId) {
        handler.returnBook(bookId,userId);
    }
    @GetMapping("/my")
    public Page<BorrowResponse> getMyBorrows(@AuthenticationPrincipal Long userId, Pageable pageable) {
        return handler.getBorrowsByUserId(userId, pageable);
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public Page<BorrowResponse> getBorrows(Pageable pageable) {
        return handler.getBorrows(pageable);
    }


}

