package pl.edu.agh.managementlibrarysystem.mapper;

import javafx.beans.property.StringProperty;
import org.springframework.stereotype.Component;
import pl.edu.agh.managementlibrarysystem.DTO.ReadBookAvailableToVoteDTO;
import pl.edu.agh.managementlibrarysystem.mapper.abstraction.OneWayMapper;
import pl.edu.agh.managementlibrarysystem.model.Book;
import pl.edu.agh.managementlibrarysystem.model.ReadBook;
import pl.edu.agh.managementlibrarysystem.utils.FxmlPropertyFactory;

@Component
public class ReadBookAvailableToVoteMapper implements OneWayMapper<ReadBook, ReadBookAvailableToVoteDTO> {
    @Override
    public ReadBookAvailableToVoteDTO map(ReadBook returnedBook) {
        Book book = returnedBook.getBook();
        String authorsAsString = Book.getAuthorsAsString(book);
        String genresAsString = Book.getGenresAsString(book);
        StringProperty placeholder = FxmlPropertyFactory.stringProperty("---");


        return ReadBookAvailableToVoteDTO.builder()
                .isbn(FxmlPropertyFactory.stringProperty(book.getIsbn()))
                .title(FxmlPropertyFactory.stringProperty(book.getTitle()))
                .authors(authorsAsString != null ? FxmlPropertyFactory.stringProperty(authorsAsString) : placeholder)
                .publisher(book.getPublisher() != null ? FxmlPropertyFactory.stringProperty(book.getPublisher().getName()) : placeholder)
                .genres(genresAsString != null ? FxmlPropertyFactory.stringProperty(genresAsString) : placeholder)
                .returnedDate(FxmlPropertyFactory.stringProperty(returnedBook.getReturnedDate().toString()))
                .edition(FxmlPropertyFactory.integerProperty(book.getEdition()))
                .bookId(FxmlPropertyFactory.longProperty(book.getId()))
                .userId(FxmlPropertyFactory.longProperty(returnedBook.getUser().getId()))
                .build();
    }
}
