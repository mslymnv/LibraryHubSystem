package az.company.books.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static az.company.books.exception.constants.ApplicationConstants.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCategoryRequest {
    @NotNull(message = Category_Id_Required)
    @Positive(message =  Id_Numbers_Validation)
    private Long categoryId;
    @NotBlank(message = Category_Description_Required)
    private String description;
}
