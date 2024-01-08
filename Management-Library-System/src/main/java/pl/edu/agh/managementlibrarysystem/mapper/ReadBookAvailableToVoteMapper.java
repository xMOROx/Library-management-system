package pl.edu.agh.managementlibrarysystem.mapper;

import org.springframework.stereotype.Component;
import pl.edu.agh.managementlibrarysystem.DTO.ReadBookAvailableToVoteDTO;
import pl.edu.agh.managementlibrarysystem.mapper.abstraction.OneWayMapper;
import pl.edu.agh.managementlibrarysystem.model.Book;
import pl.edu.agh.managementlibrarysystem.model.ReadBook;

@Component
public class ReadBookAvailableToVoteMapper implements OneWayMapper<ReadBook, ReadBookAvailableToVoteDTO> {
    @Override
    public ReadBookAvailableToVoteDTO map(ReadBook returnedBook) {
        Book book = returnedBook.getBook();
        String authorsAsString = Book.getAuthorsAsString(book);
        String genresAsString = Book.getGenresAsString(book);

        return ReadBookAvailableToVoteDTO.builder()
                .isbn(book.getIsbn())
                .title(book.getTitle())
                .authors(authorsAsString != null ? authorsAsString : "---")
                .publisher(book.getPublisher() != null ? book.getPublisher().getName() : "---")
                .genres(genresAsString != null ? genresAsString : "---")
                .returnedDate(String.valueOf(returnedBook.getReturnedDate()))
                .edition(book.getEdition())
                .bookId(book.getId())
                .userId(returnedBook.getUser().getId())
                .build();
    }
}
