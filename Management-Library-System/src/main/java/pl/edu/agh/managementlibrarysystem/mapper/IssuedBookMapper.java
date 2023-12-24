package pl.edu.agh.managementlibrarysystem.mapper;

import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.springframework.stereotype.Component;
import pl.edu.agh.managementlibrarysystem.DTO.IssuedBookDTO;
import pl.edu.agh.managementlibrarysystem.model.Book;
import pl.edu.agh.managementlibrarysystem.model.IssuedBook;
import pl.edu.agh.managementlibrarysystem.model.keys.IssuedBooksKey;

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

        return IssuedBookDTO.builder()
                .issuedID("ISSUED-" + id.getBook_id() + "-" + id.getUser_id())
                .bookISBN(book.getIsbn())
                .bookTitle(book.getTitle())
                .userID(issuedBook.getUser().getId())
                .userFullName(issuedBook.getUser().getFirstname() + " " + issuedBook.getUser().getLastname())
                .issuedDate(issuedBook.getIssuedDate().toString())
                .returnedDate(issuedBook.getReturnedDate().toString())
                .days(issuedBook.getDays())
                .fee(issuedBook.getFee())
                .build();
    }

}
