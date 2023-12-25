package pl.edu.agh.managementlibrarysystem.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.edu.agh.managementlibrarysystem.DTO.IssuedBookDTO;
import pl.edu.agh.managementlibrarysystem.model.Book;
import pl.edu.agh.managementlibrarysystem.model.IssuedBook;
import pl.edu.agh.managementlibrarysystem.model.keys.IssuedBooksKey;

import java.sql.Date;

@Component
@RequiredArgsConstructor
public class IssuedBookMapper implements Mapper<IssuedBook, IssuedBookDTO> {

    @Override
    public IssuedBook mapToEntity(IssuedBookDTO object) {
        return null;
    }

    @Override
    public IssuedBookDTO mapToDto(IssuedBook issuedBook) {
        IssuedBooksKey id = issuedBook.getId();
        Book book = issuedBook.getBook();
        Date returnedDate = issuedBook.getReturnedDate();

        return IssuedBookDTO.builder()
                .issuedID("ISSUED-" + id.getBookId() + "-" + id.getUserId())
                .bookISBN(book.getIsbn())
                .bookTitle(book.getTitle())
                .userID(issuedBook.getUser().getId())
                .userEmail(issuedBook.getUser().getEmail())
                .userFullName(issuedBook.getUser().getFirstname() + " " + issuedBook.getUser().getLastname())
                .issuedDate(issuedBook.getIssuedDate().toString())
                .returnedDate(returnedDate == null ? "---" : returnedDate.toString())
                .days(issuedBook.getDays())
                .fee(issuedBook.getFee())
                .build();
    }

}
