package az.company.books.model.request;

import az.company.books.exception.constants.ApplicationConstants;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static az.company.books.exception.constants.ApplicationConstants.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BorrowBookRequest {
   @NotNull(message = Book_Id_Required)
   @Positive(message =  Id_Numbers_Validation)

    private Long bookId;
}
