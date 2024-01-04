package pl.edu.agh.managementlibrarysystem.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.edu.agh.managementlibrarysystem.DTO.BookDTO;
import pl.edu.agh.managementlibrarysystem.mapper.abstraction.Mapper;
import pl.edu.agh.managementlibrarysystem.model.Book;
import pl.edu.agh.managementlibrarysystem.model.Genre;
import pl.edu.agh.managementlibrarysystem.model.Publisher;

import java.util.HashSet;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BookMapper implements Mapper<Book, BookDTO> {

    @Override
    public Book mapToEntity(BookDTO book) {
        return Book.builder()
                .isbn(book.getIsbn())
                .title(book.getTitle())
                .edition(book.getEdition())
                .quantity(book.getQuantity())
                .remainingBooks(book.getRemainingBooks())
                .description(book.getDescription())
                .availability(book.getAvailability())
                .cover(book.getCover())
                .image(book.getImage())
                .tableOfContent(book.getTableOfContent())
                .genres(new HashSet<>())
                .authors(new HashSet<>())
                .readBooks(new HashSet<>())
                .issuedBooks(new HashSet<>())
                .notification(new HashSet<>())
                .description(book.getDescription())
                .tableOfContent(book.getTableOfContent())
                .cover(book.getCover())
                .build();
    }

    @Override
    public BookDTO mapToDto(Book book) {
        Publisher publisher = book.getPublisher();
        String authors = Book.getAuthorsAsString(book);
        String genres = Book.getGenresAsString(book);

        return BookDTO.builder()
                .isbn(book.getIsbn())
                .title(book.getTitle())
                .publisher(publisher == null ? "---" : publisher.getName())
                .authors(authors == null ? "---" : authors)
                .mainGenre(genres == null ? "---" : genres)
                .edition(book.getEdition())
                .quantity(book.getQuantity())
                .remainingBooks(book.getRemainingBooks())
                .availability(book.getAvailability())
                .description(book.getDescription())
                .tableOfContent(book.getTableOfContent())
                .cover(book.getCover())
                .build();
    }
}
