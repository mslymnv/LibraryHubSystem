package az.company.books.model.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static az.company.books.model.constants.ApplicationConstants.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateCategoryRequest {
    @NotBlank(message = Category_Name_Required)
    private String name;
    @NotBlank(message = Category_Description_Required)
    private String description;
}
