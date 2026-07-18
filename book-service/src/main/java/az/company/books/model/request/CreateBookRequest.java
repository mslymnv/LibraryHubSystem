package az.company.books.model.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Year;

import static az.company.books.exception.constants.ApplicationConstants.*;

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
    private String isbn;
    @NotNull(message = Book_Total_Copies_Required)
    @PositiveOrZero(message =Copies_Numbers_Validation )
    private Integer totalCopies;
    private Year publishedYear;
    @NotNull(message = Book_Category_Id_Required)
    @Positive(message =  Id_Numbers_Validation)
    private Long categoryId;
}
