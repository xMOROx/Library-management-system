package pl.edu.agh.managementlibrarysystem.mapper;

import org.springframework.stereotype.Component;
import pl.edu.agh.managementlibrarysystem.DTO.BookDetailsDTO;
import pl.edu.agh.managementlibrarysystem.mapper.abstraction.OneWayMapper;
import pl.edu.agh.managementlibrarysystem.model.Book;
@Component
public class BookDetailsMapper implements OneWayMapper<Book, BookDetailsDTO> {
    @Override
    public BookDetailsDTO map(Book book) {
        String authorsAsString = Book.getAuthorsAsString(book);
        String genresAsString = Book.getGenresAsString(book);

        return BookDetailsDTO.builder()
                .isbn(book.getIsbn())
                .title(book.getTitle())
                .authors(authorsAsString != null ? authorsAsString : "---")
                .publisher(book.getPublisher() != null ? book.getPublisher().getName() : "---")
                .genres(genresAsString != null ? genresAsString : "---")
                .edition(book.getEdition())
                .quantity(book.getQuantity())
                .remainingBooks(book.getRemainingBooks())
                .availability(book.getAvailability())
                .description(book.getDescription())
                .cover(book.getCover())
                .image(book.getImage())
                .tableOfContent(book.getTableOfContent())
                .build();
    }

}
