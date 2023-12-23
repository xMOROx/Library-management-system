package pl.edu.agh.managementlibrarysystem.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.edu.agh.managementlibrarysystem.DTO.BookDTO;
import pl.edu.agh.managementlibrarysystem.enums.BookAvailability;
import pl.edu.agh.managementlibrarysystem.model.Author;
import pl.edu.agh.managementlibrarysystem.model.Book;
import pl.edu.agh.managementlibrarysystem.model.Genre;
import pl.edu.agh.managementlibrarysystem.model.Publisher;

import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class BookMapper implements Mapper<Book, BookDTO> {

    @Override
    public Book mapToEntity(BookDTO object) {
        return null; // TODO - implement
    }

    @Override
    public BookDTO mapToDto(Book book) {
        Publisher publisher = book.getPublisher();
        String authors = this.getAuthors(book);
        List<Genre> mainGenres = this.getMainGenres(book);

        return BookDTO.builder()
                .isbn(book.getIsbn())
                .title(book.getTitle())
                .publisher(publisher == null ? "---" : publisher.getName())
                .author(authors == null ? "---" : authors)
                .mainGenre(mainGenres.isEmpty() ? "---" : mainGenres.get(0).getGenre())
                .edition(book.getEdition())
                .quantity(book.getQuantity())
                .remainingBooks(book.getRemainingBooks())
                .availability(book.getAvailability())
                .build();
    }

    private Boolean getAvailability(Book book) {
        return BookAvailability.fromStringToBoolean(book.getAvailability());
    }

    private String getAuthors(Book book) {
        Set<Author> bookAuthors = book.getAuthors();

        if (bookAuthors.isEmpty()) {
            return null;
        }

        return bookAuthors
                .stream()
                .reduce("", (authors, author) -> authors + author.getFirstname() + " " + author.getLastname() + (bookAuthors.size() > 1 ? ", " : ""), String::concat);
    }

    private List<Genre> getMainGenres(Book book) {
        if (book.getGenres().isEmpty()) {
            return List.of();
        }
        return book.getGenres()
                .stream()
                .filter(genre -> genre.getParentGenre() == null)
                .toList();

    }
}
