package az.company.books.controller;

import az.company.books.model.request.CreateCategoryRequest;
import az.company.books.model.request.UpdateCategoryRequest;
import az.company.books.model.response.CategoryResponse;
import az.company.books.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/v1/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    @ResponseStatus(CREATED)
    public CategoryResponse createCategory(@Valid @RequestBody CreateCategoryRequest createCategoryRequest) {
        return categoryService.createCategory(createCategoryRequest);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/update")
    @ResponseStatus(OK)
    public CategoryResponse updateCategory(@Valid @RequestBody UpdateCategoryRequest updateCategoryRequest) {
        return categoryService.updateCategory(updateCategoryRequest);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public void deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
    }

    @GetMapping("/{id}")
    @ResponseStatus(OK)
    public CategoryResponse getCategory(@PathVariable Long id) {
        return categoryService.getCategoryById(id);
    }

    @GetMapping
    @ResponseStatus(OK)
    public List<CategoryResponse> getCategories() {
        return categoryService.getAllCategories();
    }
}
