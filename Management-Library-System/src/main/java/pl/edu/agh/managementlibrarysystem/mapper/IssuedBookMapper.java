package pl.edu.agh.managementlibrarysystem.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.edu.agh.managementlibrarysystem.DTO.IssuedBookDTO;
import pl.edu.agh.managementlibrarysystem.mapper.abstraction.Mapper;
import pl.edu.agh.managementlibrarysystem.model.Book;
import pl.edu.agh.managementlibrarysystem.model.IssuedBook;
import pl.edu.agh.managementlibrarysystem.model.User;
import pl.edu.agh.managementlibrarysystem.model.keys.IssuedBooksKey;
import pl.edu.agh.managementlibrarysystem.repository.BookRepository;
import pl.edu.agh.managementlibrarysystem.repository.UserRepository;
import pl.edu.agh.managementlibrarysystem.utils.FxmlPropertyFactory;

import java.sql.Date;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class IssuedBookMapper implements Mapper<IssuedBook, IssuedBookDTO> {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    @Override
    public IssuedBook mapToEntity(IssuedBookDTO object) {
        Book book = this.bookRepository.findByIsbn(object.getBookISBN().getValue()).orElse(null);
        User user = this.userRepository.findById(object.getUserID().getValue()).orElse(null);
        return IssuedBook.builder()
                .id(IssuedBooksKey.builder()
                        .bookId(book.getId())
                        .userId(user.getId())
                        .build())
                .book(book)
                .user(user)
                .issuedDate(Date.valueOf(object.getIssuedDate().getValue()))
                .returnedDate(Objects.equals(object.getReturnedDate().getValue(), "---") ? null : Date.valueOf(object.getReturnedDate().getValue()))
                .days(object.getDays().getValue())
                .fee(object.getFee().getValue())
                .isTaken(object.getIsTaken().getValue().equalsIgnoreCase("yes"))
                .build();
    }

    @Override
    public IssuedBookDTO mapToDto(IssuedBook issuedBook) {
        IssuedBooksKey id = issuedBook.getId();
        Book book = issuedBook.getBook();
        Date returnedDate = issuedBook.getReturnedDate();

        return IssuedBookDTO.builder()
                .issuedID(FxmlPropertyFactory.stringProperty("ISSUED-" + id.getBookId() + "-" + id.getUserId()))
                .bookISBN(FxmlPropertyFactory.stringProperty(book.getIsbn()))
                .bookTitle(FxmlPropertyFactory.stringProperty(book.getTitle()))
                .userID(FxmlPropertyFactory.longProperty(issuedBook.getUser().getId()))
                .userEmail(FxmlPropertyFactory.stringProperty(issuedBook.getUser().getEmail()))
                .userFullName(FxmlPropertyFactory.stringProperty(issuedBook.getUser().getFirstname() + " " + issuedBook.getUser().getLastname()))
                .issuedDate(FxmlPropertyFactory.stringProperty(issuedBook.getIssuedDate().toString()))
                .returnedDate(returnedDate == null ? FxmlPropertyFactory.stringProperty("---") : FxmlPropertyFactory.stringProperty(returnedDate.toString()))
                .days(FxmlPropertyFactory.integerProperty(issuedBook.getDays()))
                .fee(FxmlPropertyFactory.doubleProperty(issuedBook.getFee()))
                .isTaken(issuedBook.isTaken() ? FxmlPropertyFactory.stringProperty("Yes") : FxmlPropertyFactory.stringProperty("No"))
                .build();
    }

}
