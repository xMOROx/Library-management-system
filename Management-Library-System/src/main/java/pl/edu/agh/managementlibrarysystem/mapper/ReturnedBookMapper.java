package pl.edu.agh.managementlibrarysystem.mapper;

import org.springframework.stereotype.Component;
import pl.edu.agh.managementlibrarysystem.DTO.ReadBookDTO;
import pl.edu.agh.managementlibrarysystem.DTO.ReturnedBookDTO;
import pl.edu.agh.managementlibrarysystem.mapper.abstraction.OneWayMapper;
import pl.edu.agh.managementlibrarysystem.model.Book;
import pl.edu.agh.managementlibrarysystem.model.ReadBook;
import pl.edu.agh.managementlibrarysystem.model.ReturnedBook;

@Component
public class ReturnedBookMapper implements OneWayMapper<ReturnedBook, ReturnedBookDTO> {


    public ReturnedBookDTO map(ReturnedBook object) {
        Book book = object.getBook();
        String authorsAsString = Book.getAuthorsAsString(book);
        String genresAsString = Book.getGenresAsString(book);

        return ReturnedBookDTO
                .builder()
                .title(book.getTitle())
                .isbn(book.getIsbn())
                .authors(authorsAsString == null ? "---" : authorsAsString)
                .genres(genresAsString == null ? "---" : genresAsString)
                .edition(book.getEdition())
                .days(object.getDays())
                .issued(object.getIssuedDate())
                .returned(object.getIssuedDate())
                .build();
    }
}
