package pl.edu.agh.managementlibrarysystem.mapper;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.edu.agh.managementlibrarysystem.DTO.BookDTO;
import pl.edu.agh.managementlibrarysystem.mapper.abstraction.Mapper;
import pl.edu.agh.managementlibrarysystem.model.Book;
import pl.edu.agh.managementlibrarysystem.model.Publisher;
import pl.edu.agh.managementlibrarysystem.utils.FxmlPropertyFactory;

import java.util.HashSet;

@Component
@RequiredArgsConstructor
public class BookMapper implements Mapper<Book, BookDTO> {

    @Override
    public Book mapToEntity(BookDTO book) {
        return Book.builder()
                .isbn(book.getIsbn().getValue())
                .title(book.getTitle().getValue())
                .edition(book.getEdition().getValue())
                .quantity(book.getQuantity().getValue())
                .remainingBooks(book.getRemainingBooks().getValue())
                .description(book.getDescription().getValue())
                .availability(book.getAvailability().getValue())
                .cover(book.getCover().getValue())
                .image(book.getImage().getValue())
                .tableOfContent(book.getTableOfContent().getValue())
                .genres(new HashSet<>())
                .authors(new HashSet<>())
                .readBooks(new HashSet<>())
                .issuedBooks(new HashSet<>())
                .notification(new HashSet<>())
                .description(book.getDescription().getValue())
                .tableOfContent(book.getTableOfContent().getValue())
                .build();
    }

    @Override
    public BookDTO mapToDto(Book book) {
        Publisher publisher = book.getPublisher();
        String authors = Book.getAuthorsAsString(book);
        String genres = Book.getGenresAsString(book);
        StringProperty placeholder = new SimpleStringProperty("---");

        return BookDTO.builder()
                .isbn(FxmlPropertyFactory.stringProperty(book.getIsbn()))
                .title(FxmlPropertyFactory.stringProperty(book.getTitle()))
                .publisher(publisher == null ? placeholder : FxmlPropertyFactory.stringProperty(publisher.getName()))
                .authors(authors == null ? placeholder : FxmlPropertyFactory.stringProperty(authors))
                .mainGenre(genres == null ? placeholder : FxmlPropertyFactory.stringProperty(genres))
                .edition(FxmlPropertyFactory.integerProperty(book.getEdition()))
                .quantity(FxmlPropertyFactory.integerProperty(book.getQuantity()))
                .remainingBooks(FxmlPropertyFactory.integerProperty(book.getRemainingBooks()))
                .availability(FxmlPropertyFactory.stringProperty(book.getAvailability()))
                .description(FxmlPropertyFactory.stringProperty(book.getDescription()))
                .tableOfContent(FxmlPropertyFactory.stringProperty(book.getTableOfContent()))
                .cover(FxmlPropertyFactory.stringProperty(book.getCover()))
                .build();
    }
}
