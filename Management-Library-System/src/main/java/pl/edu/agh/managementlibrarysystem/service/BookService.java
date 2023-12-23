package pl.edu.agh.managementlibrarysystem.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.edu.agh.managementlibrarysystem.DTO.BookDTO;
import pl.edu.agh.managementlibrarysystem.mapper.Mapper;
import pl.edu.agh.managementlibrarysystem.model.Book;
import pl.edu.agh.managementlibrarysystem.repository.*;
import pl.edu.agh.managementlibrarysystem.utils.Alerts;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final IssuedBooksRepository issuedBooksRepository;
    private final ReadBooksRepository readBooksRepository;
    private final AuthorRepository authorRepository;
    private final PublisherRepository publishersRepository;
    private final GenresRepository genresRepository;
    private final Mapper<Book, BookDTO> bookMapper;

    public boolean saveBook(Book book, String authorName, String authorLastname, String publisherName, String genreType) {
        String isbn = book.getIsbn();

        if (this.bookRepository.findByIsbn(isbn).isPresent()) {
            Alerts.showErrorAlert("Book already exist", "Book with given isbn " + isbn + " already exists");
            return false;
        }

        return this.bookRepository.saveNewBookWithGivenParams(book, authorName, authorLastname, publisherName, genreType).isPresent();
    }

    public List<BookDTO> getBooks() {
        List<Book> books = this.bookRepository.findAll();
        return books
                .stream()
                .map(this.bookMapper::mapToDto)
                .toList();
    }

    public Integer getSumOfAllBooks() {
        return this.bookRepository.sumOfAllBooks();
    }

    public Integer getSumOfAllRemainingBooks() {
        return this.bookRepository.sumOfAllRemainingBooks();
    }
}
