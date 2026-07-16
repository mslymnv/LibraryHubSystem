package az.company.books.service.abstraction;

import az.company.books.model.request.CreateCategoryRequest;
import az.company.books.model.request.UpdateCategoryRequest;
import az.company.books.model.response.CategoryResponse;

import java.util.List;


public interface CategoryService {
    CategoryResponse createCategory(CreateCategoryRequest createCategoryRequest);
     CategoryResponse updateCategory(UpdateCategoryRequest updateCategoryRequest) ;
     void deleteCategory(Long id);
    List<CategoryResponse> getAllCategories();
     CategoryResponse getCategoryById(Long id) ;
}
