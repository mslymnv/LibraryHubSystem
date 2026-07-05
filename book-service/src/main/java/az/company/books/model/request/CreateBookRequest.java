package az.company.books.model.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Year;

import static az.company.books.model.constants.ApplicationConstants.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateBookRequest {
    @NotBlank(message = Book_Title_Required)
    private String title;
    @NotBlank(message = Book_Author_Required)
    private String author;
    @NotBlank(message = Book_Description_Required)
    private String description;
    @NotNull(message = Book_Total_Copies_Required)
    private Integer totalCopies;
    private Year publishedYear;
    @NotNull(message = Book_Category_Id_Required)
    private Long categoryId;
}
