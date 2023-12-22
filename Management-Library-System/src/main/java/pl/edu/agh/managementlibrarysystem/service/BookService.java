package pl.edu.agh.managementlibrarysystem.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.edu.agh.managementlibrarysystem.model.Book;
import pl.edu.agh.managementlibrarysystem.repository.*;
import pl.edu.agh.managementlibrarysystem.utils.Alerts;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final IssuedBooksRepository issuedBooksRepository;
    private final ReadBooksRepository readBooksRepository;
    private final AuthorRepository authorRepository;
    private final PublisherRepository publishersRepository;
    private final GenresRepository genresRepository;

    public boolean saveBook(Book book, String authorName, String authorLastname, String publisherName, String genreType) {
        String isbn = book.getIsbn();

        if (this.bookRepository.findByIsbn(isbn).isPresent()) {
            Alerts.showErrorAlert("Book already exist", "Book with given isbn " + isbn + " already exists");
            return false;
        }

        return this.bookRepository.saveNewBookWithGivenParams(book, authorName, authorLastname, publisherName, genreType).isPresent();
    }
}
