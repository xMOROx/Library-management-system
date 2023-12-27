package pl.edu.agh.managementlibrarysystem.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.edu.agh.managementlibrarysystem.DTO.IssuedBookDTO;
import pl.edu.agh.managementlibrarysystem.model.Book;
import pl.edu.agh.managementlibrarysystem.model.IssuedBook;
import pl.edu.agh.managementlibrarysystem.model.User;
import pl.edu.agh.managementlibrarysystem.model.keys.IssuedBooksKey;
import pl.edu.agh.managementlibrarysystem.repository.BookRepository;
import pl.edu.agh.managementlibrarysystem.repository.UserRepository;

import java.sql.Date;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class IssuedBookMapper implements Mapper<IssuedBook, IssuedBookDTO> {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    @Override
    public IssuedBook mapToEntity(IssuedBookDTO object) {
        Book book = this.bookRepository.findByIsbn(object.getBookISBN()).orElse(null);
        User user = this.userRepository.findById(object.getUserID()).orElse(null);
        return IssuedBook.builder()
                .id(IssuedBooksKey.builder()
                        .bookId(book.getId())
                        .userId(user.getId())
                        .build())
                .book(book)
                .user(user)
                .issuedDate(Date.valueOf(object.getIssuedDate()))
                .returnedDate(Objects.equals(object.getReturnedDate(), "---") ? null : Date.valueOf(object.getReturnedDate()))
                .days(object.getDays())
                .fee(object.getFee())
                .isTaken(object.getIsTaken().equalsIgnoreCase("yes"))
                .build();
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
                .isTaken(issuedBook.isTaken() ? "Yes" : "No")
                .build();
    }

}
