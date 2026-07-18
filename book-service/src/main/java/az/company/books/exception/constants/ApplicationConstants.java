package az.company.books.exception.constants;


import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApplicationConstants {
    public static final String Category_Id_Required = "Category ID cannot be null";
    public static final String Category_Name_Required = "Category name cannot be blank";
    public static final String Category_Description_Required = "Category description cannot be blank";
    public static final String Book_Title_Required = "Book title cannot be blank";
    public static final String Book_Author_Required = "Book author cannot be blank";
    public static final String Book_Description_Required = "Book description cannot be blank";
    public static final String Book_Total_Copies_Required = "Book total copies cannot be null";
    public static final String Book_Category_Id_Required = "Book category ID cannot be null";
    public static final String Book_Id_Required = "Book ID cannot be null";
    public static final String Book_Status_Required = "Book status cannot be null";
    public static final String Copies_Numbers_Validation="Copies cannot be negative";
    public static final String Id_Numbers_Validation="Id cannot be negative or zero";

}
