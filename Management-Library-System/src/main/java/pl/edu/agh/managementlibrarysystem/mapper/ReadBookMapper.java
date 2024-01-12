package pl.edu.agh.managementlibrarysystem.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.edu.agh.managementlibrarysystem.DTO.BookDTO;
import pl.edu.agh.managementlibrarysystem.DTO.ReadBookDTO;
import pl.edu.agh.managementlibrarysystem.mapper.abstraction.Mapper;
import pl.edu.agh.managementlibrarysystem.mapper.abstraction.OneWayMapper;
import pl.edu.agh.managementlibrarysystem.model.Book;
import pl.edu.agh.managementlibrarysystem.model.Publisher;
import pl.edu.agh.managementlibrarysystem.model.ReadBook;

import java.util.HashSet;

@Component
@RequiredArgsConstructor
public class ReadBookMapper implements OneWayMapper<ReadBook, ReadBookDTO> {

    public ReadBookDTO map(ReadBook read) {
        Book book = read.getBook();
        Publisher publisher = book.getPublisher();
        String authors = Book.getAuthorsAsString(book);
        String genres = Book.getGenresAsString(book);

        return ReadBookDTO.builder()
                .isbn(book.getIsbn())
                .title(book.getTitle())
                .publisher(publisher == null ? "---" : publisher.getName())
                .authors(authors == null ? "---" : authors)
                .genres(genres == null ? "---" : genres)
                .edition(book.getEdition())
                .issuedDate(read.getIssuedDate())
                .returnedDate(read.getReturnedDate())
                .days(read.getDays())
                .fee(read.getFee())
                .build();
    }


}
