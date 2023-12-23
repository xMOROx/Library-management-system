package pl.edu.agh.managementlibrarysystem.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.edu.agh.managementlibrarysystem.DTO.BookDTO;
import pl.edu.agh.managementlibrarysystem.enums.BookAvailability;
import pl.edu.agh.managementlibrarysystem.model.Book;

@Component
@RequiredArgsConstructor
public class BookMapper implements Mapper<Book, BookDTO> {

    @Override
    public Book mapToEntity(BookDTO object) {
        return null; // TODO - implement
    }

    @Override
    public BookDTO mapToDto(Book book) {
        return BookDTO.builder()
                .title(book.getTitle())
                .isbn(book.getIsbn())
                .publisher(book.getPublisher().getName())
                .author(this.getAuthors(book))
                .mainGenre(this.getGenre(book))
                .edition(book.getEdition())
                .quantity(book.getQuantity())
//                .remainingBooks(book.getRemainingBooks()) //TODO: implement when repository will be able to count it
                .availability(getAvailability(book))
                .build();
    }

    private Boolean getAvailability(Book book) {
        return BookAvailability.fromStringToBoolean(book.getAvailability());
    }

    private String getAuthors(Book book) {
        return book.getAuthors()
                .stream()
                .limit(1)
                .map(author -> (long) book.getAuthors().size() > 1 ?
                        author.getFirstname() + " " + author.getLastname() + "..." : author.getFirstname() + " " + author.getLastname())
                .toString();
    }

    private String getGenre(Book book) {
        return book.getGenres()
                .stream()
                .filter(genre -> genre.getParentGenre() == null)
                .toList()
                .get(0)
                .getGenre();

    }
}
